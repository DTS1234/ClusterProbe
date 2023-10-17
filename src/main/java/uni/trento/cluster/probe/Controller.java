package uni.trento.cluster.probe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uni.trento.cluster.probe.fileops.FileOperationsService;
import uni.trento.cluster.probe.fileops.FileSystemSpecification;
import uni.trento.cluster.probe.stressng.TestService;
import uni.trento.cluster.probe.stressng.TestSpecification;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final TestService testService;
    private final FileOperationsService fileOperationsService;

    @PostMapping("/api/load")
    public Map<String, String> payloadTest(@RequestBody Map<String, String> payload) {
        payload.forEach(
            (k, v) -> log.info(String.format("%s = %s", k, v))
        );
        return payload;
    }

    @PostMapping("/api/job")
    public String startJob(@RequestBody TestSpecification specification) {

        log.info("Received job specification: " + specification);
        boolean jobStarted = testService.startJobSpecification(specification);

        if (jobStarted) {
            log.info("Job started for specification: " + specification);
        } else {
            log.error("Job failed to start for specification: " + specification);
        }

        return "Job started";
    }

    @PostMapping("/api/file-operations")
    public String createFiles(@RequestBody FileSystemSpecification spec) {
        fileOperationsService.testFileSystem(spec);
        return String.format("File operations performed based on spec %s", spec);
    }

    @PostMapping("/api/upload-file")
    public String uploadFile(@RequestBody MultipartFile file) {
        log.info("Content type : " + file.getContentType());

        String filename = file.getOriginalFilename();
        log.info(filename);

        return "file uploaded!";
    }

}
