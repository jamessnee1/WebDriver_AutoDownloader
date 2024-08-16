package tests;

import api.CatResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class APITest extends BaseTest {

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
