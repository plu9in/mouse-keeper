package com.samoukh.mousekeeper.adapters.cli;

import com.samoukh.mousekeeper.adapters.mouse.RobotMouseController;
import com.samoukh.mousekeeper.adapters.time.SystemSleeper;
import com.samoukh.mousekeeper.adapters.time.SystemTimeProvider;
import com.samoukh.mousekeeper.application.KeepAwakeUseCase;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final List<DateTimeFormatter> FMT = Arrays.asList(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    public static void main(String[] args) throws Exception {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now;
        LocalDateTime end = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 18, 20, 0);
        // LocalDateTime end   = now.plusHours(1);

        try {
            if (args.length == 1) {
                // un seul argument = heure de fin
                end = parseFlexible(args[0], now);
            } else if (args.length >= 2) {
                start = parseFlexible(args[0], now);
                end   = parseFlexible(args[1], now);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid date/time: " + e.getMessage());
            System.exit(1);
        }

        if (!start.isBefore(end)) {
            System.err.println("Start must be strictly before end.");
            System.exit(1);
        }

        // attendre jusqu'au start si nécessaire
        LocalDateTime now2 = LocalDateTime.now();
        if (now2.isBefore(start)) {
            Thread.sleep(Duration.between(now2, start).toMillis());
        }

        KeepAwakeUseCase useCase = new KeepAwakeUseCase(
                new RobotMouseController(),
                new SystemTimeProvider(),
                new SystemSleeper()
        );

        useCase.runUntil(end, 30_000);
    }

    private static LocalDateTime parseFlexible(String s, LocalDateTime now) {
        s = s.trim();

        // now+durée (ex: now+90m, now+2h)
        if (s.startsWith("now+")) {
            String dur = s.substring(4);
            Duration d = parseDuration(dur);
            return now.plus(d);
        }

        // Epoch seconds
        if (s.matches("^\\d{10}$")) {
            long epoch = Long.parseLong(s);
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
        }

        // HH:mm[:ss] => aujourd'hui
        if (s.matches("^\\d{1,2}:\\d{2}(:\\d{2})?$")) {
            LocalDate today = LocalDate.now();
            try {
                DateTimeFormatter f = s.length() == 5
                        ? DateTimeFormatter.ofPattern("HH:mm")
                        : DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime t = LocalTime.parse(s, f);
                return LocalDateTime.of(today, t);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(s);
            }
        }

        // Formats complets connus
        for (DateTimeFormatter f : FMT) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignored) { }
        }

        throw new IllegalArgumentException(s);
    }

    private static Duration parseDuration(String s) {
        // Nm, Nh (minutes, heures). On peut étendre si besoin.
        if (s.endsWith("m")) {
            long m = Long.parseLong(s.substring(0, s.length() - 1));
            return Duration.ofMinutes(m);
        }
        if (s.endsWith("h")) {
            long h = Long.parseLong(s.substring(0, s.length() - 1));
            return Duration.ofHours(h);
        }
        // ISO-8601 "PT90M" aussi accepté :
        if (s.startsWith("P")) {
            return Duration.parse(s);
        }
        throw new IllegalArgumentException("Unsupported duration: " + s);
    }
}
