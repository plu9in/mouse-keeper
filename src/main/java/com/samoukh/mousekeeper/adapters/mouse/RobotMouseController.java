package com.samoukh.mousekeeper.adapters.mouse;

import com.samoukh.mousekeeper.ports.MouseController;

import java.awt.*;

public class RobotMouseController implements MouseController {
    private final Robot robot;

    public RobotMouseController() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new IllegalStateException("Cannot init AWT Robot", e);
        }
    }

    @Override
    public void moveBy(int dx, int dy) {
        PointerInfo info = MouseInfo.getPointerInfo();
        Point p = info.getLocation();
        robot.mouseMove(p.x + dx, p.y + dy);
    }
}

