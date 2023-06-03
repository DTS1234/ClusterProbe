package uni.trento.cluster.probe;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ClusterProbeApplication.class}
)
public class ControllerIntTest {

    @LocalServerPort
    private int port;

    @Test
    void should_return_payload() {
        given().
            port(port).
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
            .port(port)
            .contentType(ContentType.JSON)
            .body(new TestSpecification(1L, 1, null, null, null, null))
        .when()
            .post("/api/job")
        .then()
            .extract().asString();

        assertThat(result)
            .isEqualTo("Job started");
    }

    @Test
    void should_return_string_error_message() {
        String result = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(new TestSpecification(null, null, null, 1000L, null, null))
            .when()
            .post("/api/job")
            .then()
            .extract().asString();

        assertThat(result)
            .isEqualTo("You need to specify number of workers for vm load first.");
    }
}
