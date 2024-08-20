package helpers;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileHelper {

    private static final int WAIT_INTERVAL_MS = 1000; // Time in milliseconds between size checks
    private static final int MAX_WAIT_TIME_MS = 10000; // Maximum wait time in milliseconds

    /**
     * Deletes a folder and all of its contents recursively.
     *
     * @param folder The folder to delete.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteFolderRecursively(Path folder) throws IOException {
        // Traverse the directory tree and delete each file and directory
        System.out.println("Deleting folder: " + folder);
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file); // Delete file
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir); // Delete directory after its contents have been deleted
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Copies a file from source to destination.
     *
     * @param sourcePath The path of the file to copy.
     * @param destinationPath The path where the file should be copied to.
     * @throws IOException If an I/O error occurs.
     */
    public static void copyFile(Path sourcePath, Path destinationPath) throws IOException {
        // Copy the file from source to destination
        System.out.println("Copying webdriver from " + sourcePath + " to " + destinationPath);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes a single file.
     *
     * @param filePath The path of the file to delete.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteFile(Path filePath) throws IOException {
        // Delete the file if it exists
        if (Files.exists(filePath)) {
            System.out.println("Deleting file: " + filePath);
            Files.delete(filePath);
        }
    }

    // Method to unzip a file
    public static void unzipFile(String zipFilePath, String destDirectory) throws IOException {
        System.out.println("Extracting archive...");
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                File file = new File(destDirectory, entry.getName());
                if (entry.isDirectory()) {
                    if (!file.exists()) {
                        file.mkdir();
                    }
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] bytesIn = new byte[4096];
                        int read;
                        while ((read = zipIn.read(bytesIn)) != -1) {
                            fos.write(bytesIn, 0, read);
                        }
                    }
                }
                zipIn.closeEntry();
            }
        }
        System.out.println("File extracted succesfully!");
    }

    // Method to untar a file
    public static void untarFile(String tarFilePath, String outputDir) throws IOException {
        try (FileInputStream fis = new FileInputStream(tarFilePath);
             GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(fis);
             TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {

            ArchiveEntry entry;
            while ((entry = tarIn.getNextEntry()) != null) {
                File outputFile = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    if (!outputFile.isDirectory() && !outputFile.mkdirs()) {
                        throw new IOException("Failed to create directory: " + outputFile);
                    }
                } else {
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = tarIn.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    // Method to download a file from a URL
    public static void downloadFile(String fileURL, String saveAs) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedInputStream in = new BufferedInputStream(httpConn.getInputStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(saveAs)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            // Wait for the file to finish downloading
            waitForFileToBeFullyDownloaded(saveAs);

        } else {
            throw new IOException("No file to download. Server replied HTTP code: " + responseCode);
        }
    }

    private static void waitForFileToBeFullyDownloaded(String filePath) throws IOException {
        Path path = Path.of(filePath);
        long previousSize = 0;
        long currentSize;
        long startTime = System.currentTimeMillis();

        while (true) {
            currentSize = Files.size(path);
            if (currentSize == previousSize) {
                System.out.println("File download complete.");
                break;
            }
            previousSize = currentSize;

            if (System.currentTimeMillis() - startTime > MAX_WAIT_TIME_MS) {
                throw new IOException("Timeout waiting for file to complete download.");
            }

            try {
                Thread.sleep(WAIT_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for file to complete download.", e);
            }
        }
    }
}
