package com.samoukh.mousekeeper.application;

import com.samoukh.mousekeeper.ports.MouseController;
import com.samoukh.mousekeeper.ports.Sleeper;
import com.samoukh.mousekeeper.ports.TimeProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class KeepAwakeUseCaseTest {

    @Test
    void moves_mouse_at_least_once_when_now_is_before_end() throws Exception {
        MouseController mouse = mock(MouseController.class);
        Sleeper sleeper = mock(Sleeper.class);

        LocalDateTime t0 = LocalDateTime.of(2025, 10, 24, 12, 0, 0);
        LocalDateTime tend = t0.plusSeconds(1);

        TimeProvider clock = Mockito.mock(TimeProvider.class);
        // 1er appel -> t0 (boucle entre), 2e appel -> tend (sortie)
        when(clock.now()).thenReturn(t0, tend);

        KeepAwakeUseCase uc = new KeepAwakeUseCase(mouse, clock, sleeper);
        uc.runUntil(tend, 1);

        verify(mouse, atLeastOnce()).moveBy(anyInt(), anyInt());
        verify(sleeper, atLeastOnce()).sleepMillis(anyLong());
    }
}

