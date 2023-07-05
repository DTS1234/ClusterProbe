package uni.trento.cluster.probe.stressng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSpecification {
    private Long durationInSeconds;
    private Integer cpuLoad;
    private Integer vmWorkers;
    private Long mBytesRamLoad;
    private Integer IOworkers;
    private Long IOBytes;
    private Boolean isCommand;
    private String command;
}
