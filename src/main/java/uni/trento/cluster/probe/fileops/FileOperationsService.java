package uni.trento.cluster.probe.fileops;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class FileOperationsService {

    public static final String TEST_FILES = System.getProperty("user.dir") + "/test";

    public void testFileSystem(FileSystemSpecification specification) {

        List<String> paths = new ArrayList<>();
        createTestDir();
        createFiles(specification, paths);
        writeToFiles(specification, paths);
        deleteFiles();

    }

    private void deleteFiles() {
        boolean deleted = deleteDirectory(new File(TEST_FILES));
        if (deleted) {
            log.info("Directory deleted successfully");
        } else {
            log.info("Failed to delete directory");
        }
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return directory.delete();
    }

    private static void writeToFiles(FileSystemSpecification specification, List<String> paths) {
        List<String> fileContents = specification.getFileContent();

        if (fileContents == null || fileContents.isEmpty()) {
            log.info("No file content specified, only file creation is executed!");
            return;
        }

        if (fileContents.size() != paths.size() && fileContents.size() != 1) {
            log.error("FileContents should be size of one or size of filesToCreate! Remove it if you don't want to write to files.");
            deleteDirectory(new File(TEST_FILES));
            throw new IllegalStateException(
                "FileContents should be size of one or size of filesToCreate!, no value will be written. Remove it if you don't want to write to files.");
        }

        if (fileContents.size() == paths.size()) {
            for (int i = 0; i < paths.size(); i++) {
                byte[] content = fileContents.get(i).getBytes();
                Path filePath = Path.of(paths.get(i));
                writeToFile(specification, content, filePath);
            }
        } else {
            byte[] content = fileContents.get(0).getBytes();
            for (String path : paths) {
                Path filePath = Path.of(path);
                writeToFile(specification, content, filePath);
            }
        }

    }

    private void createFiles(FileSystemSpecification specification, List<String> paths) {
        for (int i = 0; i < specification.getFilesToCreate(); i++) {
            try {
                paths.add(createRandomFile(TEST_FILES));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create file for test with spec: " + specification);
            }
        }
        log.info("Successfully created files for test: " + specification);
    }

    private static void writeToFile(FileSystemSpecification specification, byte[] content, Path filePath) {
        try {
            Files.write(filePath, content, StandardOpenOption.WRITE);
            log.info("Successfully wrote to a file: " + filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write to a file for test with spec: " + specification);
        }
    }

    private void createTestDir() {
        File dir = new File(TEST_FILES);
        boolean created;

        try {
            created = dir.mkdir();
        } catch (SecurityException e) {
            e.printStackTrace();
            created = false;
        }

        if (created) {
            log.info("Successfully create test dir: " + TEST_FILES);
        } else {
            throw new IllegalStateException("Failed to create test dir for file operations!");
        }
    }

    public String createRandomFile(String basePath) throws IOException {
        Random random = new Random();
        String fileName = "file_" + random.nextInt(1000000);  // Generate a random file name
        String filePath = basePath + File.separator + fileName;

        Path path = Paths.get(filePath);
        return Files.createFile(path).toString();
    }
}
