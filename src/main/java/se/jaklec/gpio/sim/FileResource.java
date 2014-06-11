package se.jaklec.gpio.sim;

import java.io.IOException;

@FunctionalInterface
public interface FileResource<T, X extends IOException> {

    T accept(T instance) throws X;
}