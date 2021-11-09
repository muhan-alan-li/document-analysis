package cpen221.mp1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cpen221.mp1.cryptanalysis.ComplexNumber;

public class Task6ComplexNumberTests {

    @Test
    public void testEquals() {
        ComplexNumber num1 = new ComplexNumber(1,1);
        ComplexNumber num1copy = new ComplexNumber(1,1);
        Assertions.assertEquals(true,num1.equals(num1copy));
    }

    @Test
    public void testNormalAdd() {
        ComplexNumber num1 = new ComplexNumber(1,1);
        ComplexNumber num2 = new ComplexNumber(-1,2);
        num1.add(num2);
        Assertions.assertEquals(0,num1.re());
        Assertions.assertEquals(3,num1.im());
        Assertions.assertEquals("0.0+3.0i",num1.toString());
        Assertions.assertEquals(false,num1.equals(num2));
    }

    @Test
    public void testReAndIm(){
        ComplexNumber num1 = new ComplexNumber(1,1);
        ComplexNumber num2 = new ComplexNumber(-1,-1);
        ComplexNumber num3 = new ComplexNumber(0,0);
        Assertions.assertEquals(1,num1.re());
        Assertions.assertEquals(-1,num2.re());
        Assertions.assertEquals(0,num3.re());
        Assertions.assertEquals(1,num1.im());
        Assertions.assertEquals(-1,num2.im());
        Assertions.assertEquals(0,num3.im());
    }

    @Test
    public void testNormalMultiply() {
        ComplexNumber multiply1 = new ComplexNumber(1,2);
        ComplexNumber multiply2 = new ComplexNumber(3,4);
        multiply1.multiply(multiply2);
        Assertions.assertEquals("-5.0+10.0i",multiply1.toString());
    }

    @Test
    public void testConjugateMultiply() {
        ComplexNumber conjugate1 = new ComplexNumber(1,2);
        ComplexNumber conjugate2 = new ComplexNumber(-1,2);

        conjugate1.multiply(conjugate2);
        System.out.println(conjugate1.toString());
        Assertions.assertEquals("-5.0",conjugate1.toString());
    }

    @Test
    public void testNegativeImaginary(){
        ComplexNumber conjugate1 = new ComplexNumber(1,2);
        ComplexNumber conjugate2 = new ComplexNumber(-1,-2);

        conjugate1.multiply(conjugate2);
        System.out.println(conjugate1.toString());
        Assertions.assertEquals("3.0-4.0i",conjugate1.toString());
    }

    @Test
    public void testComplexNumberRaw(){
        ComplexNumber zero = new ComplexNumber();
        Assertions.assertEquals("0.0",zero.toString());
    }

    @Test
    public void testComplexNumberSeed(){
        ComplexNumber num1 = new ComplexNumber(1,1);
        ComplexNumber num1seeded = new ComplexNumber(num1);
        Assertions.assertEquals("1.0+1.0i",num1seeded.toString());
    }

    @Test
    public void testHashcode(){
        ComplexNumber num1 = new ComplexNumber(1,1);
        System.out.println(num1.hashCode());
    }

    @Test
    public void testHashcodeWithBigNum(){
        ComplexNumber bigNum = new ComplexNumber(1000,8976);
        System.out.println(bigNum.hashCode());
    }
}
