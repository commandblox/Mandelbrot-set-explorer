package org.codemob.fractal;

import org.codemob.fractal.util.Complex;
import org.codemob.fractal.util.FractalImage;
import org.codemob.fractal.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Mandelbrot {

    public FractalImage currentImage;

    public FractalImage inProgressImage;

    public FractalImage nextImage;




    public Thread thread;

    public final double outsideFactor = 2;
    public Mandelbrot() {

    }

    public boolean isStarted() {
        return Objects.nonNull(thread);
    }

    public void start(JFrame window) {
        nextImage = new FractalImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_ARGB);
        nextImage.iterations = 128;
        nextImage.zoom = 6;
        nextImage.x = 0;
        nextImage.y = 0;

        inProgressImage = new FractalImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        inProgressImage.zoom = nextImage.zoom;
        inProgressImage.x = nextImage.x;
        inProgressImage.y = nextImage.y;

        currentImage = new FractalImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
        currentImage.zoom = nextImage.zoom;
        currentImage.x = nextImage.x;
        currentImage.y = nextImage.y;

        thread = new Thread(() -> {
            while (!thread.isInterrupted()) {
                loadImage(window);
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    public Color getColor(double value) {
        Color color;
        if (value == -1) {
            color = new Color(0);
        } else {
            color = Color.getHSBColor((float) (value / 100F) + 0.5F, Util.clip((float) value / 100 + 0.2F, 0F, 1F), 1);
        }
        return color;
    }

    public int getPixelAt(double x, double y) {
        Complex c = new Complex(x, y);
        Complex z = c.copy();
        int value = 0;
        for (int n = 0; n < inProgressImage.iterations; n++) {
            z = z.multiply(z).add(c);
            if (z.abs() > outsideFactor) {
                break;
            }
            value++;
        }
        if (value == inProgressImage.iterations) {
            value = -1;
        }
        return value;
    }

    public ArrayList<Complex> getVerbosePixelAt(double x, double y) {
        Complex c = new Complex(x, y);
        Complex z = c.copy();
        ArrayList<Complex> zValues = new ArrayList<>();
        zValues.add(z);
        for (int n = 0; n < nextImage.iterations; n++) {
            z = z.multiply(z).add(c);
            zValues.add(z);
            if (z.abs() > outsideFactor) {
                break;
            }
        }
        return zValues;
    }

    public Color getColorAt(double x, double y) {
        return getColor(getPixelAt(x, y));
    }

    public double[] getPositionFrom(int intX, int intY, JFrame window) {
        double scaledX = ((double) intX - (double) window.getWidth() / 2) / window.getWidth() * inProgressImage.zoom + inProgressImage.x;
        double scaledY = ((double) intY - (double) window.getHeight() / 2) / window.getWidth() * inProgressImage.zoom + inProgressImage.y;
        return new double[] {scaledX, scaledY};
    }

    public double[] getLivePositionFrom(int intX, int intY, JFrame window) {
        double scaledX = ((double) intX - (double) window.getWidth() / 2) / window.getWidth() * nextImage.zoom + nextImage.x;
        double scaledY = ((double) intY - (double) window.getHeight() / 2) / window.getWidth() * nextImage.zoom + nextImage.y;
        return new double[] {scaledX, scaledY};
    }

    public int[] getFromPosition(double x, double y, JFrame window) {
        double scaledX = (x - nextImage.x) / nextImage.zoom * window.getWidth() + (double) window.getWidth() / 2;
        double scaledY = (y - nextImage.y) / nextImage.zoom * window.getWidth() + (double) window.getHeight() / 2;
        return new int[] {(int) scaledX, (int) scaledY};
    }

    public ArrayList<Complex> getVerbosePixelFrom(int x, int y, JFrame window) {
        double[] scaledValues = getLivePositionFrom(x, y, window);
        double scaledX = scaledValues[0];
        double scaledY = scaledValues[1];
        return getVerbosePixelAt(scaledX, scaledY);
    }

    public Color getColorFromPixel(int x, int y, JFrame window) {
        double[] scaledValues = getPositionFrom(x, y, window);
        double scaledX = scaledValues[0];
        double scaledY = scaledValues[1];

        return getColorAt(scaledX, scaledY);
    }

    public void calcColumn(int x, JFrame window) {
        for (int y = 0; y < window.getHeight(); y++) {
            try {
                inProgressImage.setRGB(x, y, getColorFromPixel(x, y, window).getRGB());
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
    }

    private class ColumnRunnable implements Runnable {
        public final int x;
        public final JFrame window;

        public ColumnRunnable(int x, JFrame window) {
            this.x = x;
            this.window = window;
        }

        @Override
        public void run() {
            calcColumn(x, window);
        }
    }

    public final ThreadGroup threadGroup = new ThreadGroup("Mandelbrot Threads");

    public final int numberOfThreads = Util.cores;


    public void loadImage(JFrame window) {
        inProgressImage = nextImage;
        nextImage = new FractalImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        nextImage.iterations = inProgressImage.iterations;
        nextImage.zoom = inProgressImage.zoom;
        nextImage.x = inProgressImage.x;
        nextImage.y = inProgressImage.y;

        for (int x = 0; x < window.getWidth(); x++) {
            ColumnRunnable columnRunnable = new ColumnRunnable(x, window);
            new Thread(threadGroup, columnRunnable).start();

            while (threadGroup.activeCount() >= numberOfThreads) {}
        }
        currentImage = inProgressImage;
    }

}
