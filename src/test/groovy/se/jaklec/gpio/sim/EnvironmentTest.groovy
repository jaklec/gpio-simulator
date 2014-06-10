package se.jaklec.gpio.sim

import org.junit.After
import org.junit.Before
import org.junit.Test

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class EnvironmentTest {

    final String gpioDir = "build/sim/gpio"

    @Before
    void setUp() {
        cleanTestArea()
    }

    @After
    void tearDownClass() {
        cleanTestArea()
    }

    @Test
    void "build base gpio path" () {
        def env = Environment.Builder.newBuilder().forPort(13).
                withBase(gpioDir).
                withDirection(Environment.IN).
                build()
        assertExists gpioDir, env
    }

    @Test
    void "create export File" () {
        def env = Environment.Builder.newBuilder().forPort(13).
                withBase(gpioDir).
                withDirection(Environment.IN).
                build()
        assertExists gpioDir + "/export", env
    }

    @Test
    void "create port dir" () {
        def env = Environment.Builder.newBuilder().forPort(13).
                withBase(gpioDir).
                withDirection(Environment.IN).
                build()
        assertExists gpioDir + "/gpio13", env
    }

    @Test
    void "set direction to in" () {
        Environment.Builder.newBuilder().forPort(13).
                withBase(gpioDir).
                withDirection(Environment.IN).
                build().create()
        Files.readAllLines(Paths.get(gpioDir + "/gpio13/direction")).each { s -> assert "in" == s }
    }

    @Test
    void "set direction to out" () {
        Environment.Builder.newBuilder().forPort(13).
                withBase(gpioDir).
                withDirection(Environment.OUT).
                build().create()
        Files.readAllLines(Paths.get(gpioDir + "/gpio13/direction")).each { s -> assert "out" == s }
    }

    def void assertExists(final String file, final Environment env) {
        env.create()
        def path = Paths.get file
        assert Files.exists(path)
    }

    def cleanTestArea() {
        Path area = Paths.get(gpioDir)
        if (Files.exists(area)) {
            Files.walkFileTree(area, new SimpleFileVisitor<Path>(){
                @Override
                FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file)
                    FileVisitResult.CONTINUE
                }

                @Override
                FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    Files.delete(dir)
                    FileVisitResult.CONTINUE
                }
            })
        }
    }
}
