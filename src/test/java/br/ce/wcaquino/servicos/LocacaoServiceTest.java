package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builders.FilmeBuilder.filme1;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.usuario1;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.isTodayWithDifferenceOfDays;
import static br.ce.wcaquino.matchers.MatchersProprios.itsToday;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

    private static LocacaoService locacaoService;
    private static int count = 0;

    private static List<Filme> filmes;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        System.out.println("Before");
        count++;
        System.out.println("Contador:" + count);

        filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 5.0));
        filmes.add(new Filme("Filme 2", 3, 5.0));
    }

    @After
    public void tearDown() {
        System.out.println("After");
    }

    @BeforeClass
    public static void setupClass() {
        locacaoService = new LocacaoService();
        LocacaoDAO locacaoDAO = new LocacaoDAOFake();
        locacaoService.setLocacaoDAO(locacaoDAO);

        System.out.println("Before Class");
    }

    @AfterClass
    public static void tearDownCLass() {
        System.out.println("After Class");
    }


    @Test
    public void testLocacao() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = new Usuario("Usuario 1");

        //When
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //Then
        Assert.assertEquals(10.0, locacao.getValor(), 0.01);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        Assert.assertThat(locacao.getValor(), is(equalTo(10.0)));
        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }

    //Utilizando ErrorCollector
    @Test
    public void testLocacaoErrorCollector() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = new Usuario("Usuario 1");

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        errorCollector.checkThat(locacao.getValor(), is(equalTo(10.0)));
        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void should_ReturnThrows_When_FilmeSemEstoque_Ex1() throws Exception {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        filmes.add(new Filme("Filme 1", 0, 5.0));

        //when
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void should_ReturnThrows_When_FilmeSemEstoque_Ex2() {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        filmes.add(new Filme("Filme 1", 0, 5.0));

        //when
        try {
            locacaoService.alugarFilme(usuario, filmes);
            Assert.fail("Esse teste deveria lançar uma exceção!");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    @Test
    public void should_ReturnThrows_When_FilmeSemEstoque_Ex3() throws Exception {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        filmes.add(new Filme("Filme 1", 0, 5.0));

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filme sem estoque");

        //when
        locacaoService.alugarFilme(usuario, filmes);
    }

    @Test
    public void should_ReturnThrows_When_UsuarioIsNull() throws FilmeSemEstoqueException {
        try {
            locacaoService.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    @Test
    public void should_ReturnThrows_When_FilmeIsNull() throws LocadoraException, FilmeSemEstoqueException {
        //given
        Usuario usuario = new Usuario("Usuario 1");

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        //when
        locacaoService.alugarFilme(usuario, null);
    }

    @Test
    public void should_ReturnResultWith25PerCentDiscount_When_Renting3Films() throws FilmeSemEstoqueException, LocadoraException {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 2, 4.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        Assert.assertEquals(11.0, locacao.getValor(), 0.1);
    }

    @Test
    public void should_ReturnResultWith50PerCentDiscountInFilm4_When_Renting4Films() throws FilmeSemEstoqueException, LocadoraException {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 2, 4.0));
        filmes.add(new Filme("Filme 4", 2, 4.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        Assert.assertEquals(13.0, locacao.getValor(), 0.1);
    }

    @Test
    public void should_ReturnResultWith75PerCentDiscountInFilm5_When_Renting5Films() throws FilmeSemEstoqueException, LocadoraException {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 2, 4.0));
        filmes.add(new Filme("Filme 4", 2, 4.0));
        filmes.add(new Filme("Filme 5", 2, 4.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        Assert.assertEquals(14.0, locacao.getValor(), 0.1);
    }

    @Test
    public void should_ReturnResultWith100PerCentDiscountInFilm6_When_Renting6Films() throws FilmeSemEstoqueException, LocadoraException {
        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme 1", 2, 4.0));
        filmes.add(new Filme("Filme 2", 2, 4.0));
        filmes.add(new Filme("Filme 3", 2, 4.0));
        filmes.add(new Filme("Filme 4", 2, 4.0));
        filmes.add(new Filme("Filme 5", 2, 4.0));
        filmes.add(new Filme("Filme 6", 2, 4.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        Assert.assertEquals(14.0, locacao.getValor(), 0.1);
    }

    @Test
    public void should_ReturnMonday_When_FilmRentedOnSaturday() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme Sábado", 1, 5.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        boolean isMonday = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(isMonday);
    }

    @Test
    public void should_ReturnMonday_When_FilmRentedOnSaturdayWithMatcher() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = new ArrayList<>();
        filmes.add(new Filme("Filme Sábado", 1, 5.0));

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        Assert.assertThat(locacao.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void should_ReturnCorrectValues_When_NewMovieWasRented() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = new Usuario("Usuario 1");

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //then
        errorCollector.checkThat(locacao.getValor(), is(equalTo(10.0)));
//        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        errorCollector.checkThat(locacao.getDataLocacao(), itsToday());
//        errorCollector.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
        errorCollector.checkThat(locacao.getDataRetorno(), isTodayWithDifferenceOfDays(1));
    }

    //Utilizando Builders
    @Test
    public void should_ReturnCorrectValues_When_NewMovieWasRented_Builder() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = usuario1().agora();
        List<Filme> filmesBuilder = Arrays.asList(filme1().agora());

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmesBuilder);

        //then
        errorCollector.checkThat(locacao.getValor(), is(equalTo(5.0)));
        errorCollector.checkThat(locacao.getDataLocacao(), itsToday());
        errorCollector.checkThat(locacao.getDataRetorno(), isTodayWithDifferenceOfDays(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void should_ReturnThrows_When_FilmeSemEstoque_Builder() throws Exception {
        //given
        Usuario usuario = usuario1().agora();
        List<Filme> filmesBuilder = Arrays.asList(filme1().semEstoque().agora());

        //when
        locacaoService.alugarFilme(usuario, filmesBuilder);
    }

    @Test
    public void should_ReturnCorrectValues_When_NewMovieWasRented_Builder_ALterandoValorFilme() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //given
        Usuario usuario = usuario1().agora();
        List<Filme> filmesBuilder = Arrays.asList(filme1().comValor(25.0).agora());

        //when
        Locacao locacao = locacaoService.alugarFilme(usuario, filmesBuilder);

        //then
        errorCollector.checkThat(locacao.getValor(), is(equalTo(25.0)));
        errorCollector.checkThat(locacao.getDataLocacao(), itsToday());
        errorCollector.checkThat(locacao.getDataRetorno(), isTodayWithDifferenceOfDays(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void should_ReturnThrows_When_FilmeSemEstoque_BuilderComBuilderSemEsqoque() throws Exception {
        //given
        Usuario usuario = usuario1().agora();
        List<Filme> filmesBuilder = Arrays.asList(umFilmeSemEstoque().agora());

        //when
        locacaoService.alugarFilme(usuario, filmesBuilder);
    }

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }
}
