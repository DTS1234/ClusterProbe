package uni.trento.cluster.probe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSpecification {
    private Long durationInSeconds;
    private Integer cpuLoad;
}
