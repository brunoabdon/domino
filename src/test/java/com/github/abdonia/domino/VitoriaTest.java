package com.github.abdonia.domino;

import static com.github.abdonia.domino.Vitoria.BATIDA_SIMPLES;
import static com.github.abdonia.domino.Vitoria.CONTAGEM_DE_PONTOS;
import static com.github.abdonia.domino.Vitoria.CRUZADA;
import static com.github.abdonia.domino.Vitoria.LA_E_LO;
import static com.github.abdonia.domino.Vitoria.SEIS_CARROCAS_NA_MAO;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class VitoriaTest {

    @Test
    void testGetPontos() {

        final Vitoria batidaSimples = Vitoria.BATIDA_SIMPLES;
        final Vitoria carroca = Vitoria.CARROCA;
        final Vitoria contagemDePontos = Vitoria.CONTAGEM_DE_PONTOS;
        final Vitoria cruzada = Vitoria.CRUZADA;
        final Vitoria laElo = Vitoria.LA_E_LO;
        final Vitoria seisCarrocas = Vitoria.SEIS_CARROCAS_NA_MAO;
        
        assertEquals(batidaSimples.getPontos(),1);
        assertEquals(carroca.getPontos(),2);
        assertEquals(contagemDePontos.getPontos(),1);
        assertEquals(cruzada.getPontos(),4);
        assertEquals(laElo.getPontos(),3);
        assertEquals(seisCarrocas.getPontos(),1);
    }

    @EnumSource(Pedra.class)
    @ParameterizedTest
    void testTipoDeBatidaNuncaEhNull(final Pedra pedra) {
        final Vitoria batidaFechando = Vitoria.tipoDeBatida(pedra, true);
        final Vitoria batidaSemFechar = Vitoria.tipoDeBatida(pedra,false);
        
        assertThat(
            asList(batidaFechando,batidaSemFechar), 
            everyItem(notNullValue())
        );
    }
    
    @EnumSource(Pedra.class)
    @ParameterizedTest(name="Testa fechar a mesa com {0}")
    void testTipoDeBatidaFechandoAMesa(final Pedra pedra) {
        final boolean fechouAMesa = true;
        auxTestTipoDeBatidaFechandoAMesa(pedra, fechouAMesa);
    }

    @EnumSource(Pedra.class)
    @ParameterizedTest(name="Testa bater com {0} sem fechar a mesa")
    void testTipoDeBatidaSemFecharAMesa(final Pedra pedra) {
        final boolean fechouAMesa = true;
        auxTestTipoDeBatidaFechandoAMesa(pedra, fechouAMesa);
    }

    private void auxTestTipoDeBatidaFechandoAMesa(final Pedra pedra,
            final boolean fechouAMesa) {
        final boolean ehCarroca = pedra.isCarroca();
        
        final boolean deveriaSerCruzada = fechouAMesa && ehCarroca;
        final boolean deveriaSerLaELo = fechouAMesa &&  !ehCarroca;
        
        final boolean deveriaSerBatidaSimples = !fechouAMesa && !ehCarroca;
        final boolean deveriaSerContagemDePontos = false;
        final boolean deveriaSer6CarrocasNaMao = false; 
                
        
        final Vitoria tipoDeBatida = Vitoria.tipoDeBatida(pedra, fechouAMesa);
        final boolean foiCruzada = tipoDeBatida == CRUZADA;
        final boolean foiLaELo = tipoDeBatida == LA_E_LO;
        final boolean foiBatidaSimples = tipoDeBatida == BATIDA_SIMPLES;
        final boolean foiContagemDePontos = tipoDeBatida == CONTAGEM_DE_PONTOS;
        final boolean foi6CarrocasNaMao = tipoDeBatida == SEIS_CARROCAS_NA_MAO;
        
        assumeTrue(tipoDeBatida != null);
        
        assertEquals(foiCruzada,deveriaSerCruzada);
        assertEquals(foiLaELo,deveriaSerLaELo);
        assertEquals(foiBatidaSimples,deveriaSerBatidaSimples);
        assertEquals(foiContagemDePontos,deveriaSerContagemDePontos);
        assertEquals(foi6CarrocasNaMao,deveriaSer6CarrocasNaMao);
    }
    
}
