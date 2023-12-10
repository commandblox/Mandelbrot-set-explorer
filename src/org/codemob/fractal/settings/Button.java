package org.codemob.fractal.settings;

import org.codemob.fractal.util.Util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Button extends SettingsElement {
    public final int width;
    public final int height;
    public final int rounding;

    public final int x;
    public final int y;


    public String text;

    public final Color color;

    private Runnable clickEvent;

    public Button(int x, int y, int width, int height, int rounding, Color color, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rounding = rounding;
        this.color = color;
        this.text = text;
    }

    public void setClickEvent(Runnable clickEvent) {
        this.clickEvent = clickEvent;
    }


    @Override
    public void paint(Graphics2D g2d) {
        if (new Rectangle(x, y, width, height).contains(Util.getMouseLocation(contained.window))) {
            g2d.setColor(color.darker());
        } else {
            g2d.setColor(color);
        }
        if (contained.eventTracker.isClicked(new Rectangle(x, y, width, height))) {
            clickEvent.run();
        }

        g2d.fillRoundRect(x, y, width, height, rounding, rounding);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 12));
        Rectangle2D textSize = g2d.getFont().getStringBounds(text, g2d.getFontRenderContext());
        g2d.drawString(text, (int) (x + width / 2 - textSize.getWidth() / 2), (int) (y + height / 2 + textSize.getHeight() / 3));
    }
}
