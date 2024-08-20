package helpers;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestService {

    public RequestSpecification getRequestSpecification() {
        return given().header("Content-Type", "application/json");
    }
}
