package tests;

import helpers.ConfigHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class UITest extends BaseTest {

    @Test
    public void exampleFirstUITest(){
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));
        driver.findElement(By.name("q")).sendKeys("Selenium" + Keys.RETURN);
        assert(1 == 2);

    }
}
