package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebDriverHelper {

    protected WebDriver driver;
    private static final String CHROME_DRIVER_URL = "https://storage.googleapis.com/chrome-for-testing-public/";
    //https://github.com/mozilla/geckodriver/releases/download/v0.35.0/geckodriver-v0.35.0-win32.zip
    private static final String GECKO_DRIVER_URL = "https://github.com/mozilla/geckodriver/releases/tag/";
    private static String GECKO_DOWNLOAD_URL_TEMPLATE = GECKO_DRIVER_URL + "%s/geckodriver-%s-%s.zip";
    private static String CHROMEDRIVER_DOWNLOAD_URL_TEMPLATE = CHROME_DRIVER_URL + "%s/" + "%s/chromedriver-%s.zip";
    private static String WEBDRIVER_DOWNLOAD_PATH = System.getProperty("user.dir") + ConfigHelper.getInstance().getProperty("webdriver.download.path");


    // Constructor to initialize the WebDriver
    public WebDriverHelper(String browserType) {
        try {
            if(browserType.equalsIgnoreCase("chrome")) {
                initializeChromeDriver();
            }
            else if(browserType.equalsIgnoreCase("firefox")){
                initializeFirefoxDriver();
            }
            else {
                //Any other browser here
            }

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

    //Initialize Chrome Driver
    private void initializeChromeDriver() throws Exception {
        //Check Chrome Version
        System.out.println("Chrome version: " + getChromeVersion());
        String chromeVersion = getChromeVersion();
        //If chromedriver exists, do not download
        Path filePath = Paths.get(WEBDRIVER_DOWNLOAD_PATH + "chromedriver.exe");
        if(Files.exists(filePath)){
            System.out.println("Chromedriver exists, no download required.");
        }
        else {
            // Download the latest ChromeDriver
            downloadLatestChromeDriver();
        }

        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", WEBDRIVER_DOWNLOAD_PATH + "chromedriver.exe");

        // Create ChromeOptions for additional settings
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-popup-blocking"); // Disable popup blocking

        // Initialize the WebDriver
        driver = new ChromeDriver(options);
    }

    //Initialize Firefox Driver
    private void initializeFirefoxDriver() throws Exception {
        // Check Firefox Version
        System.out.println("Firefox version: " + getFirefoxVersion());
        String firefoxVersion = getFirefoxVersion();
        Path filePath = Paths.get(WEBDRIVER_DOWNLOAD_PATH + "geckodriver.exe");
        if (Files.exists(filePath)) {
            System.out.println("Geckodriver exists, no download required.");
        } else {
            // Download the latest GeckoDriver
            downloadLatestGeckoDriver();
        }

        // Set the path to the GeckoDriver executable
        System.setProperty("webdriver.gecko.driver", WEBDRIVER_DOWNLOAD_PATH + "geckodriver.exe");

        // Create FirefoxOptions for additional settings
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-popup-blocking"); // Disable popup blocking

        // Initialize the WebDriver
        driver = new FirefoxDriver(options);
    }

    // Method to download the latest ChromeDriver
    private void downloadLatestChromeDriver() throws Exception {
        String os = getOS();
        CHROMEDRIVER_DOWNLOAD_URL_TEMPLATE = String.format(CHROMEDRIVER_DOWNLOAD_URL_TEMPLATE, getChromeVersion(), os, os);
        WEBDRIVER_DOWNLOAD_PATH = String.format(WEBDRIVER_DOWNLOAD_PATH, os);
        System.out.println("Chromedriver does not exist in path");
        System.out.println("Downloading latest ChromeDriver version from " + CHROMEDRIVER_DOWNLOAD_URL_TEMPLATE + "...");
        String zipFilePath = WEBDRIVER_DOWNLOAD_PATH + "chromedriver_" + os + ".zip";
        String extractedFilePath = WEBDRIVER_DOWNLOAD_PATH + "chromedriver-" + os + "/chromedriver.exe";

        // Download the latest ChromeDriver zip file
        FileHelper.downloadFile(CHROMEDRIVER_DOWNLOAD_URL_TEMPLATE, zipFilePath);
        // Extract the downloaded zip file
        WEBDRIVER_DOWNLOAD_PATH = String.format(WEBDRIVER_DOWNLOAD_PATH, os);
        FileHelper.unzipFile(zipFilePath, WEBDRIVER_DOWNLOAD_PATH);
        // Cleanup the zip file
        FileHelper.deleteFile(Paths.get(zipFilePath));
        //Copy webdriver
        FileHelper.copyFile(Paths.get(extractedFilePath), Paths.get(WEBDRIVER_DOWNLOAD_PATH + "/chromedriver.exe"));
        //Delete folder
        FileHelper.deleteFolderRecursively(Paths.get(WEBDRIVER_DOWNLOAD_PATH + "chromedriver-" + os + "/"));
        System.out.println("ChromeDriver updated successfully!");

    }

    // Method to download the latest GeckoDriver
    private void downloadLatestGeckoDriver() throws Exception {
        String os = getOS();
        GECKO_DOWNLOAD_URL_TEMPLATE = String.format(GECKO_DOWNLOAD_URL_TEMPLATE, getFirefoxVersion(), getFirefoxVersion(), os);
        System.out.println("Geckodriver does not exist in path");
        System.out.println("Downloading latest GeckoDriver version from " + GECKO_DOWNLOAD_URL_TEMPLATE + "...");
        String zipFilePath = WEBDRIVER_DOWNLOAD_PATH + "geckodriver-" + getFirefoxVersion() + "-" + os + ".zip";
        String extractedFilePath = WEBDRIVER_DOWNLOAD_PATH + "geckodriver-" + os + "/geckodriver.exe";
        // Download the latest GeckoDriver zip file
        FileHelper.downloadFile(GECKO_DOWNLOAD_URL_TEMPLATE, zipFilePath);
        // Extract the downloaded zip file
        FileHelper.unzipFile(zipFilePath, WEBDRIVER_DOWNLOAD_PATH);
        // Cleanup the zip file
        //FileHelper.deleteFile(Paths.get(zipFilePath));
        System.out.println("GeckoDriver updated successfully!");
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

    public static String getFirefoxVersion() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return getFirefoxVersionWindows();
        } else if (os.contains("mac")) {
            return getFirefoxVersionMac();
        } else if (os.contains("nix") || os.contains("nux")) {
            return getFirefoxVersionLinux();
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + os);
        }
    }

    private static String getFirefoxVersionWindows() throws Exception {
        String command = "reg query \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Mozilla\\Mozilla Firefox\" /v CurrentVersion";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("CurrentVersion")) {
                // Extract version number from the line - 3rd substring
                String[] parts = line.split("\\s+");
                String version = parts[3].trim();

                // Define the regex pattern for the version number
                String regex = "^([0-9]+)\\.([0-9]+)\\.([0-9]+)$";

                // Compile the pattern
                Pattern pattern = Pattern.compile(regex);

                // Create a matcher for the version string
                Matcher matcher = pattern.matcher(version);

                String majorVersion = "";
                String subVersion = "";

                if(matcher.matches()){
                    //Extract version components
                    majorVersion = matcher.group(1);
                    subVersion = matcher.group(3);
                }

                //Remove the first character and the last character
                return "v0." + majorVersion.substring(1) + "." + subVersion;
            }
        }
        throw new RuntimeException("Failed to find Firefox version in registry.");
    }


    private static String getFirefoxVersionMac() throws Exception {
        String command = "/Applications/Firefox.app/Contents/MacOS/firefox --version";
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null) {
            return line.split("\\s+")[line.split("\\s+").length - 1];
        }
        throw new RuntimeException("Failed to find Firefox version on Mac.");
    }

    private static String getFirefoxVersionLinux() throws Exception {
        String command = "firefox --version";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null) {
            return line.split("\\s+")[line.split("\\s+").length - 1];
        }
        throw new RuntimeException("Failed to find Firefox version on Linux.");
    }


    // Main method for testing the WebDriverHelper class
    public static void main(String[] args) {
        WebDriverHelper helper = new WebDriverHelper(ConfigHelper.getInstance().getProperty("browser"));
        WebDriver driver = helper.getDriver();

        // Open a website to test
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));

        // Perform additional actions with the driver here

        // Close the browser and quit the driver
        helper.quitDriver();
    }
}