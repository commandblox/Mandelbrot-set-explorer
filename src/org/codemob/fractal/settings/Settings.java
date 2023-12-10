package org.codemob.fractal.settings;

import org.codemob.fractal.Main;
import org.codemob.fractal.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Settings extends JPanel implements WindowListener {
    public int cursor;
    public final JFrame window;

    public final TextInputListener textInputListener = new TextInputListener(this);

    public TextInput currentTextInput;

    public final ArrayList<SettingsElement> settingsElements = new ArrayList<>();

    public final SettingsEventTracker eventTracker = new SettingsEventTracker();

    public void addSettingsElement(SettingsElement element) {
        settingsElements.add(element);
        element.contained = this;
    }

    public void runEvent(TextInput windowSelector, TextInput xLocationSelector, TextInput yLocationSelector, TextInput zoomSelector, Button runButton) {
        boolean error = false;
        for (SettingsElement settingsElement : settingsElements) {
            if (settingsElement instanceof TextInput && ((TextInput) settingsElement).error) {
                error = true;
                break;
            }
        }
        if (!error) {
            Util.targetScreenDevice = Integer.parseInt(windowSelector.inputText) - 1;
            Main.startX = Double.parseDouble(xLocationSelector.inputText);
            Main.startY = Double.parseDouble(yLocationSelector.inputText);
            Main.startZoom = Double.parseDouble(zoomSelector.inputText);
            Main.startMandel();
            runButton.text = "Stop";
            runButton.setClickEvent(() -> {
                Main.stopMandel();
            });
        }
    }
    public Settings(JFrame window) {
        this.window = window;
        this.addMouseListener(eventTracker);

        this.setPreferredSize(new Dimension(648, 288));
        Dimension size = this.getPreferredSize();

        window.addKeyListener(textInputListener);
        window.addWindowListener(this);

        int numberOfMonitors = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;

        TextInput windowSelector = new TextInput(24, 24, 24, 12, "Input window number (1-"+numberOfMonitors+"): ");
        windowSelector.setTextUpdateEvent(() -> {
            if (windowSelector.inputText.isEmpty()) {
                int indexOfWindow = List.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()).indexOf(window.getGraphicsConfiguration().getDevice());
                if (indexOfWindow == 0) {
                    windowSelector.inputText = "2";
                } else {
                    windowSelector.inputText = "1";
                }
            }
            windowSelector.inputText = String.valueOf(Util.clip(Integer.parseInt(windowSelector.inputText), 1, numberOfMonitors));
        });

        TextInput xLocationSelector = new TextInput(24, 48,  128, 12, "X position: ");
        TextInput yLocationSelector = new TextInput(24, 72,  128, 12, "Y position: ");
        TextInput zoomSelector      = new TextInput(24, 96,  128, 12, "Zoom (Scientific notation is allowed eg. 1.78e-24): ");

        xLocationSelector.setTextUpdateEvent(createLocationRunnable(xLocationSelector, "0"));
        yLocationSelector.setTextUpdateEvent(createLocationRunnable(yLocationSelector, "0"));
        zoomSelector.setTextUpdateEvent(createLocationRunnable(zoomSelector, "1"));

        xLocationSelector.maxLength   = -1;
        yLocationSelector.maxLength   = -1;
        zoomSelector.maxLength        = -1;
        xLocationSelector.allowedKeys = "-.1234567890eE";
        yLocationSelector.allowedKeys = "-.1234567890eE";
        zoomSelector.allowedKeys      = "-.1234567890eE";

        Button runButton = new Button(size.width - 96, size.height - 48, 64, 24, 16, Color.LIGHT_GRAY, "Start");



        runButton.setClickEvent(() -> runEvent(windowSelector, xLocationSelector, yLocationSelector, zoomSelector, runButton));


        addSettingsElement(runButton);
        addSettingsElement(windowSelector);
        addSettingsElement(xLocationSelector);
        addSettingsElement(yLocationSelector);
        addSettingsElement(zoomSelector);
    }

    public Runnable createLocationRunnable(TextInput locationSelector, String defaultValue) {
        return () -> {
            if (locationSelector.inputText.isEmpty()) {
                locationSelector.inputText = defaultValue;
            } else if (locationSelector.inputText.toCharArray()[0] == '0') {
                locationSelector.inputText = locationSelector.inputText.substring(1);
            }
            try {
                Double.parseDouble(locationSelector.inputText);
                locationSelector.error = false;

            } catch (NumberFormatException e) {
                locationSelector.error = true;
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g);

        cursor = Cursor.DEFAULT_CURSOR;

        for (SettingsElement element : settingsElements) {
            element.paint(g2d);
        }



        setCursor(Cursor.getPredefinedCursor(cursor));

        repaint();
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (Objects.nonNull(Main.app)) {
            Main.stopMandel();
        }
        System.exit(0);
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
