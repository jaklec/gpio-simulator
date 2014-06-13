gpio-simulator
==============

I created this simple simulator to test rpi gpio apps without the need for a real RPI.
It's currently not configurable, which means it must run as root on a regular setup - just like on the RPI. 

Note that even though it is dead simple, it still requires Java 8 to run.

$> gradle assemble

$> cd build/libs

$> sudo java -jar gpio-simulator-0.1-SNAPSHOT.jar
