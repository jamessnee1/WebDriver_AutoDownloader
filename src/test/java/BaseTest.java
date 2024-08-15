import helpers.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class BaseTest {

    protected WebDriver driver;
    private WebDriverHelper webDriverHelper;
    private ConfigHelper configHelper;

    @BeforeClass
    public void setUpClass() {
        // This method can be used for setup that needs to be done once before all tests
        // For instance, initializing resources that are shared across tests
        configHelper = new ConfigHelper();
    }

    @BeforeMethod
    public void setUp() {
        // Initialize WebDriverHelper and WebDriver before each test method
        System.out.println("BeforeMethod - Setting up Webdriver...");
        webDriverHelper = new WebDriverHelper();
        driver = webDriverHelper.getDriver();
        System.out.println("Browser is " + configHelper.getProperty("browser"));

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
