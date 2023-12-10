package org.codemob.fractal;

import org.codemob.fractal.settings.Button;
import org.codemob.fractal.settings.Settings;
import org.codemob.fractal.settings.TextInput;
import org.codemob.fractal.util.Util;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static double startX = 0;
    public static double startY = 0;
    public static double startZoom = 1;

    public static JFrame window;
    public static App app;

    public static Settings settings;

    public static void main(String[] args) {
        System.out.println("Number of logical processors: " + Util.cores);

        startSettings();
    }

    public static void startSettings() {
        JFrame window = new JFrame("Settings");

        settings = new Settings(window);
        window.add(settings);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }

    public static void startMandel() {
        window = new JFrame("Mandelbrot");

        app = new App(window);
        window.add(app);
        window.addKeyListener(app.eventTracker);
        window.addMouseWheelListener(app.eventTracker);
        window.addMouseListener(app.eventTracker);
        window.addMouseMotionListener(app.eventTracker);
        window.addWindowListener(app);

        window.setUndecorated(true);

        GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[Util.targetScreenDevice].setFullScreenWindow(window);
        window.setVisible(true);
    }

    public static void stopMandel() {
        app.mandelbrot.stop();
        try {
            app.mandelbrot.thread.join();
        } catch (InterruptedException ignore) {}

        window.dispose();
        window = null;
        app = null;

        Button runButton = (Button) settings.settingsElements.get(0);
        TextInput windowSelector = (TextInput) settings.settingsElements.get(1);
        TextInput xLocationSelector = (TextInput) settings.settingsElements.get(2);
        TextInput yLocationSelector = (TextInput) settings.settingsElements.get(3);
        TextInput zoomSelector = (TextInput) settings.settingsElements.get(4);

        runButton.text = "Start";
        runButton.setClickEvent(() -> settings.runEvent(windowSelector, xLocationSelector, yLocationSelector, zoomSelector, runButton));
    }
}
