package br.com.alura.leilao.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.LanceBuilder;
import br.com.alura.leilao.util.builder.LeilaoBuilder;
import br.com.alura.leilao.util.builder.UsuarioBuilder;
import org.junit.Assert;

class LanceDaoTest {
	
	private LanceDao dao;
	
	private EntityManager em;
	
	@BeforeEach
	public void instanciar() {
		this.em = JPAUtil.getEntityManager();
		this.dao = new LanceDao(em);
		em.getTransaction().begin();
	}
	
	@AfterEach
	public void encerrar() {
		em.getTransaction().rollback();
	}
	
	@Test
	public void deveriaBuscarOMaiorLanceDoLeilao() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		Leilao leilao = new LeilaoBuilder()
				.comNome("Mochila")
				.comValorInicial("500")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.criar();
		em.persist(leilao);
		
		List<Lance> lances = new ArrayList<>();
		
		Lance lance = new LanceBuilder()
				.comValor("500")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.comLeilao(leilao)
				.criar();
		dao.salvar(lance);
		
		Lance segundoLance = new LanceBuilder()
				.comValor("540")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.comLeilao(leilao)
				.criar();
		dao.salvar(segundoLance);
		
		Lance terceiroLance = new LanceBuilder()
				.comValor("537")
				.comData(LocalDate.now())
				.comUsuario(usuario)
				.comLeilao(leilao)
				.criar();
		dao.salvar(terceiroLance);
		
		lances.add(lance);
		lances.add(segundoLance);
		lances.add(terceiroLance);
		
		leilao.setLances(lances);
		Lance maiorLance = dao.buscarMaiorLanceDoLeilao(leilao);
		
		Assert.assertEquals(new BigDecimal("540"), maiorLance.getValor());
	}

}
