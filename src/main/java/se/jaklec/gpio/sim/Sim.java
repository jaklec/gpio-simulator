package se.jaklec.gpio.sim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sim {

    private static final Logger logger = LoggerFactory.getLogger(Sim.class);

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getProperty("sim.gpio.port", "0"));
        int sampleRate = Integer.parseInt(System.getProperty("sim.sample.rate", "50"));

        logger.info("Starting up simulator on port " + port);
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
        }, 200, sampleRate, TimeUnit.MILLISECONDS);
        logger.info("Service started");
    }
}
