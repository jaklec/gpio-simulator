package se.jaklec.gpio.sim;

import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Environment {

    public enum Direction {
        IN("in"), OUT("out");

        public final String value;

        private Direction(final String value) {
            this.value = value;
        }
    }

    public final Integer port;
    public final Path root;
    public final Direction direction;
    public final Path exportFile;
    public final Path portDirectoryPath;
    public final Path directionFile;
    public final Path valueFile;

    public Environment(final Integer port, final Path root, final Direction direction) {
        Validate.notNull(root, "Must supply root directory");
        this.port = port;
        this.root = root;
        this.direction = direction;
        this.exportFile = Paths.get(root + "/export");
        this.portDirectoryPath = Paths.get(root + "/gpio" + port);
        this.directionFile = Paths.get(portDirectoryPath + "/direction");
        this.valueFile = Paths.get(portDirectoryPath + "/value");
    }

    public void build() {
        try {
            createResource(exportFile.getParent(), Files::createDirectories);
            createResource(exportFile, Files::createFile);
            Files.write(exportFile, port.toString().getBytes());

            createResource(directionFile.getParent(), Files::createDirectories);
            createResource(directionFile, Files::createFile);
            Files.write(directionFile, direction.value.getBytes());

            createResource(valueFile, Files::createFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createResource(Path p, final FileResource<Path, IOException> block) throws IOException {
        if (Files.notExists(p)) {
            block.accept(p);
        }
    }
}