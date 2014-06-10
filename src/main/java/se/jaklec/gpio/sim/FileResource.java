package se.jaklec.gpio.sim;


import java.io.IOException;

@FunctionalInterface
public interface FileResource<T, X extends IOException> {

    void accept(T instance) throws X;
}
