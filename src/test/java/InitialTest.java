import api.CatResponse;
import helpers.ConfigHelper;
import io.opentelemetry.sdk.logs.data.Body;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers;
import org.hamcrest.Matchers;

public class InitialTest extends BaseTest {

    @Test
    public void exampleFirstUITest(){
        driver.get(ConfigHelper.getInstance().getProperty("baseUrl"));
        driver.findElement(By.name("q")).sendKeys("Selenium" + Keys.RETURN);

    }

    @Test
    public void exampleAPITest() {
        Response response = RestAssured.given().when().get(baseAPIUrl);
        response.then().statusCode(200);
        System.out.println("Raw response: " + response.body().asString());
        CatResponse catResponse = response.as(CatResponse.class);
        System.out.println("Cat Fact: " + catResponse.getFact());
        System.out.println("Fact Length: " + catResponse.getLength());

    }
}
