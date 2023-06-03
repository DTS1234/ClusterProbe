package uni.trento.cluster.probe;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TestService {

    public boolean startJobSpecification(TestSpecification specification) {
        String[] command = getCommandFromSpec(specification).split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCommandFromSpec(TestSpecification spec) {
        int numCores = Runtime.getRuntime().availableProcessors();

        String cpuLoadCommand = mapCpuLoadToCommand(spec.getCpuLoad());
        String testTimeCommand = mapTestTimeCommand(spec.getDurationInSeconds());
        String vmWorkersCommand = mapVmWorkersCommand(spec.getVmWorkers());
        String vmBytes = mapVmMbCommand(spec.getMBytesRamLoad(), vmWorkersCommand);
        String ioWorkersCommand = mapIOworkersCommand(spec.getIOworkers());
        String ioBytes = mapIoBytes(spec.getIOBytes(), ioWorkersCommand);

        return String.format("stress-ng --cpu %s %s%s%s%s%s%s",
            numCores, cpuLoadCommand, vmWorkersCommand, vmBytes, ioWorkersCommand, ioBytes, testTimeCommand);
    }

    private String mapIoBytes(Long ioBytes, String ioWorkersCommand) {
        if (ioWorkersCommand.isEmpty() && ioBytes != null) {
            throw new IllegalArgumentException("You need to specify number of workers for I/O load first.");
        } else if (ioBytes != null) {
            return String.format("--io-bytes %s ", ioBytes);
        } else if (ioWorkersCommand.isEmpty()) {
            return "";
        } else {
            return "--io-bytes 1024 ";
        }
    }

    private String mapIOworkersCommand(Integer IOworkers) {
        if (IOworkers == null) {
            return "";
        } else {
            return String.format("--io 1 --io-ops %s ", IOworkers);
        }
    }

    private String mapVmMbCommand(Long mBytesRamLoad, String vmWorkersCommand) {
        if (mBytesRamLoad == null && !vmWorkersCommand.isEmpty()) {
            return "--vm-bytes 256M ";
        } else if (mBytesRamLoad != null && vmWorkersCommand.isEmpty()) {
            throw new IllegalArgumentException("You need to specify number of workers for vm load first.");
        } else if (mBytesRamLoad == null) {
            return "";
        } else {
            return String.format("--vm-bytes %sM ", mBytesRamLoad);
        }
    }

    private String mapVmWorkersCommand(Integer vmWorkers) {
        if (vmWorkers == null) {
            return "";
        } else {
            return String.format("--vm %s ", vmWorkers);
        }
    }

    private String mapTestTimeCommand(Long durationInSeconds) {
        if (durationInSeconds == null) {
            return "-t 24h";
        } else {
            return String.format("-t %ss", durationInSeconds);
        }
    }

    private String mapCpuLoadToCommand(Integer cpuLoad) {
        if (cpuLoad == null) {
            return "--cpu-load 5 ";
        }

        if (cpuLoad > 100 || cpuLoad < 0) {
            throw new IllegalArgumentException(
                String.format("Value %s is out of range for cpu-load, allowed: 0 .. 100", cpuLoad)
            );
        }

        return String.format("--cpu-load %s ", cpuLoad);
    }
}
