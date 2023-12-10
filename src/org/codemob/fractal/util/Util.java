package org.codemob.fractal.util;

import javax.swing.*;
import java.awt.*;

public class Util {
    public static final int cores = Runtime.getRuntime().availableProcessors();
    public static int targetScreenDevice = 0;

    public static Point getMouseLocation(JFrame window) {
        Point windowLocation;
        Point point = MouseInfo.getPointerInfo().getLocation();
        try {
            windowLocation = window.getContentPane().getLocationOnScreen();
        } catch (IllegalComponentStateException e) {
            windowLocation = new Point(0, 0);
        }
        point.translate(-windowLocation.x, -windowLocation.y);
        return point;
    }

    public static double clip(double val, double min, double max) {
        if (min > max) {
            throw new RuntimeException("Minimum cannot be less than maximum!");
        }

        if (val < min) {val = min;}
        if (val > max) {val = max;}
        return val;
    }

    public static int clip(int val, int min, int max) {
        if (min > max) {
            throw new RuntimeException("Minimum cannot be less than maximum!");
        }

        if (val < min) {val = min;}
        if (val > max) {val = max;}
        return val;
    }

    public static float clip(float val, float min, float max) {
        return (float) clip((double) val, min, max);
    }
}
