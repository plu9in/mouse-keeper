package com.samoukh.mousekeeper.adapters.time;

import com.samoukh.mousekeeper.ports.TimeProvider;

import java.time.LocalDateTime;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

