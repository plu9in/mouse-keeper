package com.samoukh.mousekeeper.adapters.cli;

import com.samoukh.mousekeeper.adapters.mouse.RobotMouseController;
import com.samoukh.mousekeeper.adapters.time.SystemSleeper;
import com.samoukh.mousekeeper.adapters.time.SystemTimeProvider;
import com.samoukh.mousekeeper.application.KeepAwakeUseCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = (args.length >= 1) ? LocalDateTime.parse(args[0], FMT) : now;
        LocalDateTime end   = (args.length >= 2) ? LocalDateTime.parse(args[1], FMT) : now.plusHours(1);

        if (now.isBefore(start)) {
            Thread.sleep(java.time.Duration.between(now, start).toMillis());
        }

        KeepAwakeUseCase useCase = new KeepAwakeUseCase(
                new RobotMouseController(),
                new SystemTimeProvider(),
                new SystemSleeper()
        );
        useCase.runUntil(end, 30_000);
    }
}

