package uni.trento.cluster.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FileSystemSpecification {
    private int filesToCreate;
    @JsonProperty("fileContents")
    private List<String> fileContent;
}
