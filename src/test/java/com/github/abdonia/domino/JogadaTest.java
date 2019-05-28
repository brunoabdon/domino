package com.github.abdonia.domino;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JogadaTest {

	@ParameterizedTest(name = "Deve jogar {0} na esquerda")
	@EnumSource(Pedra.class)
	@DisplayName("Deve jogar a pedra na esquerda")
	void deveCriarJogadaCertaNaEsquerda(final Pedra pedra) {
		auxDeveCriarJogadaCerta(Lado.ESQUERDO,pedra);
		
	}

	@ParameterizedTest(name = "Deve jogar {0} na direita")
	@EnumSource(Pedra.class)
	@DisplayName("Deve jogar a pedra na direita")
	void deveCriarJogadaCertaNaDireita(final Pedra pedra) {
		auxDeveCriarJogadaCerta(Lado.DIREITO,pedra);
	}
	
	@Test
	@DisplayName("Deve tocar sem pedra nem lado")
	void deveTratarToqueComoNenhumaJogada() throws Exception {
		final Jogada toque = Jogada.TOQUE;
		assertNull(toque.getLado());
		assertNull(toque.getPedra());
	}
	

	void auxDeveCriarJogadaCerta(final Lado lado, final Pedra pedra) {
		
		//acao
		final Jogada j = Jogada.de(pedra, lado);
		
		//verificacao
		assertEquals(j.getPedra(), pedra);
		assertEquals(j.getLado(), lado);
	}
	
	@ParameterizedTest(name="Deve jogar singletons de {0} na esquerda")
	@EnumSource(Pedra.class)
	@DisplayName("Deve jogar singletons na esquerda")
	void deveCriarJogadaSingletonNaEsquerda(final Pedra pedra) {
		auxDeveCriarJogadaSingleton(Lado.ESQUERDO, pedra);
	}

	@ParameterizedTest(name="Deve jogar singletons de {0} na direita")
	@EnumSource(Pedra.class)
	@DisplayName("Deve jogar singletons na direita")
	void deveCriarJogadaSingletonNaDireita(final Pedra pedra) {
		auxDeveCriarJogadaSingleton(Lado.DIREITO, pedra);
	}
	
	@EnumSource(Lado.class)
	@ParameterizedTest(name="Deve falhar com pedra nula na {0}")
	void deveFalharComPedraNull(final Lado lado) {
	    assertThrows(IllegalArgumentException.class, ()->Jogada.de(null,lado));
	}
	
	private void auxDeveCriarJogadaSingleton(
	        final Lado lado, final Pedra pedra) {
		
		//acao
		final Jogada jogada1 = Jogada.de(pedra, lado);
		final Jogada jogada2 = Jogada.de(pedra, lado);
		
		//verificacao
		assertTrue(jogada1 == jogada2);
	}
}
