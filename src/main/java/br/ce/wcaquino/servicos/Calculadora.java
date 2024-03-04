package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.DividePorZeroException;

public class Calculadora {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int divide(int a, int b) throws DividePorZeroException {
        if(b == 0)
            throw new DividePorZeroException();

        return a / b;
    }
}
