/*
 * Copyright (C) 2016 Bruno Abdon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;


import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import com.github.abdonia.domino.Vontade;


/**
 *
 * @author Bruno Abdon
 */
public class GeniusMalignus 
        implements RandomGoddess, Jogador, OmniscientDominoEventListener {

    private static final Pedra[] TODAS = Pedra.values();
    
    //por desencargo. eh 28.
    private static final int QUANTAS_PEDRAS = TODAS.length;
    
    //pra deixar mais facil de ler
    private static final int TAMANHO_MAO = 6;
    private static final int QTD_JOGADORES = 4;
    
    private final LogJogo.Iterator logJogoIterator;
    
    private int[][] ordemPedras;
    private int idxPartida;
    private boolean[] jogador1Comeca;
    private int idxJogador1Comeca;
    private int idxPedra;
    private int placarInicialDupla1;
    private int placarInicialDupla2;
   
    

    public GeniusMalignus(final LogJogo logJogo) {
        this.logJogoIterator = logJogo.iterator();
    }
    
    @Override
    public Pedra[] embaralha() {
        final Pedra pedras[] = new Pedra[QUANTAS_PEDRAS];
        
        final int[] ordem = this.ordemPedras[this.idxPartida++];
        
        for (int i = 0; i < QUANTAS_PEDRAS; i++) {
            pedras[i] = TODAS[ordem[i]];
        }
        return pedras;
    }

    @Override
    public boolean jogador1Comeca() {
        return this.jogador1Comeca[this.idxJogador1Comeca++];
    }
    
    @Override
    public void dormeDefinido(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4) {
        assertNotNull(pedra1);
        assertNotNull(pedra2);
        assertNotNull(pedra3);
        assertNotNull(pedra4);
    }

    @Override
    public void jogadorRecebeuPedras(
            final int quemFoi, 
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        assertJogadorValido(quemFoi);
        assertNotNull(pedra1);
        assertNotNull(pedra2);
        assertNotNull(pedra3);
        assertNotNull(pedra4);
        assertNotNull(pedra5);
        assertNotNull(pedra6);
        
        /**
         * @TODO testar que todas as peças são distintas
         */
    }

    private void assertJogadorValido(final int quemFoi) {
        assertTrue(quemFoi >=1 );
        assertTrue(quemFoi <= 4);
    }

    @Override
    public void jogoAcabou(final int placarDupla1, final int placarDupla2) {
        assertTrue(placarDupla1 >= 6 || placarDupla2 >= 6);
        assertNotEquals(placarDupla1,placarDupla2);
    }

    @Override
    public void partidaEmpatou() {
        
    }

    @Override
    public void jogadorBateu(final int quemFoi, final Vitoria tipoDeVitoria) {
        assertJogadorValido(quemFoi);
        assertNotNull(tipoDeVitoria);
    }

    @Override
    public void partidaVoltou(int jogador) {
        assertJogadorValido(jogador);
    }

    @Override
    public void jogadorTocou(final int jogador) {
        assertJogadorValido(jogador);
        assertNull(ordemPedras[idxPartida][idxPedra++]);
    }

    @Override
    public void jogadorJogou(
            final int jogador, 
            final Lado lado, 
            final Pedra pedra) {
        assertJogadorValido(jogador);
        assertNotNull(pedra);
        assertNotNull(lado);
    }

    @Override
    public void decididoQuemComeca(
            final int jogador, 
            final boolean consentimentoMutuo) {
        assertJogadorValido(jogador);
        if(!consentimentoMutuo){
            assertEquals(
                jogador1Comeca[idxJogador1Comeca++],
                jogador%2!=0);
        }
    }

    @Override
    public void partidaComecou(
            final int placarDupla1, 
            final int placarDupla2, 
            final boolean ehDobrada) {
        assertTrue(placarDupla1 >= placarInicialDupla1);
        assertTrue(placarDupla2 >= placarInicialDupla2);
        assertTrue(placarDupla1 < 6);
        assertTrue(placarDupla2 < 6);

        this.placarInicialDupla1 = placarDupla1;
        this.placarInicialDupla2 = placarDupla2;
    }

    @Override
    public void jogoComecou(
            final String nomeDoJogador1, 
            final String nomeDoJogador2, 
            final String nomeDoJogador3, 
            final String nomeDoJogador4) {
        assertNotNull(nomeDoJogador1);
        assertNotNull(nomeDoJogador2);
        assertNotNull(nomeDoJogador3);
        assertNotNull(nomeDoJogador4);
    }

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        assertNotNull(mesa);
        assertJogadorValido(cadeiraQueSentou); //x >=1 && x <=4
    }

    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        assertNotNull(pedra1);
        assertNotNull(pedra2);
        assertNotNull(pedra3);
        assertNotNull(pedra4);
        assertNotNull(pedra5);
        assertNotNull(pedra6);
        //comparar pedras com as esperadas
    }

    @Override
    public Jogada joga() {
        final int ordinalPedra = ordemPedras[idxPartida][idxPedra];
        return null;//Jogada.jogada(TODAS[ordinalPedra], );
    }

    @Override
    public Vontade vontadeDeComecar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
