package org.codemob.fractal.util;

import java.awt.image.BufferedImage;

public class FractalImage extends BufferedImage {
    public int iterations;
    public double zoom = 6;
    public double x = 0;
    public double y = 0;
    public FractalImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }
}
