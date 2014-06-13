package se.jaklec.gpio.sim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sim {

    public static void main(String[] args) {
        int port = 0;
        Path rootDirectory = Paths.get("/sys/class/gpio");
        Environment environment = new Environment(port, rootDirectory, Environment.Direction.IN);
        environment.build();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            try {
                Random rand = new Random(System.currentTimeMillis());
                Integer randomDigital = new Integer(rand.nextInt(2));
                Files.write(environment.valueFile, randomDigital.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
}
