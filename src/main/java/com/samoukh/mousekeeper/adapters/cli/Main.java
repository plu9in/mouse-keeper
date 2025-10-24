package com.samoukh.mousekeeper.adapters.cli;

import com.samoukh.mousekeeper.adapters.mouse.RobotMouseController;
import com.samoukh.mousekeeper.adapters.time.SystemSleeper;
import com.samoukh.mousekeeper.adapters.time.SystemTimeProvider;
import com.samoukh.mousekeeper.application.KeepAwakeUseCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int DEFAULT_FUZZ_MINUTES = 15; // ±15 min autour de l’heure cible

    public static void main(String[] args) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = (args.length >= 1) ? LocalDateTime.parse(args[0], FMT) : now;
        // end: si non fourni -> "≈ 18:00" aujourd'hui (ou demain si dépassé) avec jitter
        LocalDateTime end = (args.length >= 2)
                ? parseEndSmart(args[1], now, DEFAULT_FUZZ_MINUTES)
                : approxAtHour(LocalTime.of(18, 20), now, DEFAULT_FUZZ_MINUTES);

        if (now.isBefore(start)) {
            Thread.sleep(java.time.Duration.between(now, start).toMillis());
        }

        KeepAwakeUseCase useCase = new KeepAwakeUseCase(
                new RobotMouseController(),
                new SystemTimeProvider(),
                new SystemSleeper()
        );

        System.out.println("Valable jusqu'à: " + end.format(FMT));
        useCase.runUntil(end, 30_000);
        System.out.println("Done:" + LocalDateTime.now().format(FMT));
        
    }

    /**
     * Parse une fin "intelligente":
     * - "yyyy-MM-dd HH:mm:ss" -> date-heure exacte (sans jitter)
     * - "HH:mm" ou "HH"       -> prochaine occurrence de cette heure avec jitter ±fuzz
     */
    private static LocalDateTime parseEndSmart(String input, LocalDateTime now, int fuzzMinutes) {
        // 1) essai date-heure complète
        try {
            return LocalDateTime.parse(input, FMT);
        } catch (DateTimeParseException ignored) {}

        // 2) essai "HH:mm"
        try {
            LocalTime t = LocalTime.parse(input);
            return approxAtHour(t, now, 0);
        } catch (DateTimeParseException ignored) {}

        // 3) essai "HH"
        try {
            int hour = Integer.parseInt(input);
            System.out.println("Heure fournie: " + hour);
            if (hour < 0 || hour > 23) throw new NumberFormatException();
            return approxAtHour(LocalTime.of(hour, 0), now, fuzzMinutes);
        } catch (NumberFormatException ignored) {}

        throw new IllegalArgumentException("Unsupported end format: " + input +
                " (use 'yyyy-MM-dd HH:mm:ss', 'HH:mm' or 'HH')");
    }

    /** Prochaine occurrence de l’heure cible + jitter ±fuzzMinutes */
    private static LocalDateTime approxAtHour(LocalTime target, LocalDateTime now, int fuzzMinutes) {
        LocalDate date = now.toLocalDate();
        LocalDateTime candidate = LocalDateTime.of(date, target);
        if (!now.isBefore(candidate)) {
            System.out.println("L'heure de fin cible est déjà passée aujourd'hui.");
            System.exit(0);
        }
        int delta = (fuzzMinutes <= 0) ? 0 : ThreadLocalRandom.current().nextInt(-fuzzMinutes, fuzzMinutes + 1);
        return candidate.plusMinutes(delta);
    }    
}

