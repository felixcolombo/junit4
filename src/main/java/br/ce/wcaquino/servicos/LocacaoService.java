package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO locacaoDAO;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		if(usuario == null)
			throw new LocadoraException("Usuário vazio");

		if(filmes == null || filmes.isEmpty())
			throw new LocadoraException("Filme vazio");

        for(Filme filme : filmes) {
			if (filme.getEstoque() == 0)
				throw new FilmeSemEstoqueException("Filme sem estoque");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		double valorTotal = 0;

		for(int i = 0; i < filmes.size(); i++) {

			Filme filme = filmes.get(i);
			Double valorLocacaoFilme = filme.getPrecoLocacao();

			switch (i) {
				case 2: valorLocacaoFilme = valorLocacaoFilme * 0.75; break;
				case 3: valorLocacaoFilme = valorLocacaoFilme * 0.5; break;
				case 4: valorLocacaoFilme = valorLocacaoFilme * 0.25; break;
				case 5: valorLocacaoFilme = 0.0; break;
			}

			valorTotal += valorLocacaoFilme;
		}

		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		locacaoDAO.salvar(locacao);

		return locacao;
	}

	//Injeção de dependência
	public void setLocacaoDAO(LocacaoDAO locacaoDAO) {
		this.locacaoDAO = locacaoDAO;
	}
}