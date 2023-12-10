package org.codemob.fractal.settings;

import org.codemob.fractal.util.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class TextInput extends SettingsElement {
    public final int minWidth;
    public final int x;
    public final int y;
    public final int textSize;

    public String allowedKeys = "1234567890";

    public int maxLength = 1;

    public String inputText = "";

    private Runnable textUpdateEvent = ()->{};
    public final String title;

    public boolean error = false;

    public void setTextUpdateEvent(Runnable textUpdateEvent) {
        this.textUpdateEvent = textUpdateEvent;
        textUpdateEvent.run();
    }
    public TextInput(int x, int y, int minWidth, int textSize, String title) {
        this.x = x;
        this.y = y;
        this.minWidth = minWidth;
        this.textSize = textSize;
        this.title = title;
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0x000000));
        if (error) {
            g2d.setColor(new Color(0xC23225));
        }
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, textSize));

        String modifiedInputText = inputText + (contained.currentTextInput == this?"|":"");

        Rectangle2D titleBounds = g2d.getFont().getStringBounds(title, g2d.getFontRenderContext());
        g2d.drawString(title, x, (int) (y + titleBounds.getHeight()));

        Rectangle2D textBounds = g2d.getFont().getStringBounds(modifiedInputText + "|", g2d.getFontRenderContext());

        Rectangle textBoxBounds = new Rectangle((int) (x + titleBounds.getWidth()), y, Math.max(minWidth, (int) textBounds.getWidth() + 4), (int) titleBounds.getHeight() * 4/3);
        if (!textBoxBounds.contains(Util.getMouseLocation(contained.window)) & contained.currentTextInput != this) {
            g2d.setColor(new Color(0xA1A1A1));
            if (error) {
                g2d.setColor(new Color(0xA4635D));
            }
        }
        if (textBoxBounds.contains(Util.getMouseLocation(contained.window))) {
            contained.cursor = Cursor.TEXT_CURSOR;
            if (contained.eventTracker.isClicked(textBoxBounds)) {
                contained.currentTextInput = this;
            }

        }

        g2d.drawRoundRect(textBoxBounds.x, textBoxBounds.y, textBoxBounds.width, textBoxBounds.height, 2, 2);
        g2d.drawString(modifiedInputText, textBoxBounds.x + 4, (int) (textBoxBounds.y + textBoxBounds.height * 0.7));
    }

    public void type(KeyEvent e) {
        if (allowedKeys.contains((String.valueOf(e.getKeyChar())))) {
            if (inputText.length() >= maxLength & maxLength != -1) {
                this.inputText = this.inputText.substring(0, maxLength - 1);
            }
            this.inputText += e.getKeyChar();
        } else if (e.getKeyChar() == '\b') {
            this.inputText = this.inputText.substring(0, Math.max(this.inputText.length() - 1, 0));
        }
        textUpdateEvent.run();
    }
}
