package cpen221.mp1.cryptanalysis;

import java.util.Objects;

public class ComplexNumber {

    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber() {
        real = 0.0;
        imaginary = 0.0;
    }

    public ComplexNumber(ComplexNumber seed) {
        this.real = seed.real;
        this.imaginary = seed.imaginary;
    }

    public void add(ComplexNumber other) {
        this.real += other.real;
        this.imaginary += other.imaginary;
    }

    public void multiply(ComplexNumber other) {
        double real1 = this.real;
        double imaginary1 = this.imaginary;
        double real2 = other.real;
        double imaginary2 = other.imaginary;

        this.real = real1 * real2 - imaginary1 * imaginary2;
        this.imaginary = imaginary1 * real2 + real1 * imaginary2;
    }

    @Override
    public String toString() {
        double re = Math.round(this.real*1000)/1000.0;
        double im = Math.round(this.imaginary*1000)/1000.0;
        if (im < 0){
            return re + "" + im + "i";
        }
        else if(im == 0){
            return re + "";
        }
        return re + "+" + im + "i";
    }

    public double re() {
        return this.real;
    }

    public double im() {
        return this.imaginary;
    }

    @Override
    public boolean equals(Object other) {
        boolean out = false;
        if(other instanceof ComplexNumber){
            ComplexNumber o = (ComplexNumber) other; 
            out = this.real == o.re() && this.imaginary == o.im();
        }
        return out;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.re(), this.im());
    }
}
