gpio-simulator
==============

I created this simple simulator to test rpi gpio apps without the need for a real RPI.
It's currently not configurable, which means it must run as root on a regular setup - just like on the RPI. 

Note that even though it is dead simple, it still requires Java 8 to run.

## Running
The simulator should run with root privileges on a typical set up. It defaults to port 0 and with a rate of 50ms.
```bash
$ gradle assemble
$ cd build/libs
$ sudo java -jar gpio-simulator-0.1-SNAPSHOT.jar
```

### Configure port and rate
```bash
# Simulating GPIO port 18 with new random signal every 150 millisecond
$ sudo java -jar gpio-simulator-0.1-SNAPSHOT.jar -Dsim.gpio.port=18 -Dsim.sample.rate=150
```
