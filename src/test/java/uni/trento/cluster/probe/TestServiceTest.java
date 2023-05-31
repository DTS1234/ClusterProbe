package uni.trento.cluster.probe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestServiceTest {

    private final TestService testService = new TestService();

    @ParameterizedTest
    @MethodSource("should_create_job_from_specification")
    void should_create_job_from_specification(Long durationInSeconds, Integer cpuLoad, Integer vmWorkers, Long mBytes, String expected) {
        // given
        TestSpecification spec = new TestSpecification(
            durationInSeconds,
            cpuLoad,
            vmWorkers,
            mBytes
        );

        // when
        String result = testService.getCommandFromSpec(spec);

        // then
        assertThat(result).isEqualTo(
            expected
        );
    }

    private static List<Arguments> should_create_job_from_specification() {
        return List.of(
            arguments(10L, 10, null, null, "stress-ng --cpu 8 --cpu-load 10 -t 10s"),
            arguments(null, 15, null, null, "stress-ng --cpu 8 --cpu-load 15 -t 24h"),
            arguments(null, 15, null, null, "stress-ng --cpu 8 --cpu-load 15 -t 24h"),
            arguments(null, 16, 2, null, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 256M -t 24h"),
            arguments(null, 16, 2, 2000L, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 2000M -t 24h")
        );
    }

}
