package com.samoukh.mousekeeper.ports;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime now();
}

