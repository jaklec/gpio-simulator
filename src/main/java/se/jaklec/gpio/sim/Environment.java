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
        createResource(path, p -> {
           Files.createDirectories(p);
           Files.createFile(Paths.get(p + "/export"));
        });

        Path portDir = Paths.get(path + "/gpio" + port);
        createResource(portDir, Files::createDirectories);

        createResource(Paths.get(portDir + "/direction"), p -> {
           Files.createFile(p);
           Files.write(p, direction.getBytes());
        });
    }

    private void createResource(Path p, FileResource<Path, IOException> block) throws IOException {
        if (Files.notExists(p)) {
            block.accept(p);
        }
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
