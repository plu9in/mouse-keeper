package com.samoukh.mousekeeper.adapters.time;

import com.samoukh.mousekeeper.ports.Sleeper;

public class SystemSleeper implements Sleeper {
    @Override
    public void sleepMillis(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}

