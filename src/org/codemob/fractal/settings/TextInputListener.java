package org.codemob.fractal.settings;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextInputListener implements KeyListener {
    public final Settings window;
    public TextInputListener(Settings window) {
        this.window = window;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        window.currentTextInput.type(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
