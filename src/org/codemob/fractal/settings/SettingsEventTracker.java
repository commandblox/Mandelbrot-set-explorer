package org.codemob.fractal.settings;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class SettingsEventTracker implements MouseListener {
    final boolean[] buttons = new boolean[MouseInfo.getNumberOfButtons()];

    private MouseEvent click = null;

    public boolean isClicked(Rectangle2D rect) {
        if (Objects.nonNull(click) && click.getButton() == MouseEvent.BUTTON1 && rect.contains(click.getPoint())) {
            click = null;
            return true;
        }
        return false;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        click = e;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton() - 1] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton() - 1] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
