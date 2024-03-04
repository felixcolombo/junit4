import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void teste() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1, 1);
        Assert.assertEquals(0.512, 0.51, 0.01); //delta -> margem de erro de comparação entre doubles

        int i1 = 5;
        Integer i2 = 5;
//        Assert.assertEquals(i1, i2); -> Os tipos são diferentes!
        Assert.assertEquals(Integer.valueOf(i1), i2);
        Assert.assertEquals(i1, i2.intValue());

        Assert.assertEquals("teste", "teste");
        Assert.assertTrue("teste".equalsIgnoreCase("Teste"));
        Assert.assertTrue("teste".startsWith("te"));
        Assert.assertNotEquals("teste01", "teste02");
        Assert.assertEquals("Erro de comparação", "1", "1");//Mensagem aparece somente com erro na comparação

        Usuario u1 = new Usuario("Usuário 01");
        Usuario u2 = new Usuario("Usuário 01");
        Usuario u3 = u2;
        Usuario u4 = null;
        Assert.assertEquals(u1, u2);
        Assert.assertSame(u1, u1); //-> Comparar a instância do objeto
        Assert.assertNotSame(u1, u2);
        Assert.assertSame(u2, u3);
        Assert.assertNull(u4);
        Assert.assertNotNull(u1);
    }
}
