package tests;

import helpers.ConfigHelper;
import model.GoogleHomePage;
import model.GoogleResultsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class UITest extends BaseTest {

    private GoogleHomePage googleHomePage;
    private GoogleResultsPage googleResultsPage;

    @Test
    public void exampleFirstUITest(){
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));

        //Homepage
        googleHomePage = new GoogleHomePage(driver);
        googleHomePage.enterSearchText("Selenium" + Keys.RETURN);

    }
}
