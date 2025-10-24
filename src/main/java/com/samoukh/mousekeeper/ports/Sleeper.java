package com.samoukh.mousekeeper.ports;

public interface Sleeper {
    void sleepMillis(long millis) throws InterruptedException;
}

