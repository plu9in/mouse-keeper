package com.samoukh.mousekeeper.application;

import com.samoukh.mousekeeper.ports.MouseController;
import com.samoukh.mousekeeper.ports.Sleeper;
import com.samoukh.mousekeeper.ports.TimeProvider;

import java.time.LocalDateTime;

public class KeepAwakeUseCase {
    private final MouseController mouse;
    private final TimeProvider clock;
    private final Sleeper sleeper;

    public KeepAwakeUseCase(MouseController mouse, TimeProvider clock, Sleeper sleeper) {
        this.mouse = mouse;
        this.clock = clock;
        this.sleeper = sleeper;
    }

    public void runUntil(LocalDateTime endExclusive, long periodMillis) throws InterruptedException {
        while (clock.now().isBefore(endExclusive)) {
            mouse.moveBy(1, 1);
            mouse.moveBy(-1, -1);
            sleeper.sleepMillis(periodMillis);
        }
    }
}

