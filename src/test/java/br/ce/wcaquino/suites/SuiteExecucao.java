package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//Em casos de integração contínuia, os testes definidos na suite seriam executados duas vezes
//Pois, por padrão, os testes são executados automaticamente pela integração
//@RunWith(Suite.class)
@SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})

public class SuiteExecucao {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("SuiteBeforeClass");
    }

    @AfterClass
    public static void afterClas() {
        System.out.println("SuiteAfterClass");
    }
}
