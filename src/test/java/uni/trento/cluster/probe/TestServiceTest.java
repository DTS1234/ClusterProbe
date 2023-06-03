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
    void should_create_job_from_specification(
        Long durationInSeconds, Integer cpuLoad,
        Integer vmWorkers, Long mBytes,
        Integer IOWorkers, Long IOBytes,
        String expected
    ) {
        // given
        TestSpecification spec = new TestSpecification(
            durationInSeconds,
            cpuLoad,
            vmWorkers,
            mBytes,
            IOWorkers,
            IOBytes
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
            arguments(10L, 10, null, null, null, null, "stress-ng --cpu 8 --cpu-load 10 -t 10s"),
            arguments(null, 15, null, null, null, null, "stress-ng --cpu 8 --cpu-load 15 -t 24h"),
            arguments(null, 15, null, null, null, null, "stress-ng --cpu 8 --cpu-load 15 -t 24h"),
            arguments(null, 16, 2, null, null, null, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 256M -t 24h"),
            arguments(null, 16, 2, 2000L, null, null, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 2000M -t 24h"),
            arguments(null, 16, 2, 2000L, 2, 4096L, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 2000M --io 1 --io-ops 2 --io-bytes 4096 -t 24h"),
            arguments(null, 16, 2, 2000L, 4, null, "stress-ng --cpu 8 --cpu-load 16 --vm 2 --vm-bytes 2000M --io 1 --io-ops 4 --io-bytes 1024 -t 24h"),
            arguments(3600L, null, null, null, 3, 2048L, "stress-ng --cpu 8 --cpu-load 5 --io 1 --io-ops 3 --io-bytes 2048 -t 3600s")
        );
    }

    @Test
    void should_throw_if_vm_bytes_present_without_workers() {
        Assertions.assertThatThrownBy(() -> {
                testService.getCommandFromSpec(new TestSpecification(
                    null, null, null, 1L, null, null
                ));
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You need to specify number of workers for vm load first.");
    }

    @Test
    void should_throw_if_io_bytes_present_without_workers() {
        Assertions.assertThatThrownBy(() -> {
                testService.getCommandFromSpec(new TestSpecification(
                    null, null, null, null, null, 100L
                ));
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("You need to specify number of workers for I/O load first.");
    }
}
