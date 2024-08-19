package helpers;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileHelper {

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
}
