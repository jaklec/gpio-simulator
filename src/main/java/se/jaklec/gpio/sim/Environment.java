package se.jaklec.gpio.sim;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Environment {

    public static final String IN = "in";
    public static final String OUT = "out";

    public final String base;
    public final int port;
    public final String direction;

    private Environment(Builder builder) {
        base = builder.base;
        port = builder.port;
        direction = builder.direction;
        Preconditions.checkNotNull(base, "Must supply a base directory");
        Preconditions.checkNotNull(port, "Must supply a port number");
        Preconditions.checkNotNull(direction, "Must be either 'in' or 'out'");
    }

    public void create() throws IOException {
        Path path = Paths.get(base);
        Path exportPath = Paths.get(path + "/export");
        createResource(exportPath, p -> {
           Files.createDirectories(path);
           return Files.createFile(p);
        });

        Path portDir = Paths.get(path + "/gpio" + port);
        createResource(Paths.get(path + "/gpio" + port), Files::createDirectories);

        Path directionPath = Paths.get(portDir + "/direction");
        createResource(directionPath, p -> {
           Files.createFile(p);
           return Files.write(p, IN.getBytes());
        });
    }

    private Path createResource(Path p, FileResource<Path, IOException> block) throws IOException {
        if (Files.notExists(p)) {
            return block.accept(p);
        }
        return p;
    }

    public static class Builder {

        private String base;
        private int port;
        private String direction;

        private Builder() {}

        public Builder withDirection(final String direction) {
            this.direction = direction;
            return this;
        }

        public Builder forPort(final int port) {
            this.port = port;
            return this;
        }

        public Builder withBase(final String base) {
            this.base = base;
            return this;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Environment build() {
            return new Environment(this);
        }
    }
}
