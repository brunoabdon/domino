package com.github.abdonia.domino;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.hamcrest.object.HasToString.hasToString;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PedraTest {

    @EnumSource(Pedra.class)
    @ParameterizedTest
    void testaCarroca(final Pedra pedra){
        
        final Numero primeiroNumero = pedra.getPrimeiroNumero();
        final Numero segundoNumero = pedra.getSegundoNumero();
        final boolean isCarroca = pedra.isCarroca();
        
        assertThat(primeiroNumero, notNullValue());
        assertThat(segundoNumero, notNullValue());
        
        assertTrue(isCarroca == (primeiroNumero == segundoNumero));
    }
    

    @EnumSource(Pedra.class)
    @ParameterizedTest
    void testaValorValidoPontos(final Pedra pedra){
        final int pontosLimpo = 0;
        final int pontosDozão = 12;

        final Numero primeiroNumero = pedra.getPrimeiroNumero();
        final Numero segundoNumero = pedra.getSegundoNumero();

        assumeTrue(primeiroNumero != null);
        assumeTrue(segundoNumero != null);
        
        final int pontosPrimeiroNumero = primeiroNumero.getPontos();
        final int pontosSegundoNumero = segundoNumero.getPontos();
        
        //acao
        final int pontos = pedra.getPontos();
        
        //verificacao
        assertThat(
            pontos, 
            allOf(
                greaterThanOrEqualTo(pontosLimpo),
                lessThanOrEqualTo(pontosDozão),
                is(pontosPrimeiroNumero + pontosSegundoNumero)
            )
        );
        
    }
    

    @EnumSource(Pedra.class)
    @ParameterizedTest
    void testTemNumero(final Pedra pedra) {
        final Numero primeiroNumero = pedra.getPrimeiroNumero();
        final Numero segundoNumero = pedra.getSegundoNumero();

        final Matcher<Numero> pedraTem = pedraTem(pedra);
        
        assertThat(
            asList(primeiroNumero, segundoNumero), 
            everyItem(
                allOf(
                    notNullValue(),
                    pedraTem
                )
            )
        );
        
       assertTrue(pedra.temNumero(primeiroNumero));
        
    }
    
    private Matcher<Numero> pedraTem(final Pedra pedra) {
        return new TypeSafeMatcher<Numero>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText(" contained in " + pedra);
            }

            @Override
            protected boolean matchesSafely(final Numero numero) {
                return pedra.temNumero(numero);
            }
        };
    }

    @EnumSource(Pedra.class)
    @ParameterizedTest
    void testToString(final Pedra pedra) {
        assertThat(pedra,hasToString(not(emptyOrNullString())));
    }

    @Test
    void testQuantidade() {
        final int quantosPossiveis = Pedra.values().length;
        assertEquals(quantosPossiveis, 28);
    }
    
}
