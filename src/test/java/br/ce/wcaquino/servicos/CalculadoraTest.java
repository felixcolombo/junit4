package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.DividePorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup() {
        calculadora = new Calculadora();
    }

    @Test
    public void should_ReturnCorrectValue_When_AddingTwoValues() {
        //given
        int a = 5;
        int b = 5;

        //when
        int result = calculadora.add(a, b);

        //then
        Assert.assertEquals(10, result);
    }

    @Test
    public void should_ReturnCorrectValue_When_SubtractingTwoValues() {
        //given
        int a = 5;
        int b = 5;

        //when
        int result = calculadora.subtract(a, b);

        //then
        Assert.assertEquals(0, result);
    }

    @Test
    public void should_ReturnCorrectValue_When_DividingTwoValues() throws DividePorZeroException {
        //given
        int a = 5;
        int b = 5;

        //when
        int result = calculadora.divide(a, b);

        //then
        Assert.assertEquals(1, result);
    }

    @Test(expected = DividePorZeroException.class)
    public void should_ReturnThrows_When_DividingByZero() throws DividePorZeroException {
        //given
        int a = 5;
        int b = 0;

        //when
        int result = calculadora.divide(a, b);

        //then
        Assert.assertEquals(1, result);
    }
}
