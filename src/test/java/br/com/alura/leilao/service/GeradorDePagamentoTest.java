package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {
	
	private GeradorDePagamento geradorDePagamento;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
	}

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance lanceVencedor = leilao.getLanceVencedor();
		geradorDePagamento.gerarPagamento(lanceVencedor);
		
		Mockito.verify(pagamentoDao).salvar(captor.capture());
				
		Pagamento pagamento = captor.getValue();
		
		assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		assertEquals(lanceVencedor.getValor(), pagamento.getValor());
		assertFalse(pagamento.getPago());
		assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
		assertEquals(lanceVencedor.getLeilao(), pagamento.getLeilao());
		
	}

	private Leilao leilao() {
		List<Leilao> lista = new ArrayList<>();

		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));

		leilao.propoe(segundo);
		leilao.setLanceVencedor(segundo);

		return leilao;

	}

}
