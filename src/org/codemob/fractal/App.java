package org.codemob.fractal;

import org.codemob.fractal.util.Complex;
import org.codemob.fractal.util.FractalImage;
import org.codemob.fractal.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;

public class App extends JPanel implements WindowListener {

    final JFrame window;

    final Mandelbrot mandelbrot = new Mandelbrot();

    final EventTracker eventTracker;
    public App(JFrame window) {
        this.window = window;
        eventTracker = new EventTracker(mandelbrot, window, this);
        this.setBackground(mandelbrot.getColor(0));
    }

    public void calcZoom() {
        Point mousePos = Util.getMouseLocation(window);
        double mousePointingX = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[0];
        double mousePointingY = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[1];

        mandelbrot.nextImage.zoom *= Math.pow(2, eventTracker.getDeltaZoom() / 3);

        mandelbrot.nextImage.x = mousePointingX;
        mandelbrot.nextImage.y = mousePointingY;

        double newMousePointingX = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[0];
        double newMousePointingY = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[1];

        mandelbrot.nextImage.x += mousePointingX - newMousePointingX;
        mandelbrot.nextImage.y += mousePointingY - newMousePointingY;
    }

    public void drawImage(Graphics2D g2d, FractalImage image) {
        int width = (int) (window.getWidth() / (mandelbrot.nextImage.zoom / image.zoom));
        int height = (int) (window.getHeight() / (mandelbrot.nextImage.zoom / image.zoom));

        int x = (int) ((image.x - mandelbrot.nextImage.x) / mandelbrot.nextImage.zoom * window.getWidth()) + (window.getWidth() - width) / 2;
        int y = (int) ((image.y - mandelbrot.nextImage.y) / mandelbrot.nextImage.zoom * window.getWidth()) + (window.getHeight() - height) / 2;

        g2d.drawImage(image, x, y, width, height , this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (!mandelbrot.isStarted()) {
            mandelbrot.start(window);
            mandelbrot.nextImage.x = Main.startX;
            mandelbrot.nextImage.y = -Main.startY;
            mandelbrot.nextImage.zoom *= Main.startZoom;
        }

        calcZoom();

        super.paintComponent(g2d);

        drawImage(g2d, mandelbrot.currentImage);
        drawImage(g2d, mandelbrot.inProgressImage);

        Point mousePos = Util.getMouseLocation(window);

        g2d.setColor(new Color(0xFFFFFF));
        if (eventTracker.rightClickDown) {
            Complex[] complexes = mandelbrot.getVerbosePixelFrom(mousePos.x, mousePos.y, window).toArray(Complex[]::new);
            int[] xVals = new int[complexes.length];
            int[] yVals = new int[complexes.length];
            int i = 0;
            for (Complex complex : complexes) {
                int[] position = mandelbrot.getFromPosition(complex.real, complex.imaginary, window);
                xVals[i] = position[0];
                yVals[i] = position[1];
                i++;
            }
            g2d.setStroke(new BasicStroke(2));
            g2d.drawPolyline(xVals, yVals, complexes.length);
        }

        drawText(g2d);


        repaint();
    }

    public void drawText(Graphics2D g2d) {
        g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 20));

        String text = "Iterations: %d".formatted(mandelbrot.nextImage.iterations);
        Rectangle2D stringBounds = g2d.getFont().getStringBounds(text, g2d.getFontRenderContext());
        g2d.drawString(text, 10, (int) (stringBounds.getHeight()/2 + 10));


        text = "Zoom: %e".formatted(1/mandelbrot.nextImage.zoom * 6);
        stringBounds = g2d.getFont().getStringBounds(text, g2d.getFontRenderContext());
        g2d.drawString(text, (int) (window.getWidth() - stringBounds.getWidth() - 10), (int) (stringBounds.getHeight()/2 + 10));

        text = "Location: %s (press c to copy)".formatted(getLocationAsString(8));
        g2d.drawString(text, 10, window.getHeight() - 10);

        text = "[left drag]=move   [middle mouse]=iterations   [scroll]=zoom";
        stringBounds = g2d.getFont().getStringBounds(text, g2d.getFontRenderContext());
        g2d.drawString(text, (int) (window.getWidth() - stringBounds.getWidth() - 10), window.getHeight() - 10);
    }

    public String getLocationAsString(int precision) {
        Point mousePos = Util.getMouseLocation(window);
        double mousePointingX = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[0];
        double mousePointingY = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window)[1];
        return ("x=%."+precision+"f y=%."+precision+"f").formatted(mousePointingX, -mousePointingY);
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Main.stopMandel();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
