package org.codemob.fractal.util;

public class Complex {
    public final double real;
    public final double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex multiply(Complex c) {
        double realTemp      = real * c.real      - imaginary * c.imaginary;
        double imaginaryTemp = real * c.imaginary + imaginary * c.real;

        return new Complex(realTemp, imaginaryTemp);
    }

    public Complex add(Complex c) {
        return new Complex(real + c.real, imaginary + c.imaginary);
    }

    public Complex copy() {
        return new Complex(real, imaginary);
    }

    public double abs() {
        return Math.hypot(real, imaginary);
    }
}
