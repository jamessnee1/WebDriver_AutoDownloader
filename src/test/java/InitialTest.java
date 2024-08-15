import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class InitialTest extends BaseTest {

    @Test
    public void initialTest(){
        driver.get("www.google.com");
        driver.findElement(By.name("q")).sendKeys("Selenium" + Keys.RETURN);
    }
}
