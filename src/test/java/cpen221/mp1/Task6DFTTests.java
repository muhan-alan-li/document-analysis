package cpen221.mp1;

import cpen221.mp1.cryptanalysis.ComplexNumber;
import cpen221.mp1.cryptanalysis.DFT;
import org.junit.jupiter.api.Test;

public class Task6DFTTests {

    @Test
    public void testInts(){
        int[] input = {1,2,3,4,5};
        ComplexNumber[] out = DFT.dft(input);
        for(ComplexNumber cn : out){
            System.out.println(cn.toString());
        }
    }

    @Test
    public void testComplex(){
        ComplexNumber a = new ComplexNumber(1.0,1.0);
        ComplexNumber b = new ComplexNumber(2.3,2.3);
        ComplexNumber c = new ComplexNumber(3.5,3.5);
        ComplexNumber[] input = {a,b,c};
        ComplexNumber[] out = DFT.dft(input);
        for(ComplexNumber cn : out){
            System.out.println(cn.toString());
        }
    }
}
