package org.codemob.fractal.settings;

import java.awt.*;

public abstract class SettingsElement {
    public Settings contained;
    public abstract void paint(Graphics2D g2d);
}
