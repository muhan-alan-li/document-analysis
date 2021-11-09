package cpen221.mp1.cryptanalysis;

import org.checkerframework.checker.units.qual.C;

public abstract class DFT {

    public static ComplexNumber[] dft(ComplexNumber[] inSignal) {
        int size = inSignal.length;
        ComplexNumber[] out = new ComplexNumber[size];

        for(int k=0;k<size;k++){
            ComplexNumber term = new ComplexNumber();
            for(int j=0;j<size;j++){
                ComplexNumber subterm = new ComplexNumber(inSignal[j]);
                double theta = 2*Math.PI*(j*k)/size;
                ComplexNumber trigComponent = new ComplexNumber(Math.cos(theta), -1.0*Math.sin(theta));
                subterm.multiply(trigComponent);
                term.add(subterm);
            }
            out[k] = term;
        }
        return out;
    }

    public static ComplexNumber[] dft(int[] inSignal) {
        int size = inSignal.length;
        ComplexNumber[] input = new ComplexNumber[size];

        for(int i=0; i<size; i++){
            input[i] = new ComplexNumber((double) inSignal[i], 0.0);
        }
        return dft(input);
    }

}