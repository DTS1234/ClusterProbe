package uni.trento.cluster.probe;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import uni.trento.cluster.probe.fileops.FileSystemSpecification;
import uni.trento.cluster.probe.stressng.TestSpecification;

import java.util.Arrays;
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
            .body(new TestSpecification(1L, 1, null, null, null, null, false, null))
        .when()
            .post("/api/job")
        .then()
            .extract().asString();

        assertThat(result)
            .isEqualTo("Job started");
    }

    @Test
    void should_return_string_error_message_for_job_test() {
        String result = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(new TestSpecification(null, null, null, 1000L, null, null, false, null))
            .when()
            .post("/api/job")
            .then()
            .statusCode(400)
            .extract().asString();

        assertThat(result)
            .isEqualTo("You need to specify number of workers for vm load first.");
    }

    @Test
    void should_return_string_error_message_for_file_ops_test_wrong_fileContents() {
        String result = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(new FileSystemSpecification(10, Arrays.asList("1", "2")))
            .when()
            .post("/api/file-operations")
            .then()
            .statusCode(400)
            .extract().asString();

        assertThat(result)
            .isEqualTo("FileContents should be size of one or size of filesToCreate!, no value will be written. Remove it if you don't want to write to files.");
    }
}
