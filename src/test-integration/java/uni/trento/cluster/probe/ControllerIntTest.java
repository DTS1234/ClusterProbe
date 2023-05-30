package uni.trento.cluster.probe;

import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControllerIntTest {

    @Test
    void should_return_payload() {
        given().
            contentType(ContentType.JSON).
            body(Map.of("key1", "value1")).
            when().
            post("/api/load").
            then().
            body("key1", equalTo("value1"));
    }

    @Test
    void should_return_job_started() {
        String result = given()
            .contentType(ContentType.JSON)
            .body(new TestSpecification(1L, 1)).
            when().
            post("/api/job").
            then().extract().asString();

        assertThat(result)
            .isEqualTo("Job started");
    }
}
