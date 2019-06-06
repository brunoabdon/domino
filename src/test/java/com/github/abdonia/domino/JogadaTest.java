package com.github.abdonia.domino;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.object.HasToString.hasToString;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

class JogadaTest {

    private final <X> Matcher<X> hasNonEmptyToString(){
        return hasToString(not(emptyOrNullString())); 
    }
    
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
	void deveTratarToqueComoNenhumaJogada() {
		final Jogada toque = Jogada.TOQUE;

		assertThat(
	        toque,
	        is(allOf(
                notNullValue(),
                hasProperty("lado", nullValue()),
                hasProperty("pedra", nullValue())
            ))
        );
	}

	void auxDeveCriarJogadaCerta(final Lado lado, final Pedra pedra) {
		
		//acao
		final Jogada jogada = Jogada.de(pedra, lado);
				
		//verificacao
		assertThat(
	        jogada, 
	        allOf(
                notNullValue(),
                hasProperty("pedra", is(pedra)),
	            hasProperty("lado", is(lado))
            )
        );
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
	
	@NullSource
	@EnumSource(Lado.class)
	@DisplayName("Deve falhar com pedra nula")
	@ParameterizedTest(name="Deve falhar com pedra nula na {0}")
	void deveFalharComPedraNull(final Lado lado) {
	    assertThrows(IllegalArgumentException.class, ()->Jogada.de(null,lado));
	}

	@NullSource
    @EnumSource(Pedra.class)
	@DisplayName("Deve falhar com lado null")
    @ParameterizedTest(name="Deve falhar ao jogar {0} em lado nulo")
    void deveFalharComLadoNull(final Pedra pedra) {
        assertThrows(IllegalArgumentException.class,()->Jogada.de(pedra,null));
    }
	
	private void auxDeveCriarJogadaSingleton(
	        final Lado lado, final Pedra pedra) {
		
		//acao
		final Jogada jogada1 = Jogada.de(pedra, lado);
		final Jogada jogada2 = Jogada.de(pedra, lado);
		
		//verificacao
		assertSame(jogada1, jogada2);
	}

    @EnumSource(Pedra.class)
    @DisplayName("Deve suportar toString")
    @ParameterizedTest(name="Deve suportar toString pra {0}")
	void deveImplementarToString(final Pedra pedra){
	    //cenario
        final Jogada jogada = Jogada.de(pedra, Lado.ESQUERDO);
        
        assertThat(jogada, hasNonEmptyToString());
	}
    
    @Test
    @DisplayName("Deve suportar toString pra toque")
    void deveImplementarToStringPraToque(){
        //cenario
        final Jogada jogada = Jogada.TOQUE;
        
        //verificacao
        assertThat(jogada, hasNonEmptyToString());

    }
}
