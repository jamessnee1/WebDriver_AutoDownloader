package tests;

import helpers.*;
import listener.ExtentTestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

@Listeners(ExtentTestListener.class)
public class BaseTest {

    //WebDriver and config
    protected WebDriver driver;
    private WebDriverHelper webDriverHelper;
    private ConfigHelper configHelper;
    private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    protected String baseAPIUrl;
    protected String baseUrl;

    @BeforeClass
    public void setUpClass() {
        // This method can be used for setup that needs to be done once before all tests
        // For instance, initializing resources that are shared across tests
        configHelper = new ConfigHelper();
        baseAPIUrl = configHelper.getProperty("baseAPIUrl");
        baseUrl = configHelper.getProperty("baseUrl");
        System.out.println("BaseUrl: " + configHelper.getProperty("baseUrl"));
        System.out.println("BaseAPIUrl: " + configHelper.getProperty("baseAPIUrl"));
        System.out.println("Environment: " + configHelper.getProperty("environment"));
        System.out.println("Browser: " + configHelper.getProperty("browser"));
        System.out.println("Report Location: " + configHelper.getProperty("reportLocation"));

        // Initialize WebDriverHelper and WebDriver before each test run
        System.out.println("BeforeMethod - Setting up Webdriver...");
        webDriverHelper = new WebDriverHelper(configHelper.getProperty("browser"));
        driver = webDriverHelper.getDriver();
        threadDriver.set(driver);

    }

    @BeforeMethod
    public void setUp() {

    }

    @AfterMethod
    public void tearDown() {
        // Quit the WebDriver after each test method
        System.out.println("AfterMethod - TearDown");
        if (driver != null) {
            driver.quit();
        }
        threadDriver.remove();
    }

    @AfterClass
    public void tearDownClass() {
        // This method can be used for cleanup that needs to be done once after all tests
        // For instance, closing any resources that were initialized in setUpClass
        System.out.println("AfterClass");
    }

    // Provide static method to get the WebDriver instance
    public static WebDriver getDriver() {
        return threadDriver.get();
    }
}
