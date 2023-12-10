package org.codemob.fractal;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

public class EventTracker implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {


    private double deltaZoom = 0;

    public boolean leftClickDown = false;
    public boolean rightClickDown = false;
    public boolean middleClickDown = false;

    public boolean shiftDown = false;

    public Point mousePos = null;

    public final Mandelbrot mandelbrot;

    public final JFrame window;

    public final App app;

    public EventTracker(Mandelbrot mandelbrot, JFrame window, App app) {
        this.mandelbrot = mandelbrot;
        this.window = window;
        this.app = app;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            System.out.println("copying");
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(app.getLocationAsString(24)), null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Main.stopMandel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftDown = false;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (shiftDown) {
            mandelbrot.nextImage.iterations += e.getScrollAmount() / e.getUnitsToScroll();
            if (mandelbrot.nextImage.iterations < 1) {
                mandelbrot.nextImage.iterations = 1;
            }
        } else {
            deltaZoom += e.getPreciseWheelRotation();
        }
    }

    public double getDeltaZoom() {
        double returnVal = deltaZoom;
        deltaZoom = 0;
        return returnVal;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickDown = true;
            window.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = true;
            window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middleClickDown = true;
            window.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickDown = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightClickDown = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middleClickDown = false;
        }
        window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {
        Point newPos = e.getPoint();

        if (leftClickDown) {
            double[] updatedNewPos = mandelbrot.getLivePositionFrom(newPos.x, newPos.y, window);
            double[] updatedOriginalPos = mandelbrot.getLivePositionFrom(mousePos.x, mousePos.y, window);
            double xDiff = updatedOriginalPos[0] - updatedNewPos[0];
            double yDiff = updatedOriginalPos[1] - updatedNewPos[1];

            mandelbrot.nextImage.x += xDiff;
            mandelbrot.nextImage.y += yDiff;
        } else if (middleClickDown) {
            int yDiff = newPos.y - mousePos.y;
            mandelbrot.nextImage.iterations -= yDiff;
            if (mandelbrot.nextImage.iterations < 1) {
                mandelbrot.nextImage.iterations = 1;
            }
        }

        mousePos = newPos;
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = e.getPoint();
    }
}
