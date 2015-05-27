package springbook.learningtest.template;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CalculatorTest {
    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() {
        calculator = new Calculator();
        numFilepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws Exception {
        assertThat(calculator.calcSum(this.numFilepath), is(10));
    }
    
    @Test
    public void multiplyOfNumbers() throws Exception {
        assertThat(calculator.calcMultiply(this.numFilepath), is(24));
    }
    
    @Test
    public void concatenateStrings() throws Exception {
        assertThat(calculator.concatenate(this.numFilepath), is("1234"));
    }

}