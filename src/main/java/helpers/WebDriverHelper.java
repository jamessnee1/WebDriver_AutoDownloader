package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WebDriverHelper {

    private WebDriver driver;
    private static final String CHROME_DRIVER_URL = "https://storage.googleapis.com/chrome-for-testing-public/";
    private static String DOWNLOAD_URL_TEMPLATE = CHROME_DRIVER_URL + "%s/" + "%s/chromedriver-%s.zip";
    private static String DOWNLOAD_PATH = System.getProperty("user.dir") + ConfigHelper.getInstance().getProperty("webdriver.chrome.driver");

    // Constructor to initialize the WebDriver
    public WebDriverHelper() {
        try {
            //Check Chrome Version
            System.out.println("Chrome version: " + getChromeVersion());
            String chromeVersion = getChromeVersion();
            //If chromedriver exists, do not download
            Path filePath = Paths.get(DOWNLOAD_PATH + "chromedriver.exe");
            if(Files.exists(filePath)){
                System.out.println("Chromedriver exists, no download required.");
            }
            else {
                // Download the latest ChromeDriver
                downloadLatestChromeDriver();
            }

            // Set the path to the ChromeDriver executable
            System.setProperty("webdriver.chrome.driver", DOWNLOAD_PATH + "chromedriver.exe");

            // Create ChromeOptions for additional settings
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized"); // Start the browser maximized
            options.addArguments("--disable-popup-blocking"); // Disable popup blocking

            // Initialize the WebDriver
            driver = new ChromeDriver(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize WebDriver.", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get WebDriver instance
    public WebDriver getDriver() {
        return driver;
    }

    // Method to quit the WebDriver
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Method to download the latest ChromeDriver
    private void downloadLatestChromeDriver() throws Exception {
        String os = getOS();
        DOWNLOAD_URL_TEMPLATE = String.format(DOWNLOAD_URL_TEMPLATE, getChromeVersion(), os, os);
        DOWNLOAD_PATH = String.format(DOWNLOAD_PATH, os);
        System.out.println("Chromedriver does not exist in path");
        System.out.println("Downloading latest ChromeDriver version from " + DOWNLOAD_URL_TEMPLATE + "...");
        String zipFilePath = DOWNLOAD_PATH + "chromedriver_" + os + ".zip";
        String extractedFilePath = DOWNLOAD_PATH + "chromedriver-" + os + "/chromedriver.exe";

        // Download the latest ChromeDriver zip file
        downloadFile(DOWNLOAD_URL_TEMPLATE, zipFilePath);
        // Extract the downloaded zip file
        DOWNLOAD_PATH = String.format(DOWNLOAD_PATH, os);
        unzipFile(zipFilePath, DOWNLOAD_PATH);
        System.out.println("Extracting archive...");
        // Cleanup the zip file
        FileHelper.deleteFile(Paths.get(zipFilePath));
        //Copy webdriver
        FileHelper.copyFile(Paths.get(extractedFilePath), Paths.get(DOWNLOAD_PATH + "/chromedriver.exe"));
        //Delete folder
        FileHelper.deleteFolderRecursively(Paths.get(DOWNLOAD_PATH + "chromedriver-" + os + "/"));
        System.out.println("ChromeDriver updated successfully!");

    }

    // Method to download a file from a URL
    private void downloadFile(String fileURL, String saveAs) throws IOException {
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
        } else {
            throw new IOException("No file to download. Server replied HTTP code: " + responseCode);
        }
    }

    // Method to unzip a file
    private void unzipFile(String zipFilePath, String destDirectory) throws IOException {
        try (java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            java.util.zip.ZipEntry entry;
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
    }

    // Method to determine the OS for the driver binary name
    private String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "win64";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return "linux64";
        } else {
            throw new UnsupportedOperationException("OS not supported: " + os);
        }
    }

    public static String getChromeVersion() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return getChromeVersionWindows();
        } else if (os.contains("mac")) {
            return getChromeVersionMac();
        } else if (os.contains("nix") || os.contains("nux")) {
            return getChromeVersionLinux();
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }
    }

    private static String getChromeVersionWindows() throws Exception {
        String command = "reg query \"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("version")) {
                return line.split("\\s+")[line.split("\\s+").length - 1];
            }
        }
        throw new RuntimeException("Failed to find Chrome version in registry.");
    }

    private static String getChromeVersionMac() throws Exception {
        String command = "/Applications/Google\\ Chrome.app/Contents/MacOS/Google\\ Chrome --version";
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null) {
            return line;
        }
        throw new RuntimeException("Failed to find Chrome version on Mac.");
    }

    private static String getChromeVersionLinux() throws Exception {
        String command = "google-chrome --version";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null) {
            return line;
        }
        throw new RuntimeException("Failed to find Chrome version on Linux.");
    }


    // Main method for testing the WebDriverHelper class
    public static void main(String[] args) {
        WebDriverHelper helper = new WebDriverHelper();
        WebDriver driver = helper.getDriver();

        // Open a website to test
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));

        // Perform additional actions with the driver here

        // Close the browser and quit the driver
        helper.quitDriver();
    }
}