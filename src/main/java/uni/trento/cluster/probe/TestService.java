package uni.trento.cluster.probe;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TestService {

    public boolean startJobSpecification(TestSpecification specification) {
        int numCores = Runtime.getRuntime().availableProcessors();

        String [] command;
        if (specification.getDurationInSeconds() == null) {
            command = String.format("stress-ng --cpu %s --cpu-load %s -t 24h", numCores, specification.getCpuLoad()).split(" ");
        } else {
            command = String.format("stress-ng --cpu %s --cpu-load %s -t %s", numCores, specification.getCpuLoad(), specification.getDurationInSeconds()).split(" ");
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
