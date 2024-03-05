package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {}

    public static FilmeBuilder filme1() {
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        filmeBuilder.filme.setEstoque(2);
        filmeBuilder.filme.setNome("Filme 1");
        filmeBuilder.filme.setPrecoLocacao(5.0);

        return filmeBuilder;
    }

    public static FilmeBuilder umFilmeSemEstoque() {
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        filmeBuilder.filme.setEstoque(0);
        filmeBuilder.filme.setNome("Filme 1");
        filmeBuilder.filme.setPrecoLocacao(5.0);

        return filmeBuilder;
    }

    public FilmeBuilder semEstoque() {
        filme.setEstoque(0);

        return this;
    }

    public FilmeBuilder comValor(Double valor){
        filme.setPrecoLocacao(valor);
        return this;
    }

    public Filme agora() {
        return filme;
    }

}
