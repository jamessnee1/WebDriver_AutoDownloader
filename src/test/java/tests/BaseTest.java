package tests;

import helpers.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

@Listeners(helpers.ExtentTestListener.class)
public class BaseTest {

    //WebDriver and config
    protected WebDriver driver;
    private WebDriverHelper webDriverHelper;
    private ConfigHelper configHelper;

    protected String env;
    protected String baseUrl;
    protected String chromeDriverPath;
    protected String baseAPIUrl;
    protected String reportLocation;

    @BeforeClass
    public void setUpClass() {
        // This method can be used for setup that needs to be done once before all tests
        // For instance, initializing resources that are shared across tests
        configHelper = new ConfigHelper();
        env = configHelper.getProperty("environment");
        baseUrl = configHelper.getProperty("baseUrl");
        chromeDriverPath = configHelper.getProperty("webdriver.chrome.driver");
        baseAPIUrl = configHelper.getProperty("baseAPIUrl");
        reportLocation = configHelper.getProperty("reportLocation");

    }

    @BeforeMethod
    public void setUp() {
        // Initialize WebDriverHelper and WebDriver before each test method
        System.out.println("BeforeMethod - Setting up Webdriver...");
        webDriverHelper = new WebDriverHelper();
        driver = webDriverHelper.getDriver();
        System.out.println("BaseUrl: " + baseUrl);
        System.out.println("Environment: " + env);
        System.out.println("ChromeDriver path: " + chromeDriverPath);

    }

    @AfterMethod
    public void tearDown() {
        // Quit the WebDriver after each test method
        System.out.println("AfterMethod - TearDown");
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void tearDownClass() {
        // This method can be used for cleanup that needs to be done once after all tests
        // For instance, closing any resources that were initialized in setUpClass
        System.out.println("AfterClass");
    }
}
