package se.jaklec.gpio.sim;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.jaklec.gpio.sim.Environment;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.jaklec.gpio.sim.Environment.Direction.IN;
import static se.jaklec.gpio.sim.Environment.Direction.OUT;

public class EnvironmentTest {

    final Path root = Paths.get("build/sim/gpio");

    @Before
    public void setUp() throws Exception {
        cleanEnvironment(root);
    }

    @After
    public void tearDown() throws Exception {
        cleanEnvironment(root);
    }

    @Test(expected = NullPointerException.class)
    public void baseNotNull() {
        new Environment(5, null, IN);
    }

    @Test
    public void exportFilePath() throws IOException {
        Integer port = 5;
        new Environment(port, root, OUT).build();
        Files.readAllLines(Paths.get(root + "/export")).forEach(p -> assertEquals(port.toString(), p));
    }

    @Test
    public void gpioDirection() throws IOException {
        new Environment(18, root, IN).build();
        new Environment(18, root, OUT).build();
        Files.readAllLines(Paths.get(root + "/gpio18/direction")).forEach(d -> assertEquals(OUT.value, d));
    }

    @Test
    public void gpioValue() {
        new Environment(18, root, IN).build();
        assertTrue("should create value file", Files.exists(Paths.get(root + "/gpio18/value")));
    }

    private void cleanEnvironment(final Path rootDir) throws IOException {
        if (Files.exists(rootDir)) {
            Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}