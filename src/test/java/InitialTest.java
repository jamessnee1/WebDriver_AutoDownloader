import helpers.ConfigHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class InitialTest extends BaseTest {

    @Test
    public void firstTest(){
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));
        driver.findElement(By.name("q")).sendKeys("Selenium" + Keys.RETURN);

    }
}
