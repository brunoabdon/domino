/*
 * Copyright (C) 2017 Bruno Abdon
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
package com.github.abdonia.domino;

/**
 * As diferente maneiras que um {@link Jogador} pode ganhar uma partida 
 * (uma rodada) de dominó.
 * 
 * @author Bruno Abdon
 */
public enum Vitoria {

    /**
     * A {@linkplain Mesa mesa} foi trancada (isto é, todos os {@link Jogador 
     * jogadores} {@linkplain Jogada#TOQUE tocaram} seguidamente, nenhum tem uma 
     * {@linkplain Pedra pedra} jogável na mão) e ganhou aquele que 
     * somava menos {@linkplain Pedra#getNumeroDePontos() pontos} nas pedras da mão. 
     * Essa vitória vale 1 ponto.
     */
    CONTAGEM_DE_PONTOS(1), 

    /**
     * Um dos {@linkplain Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} (que {@link Pedra#isCarroca() não era uma carroça}) em um dos 
     * {@linkplain Lado lados} da {@linkplain Mesa mesa} (e depois o {@link 
     * Mesa#getNumeroEsquerda() número de um lado da mesa} não ficou igual ao
     * {@link  Mesa#getNumeroDireita() número do outro lado}). Essa
     * vitória vale 1 ponto.
     */
    BATIDA_SIMPLES(1), 

    /**
     * Caso raro: Se as seis {@linkplain Pedra pedras} que o {@linkplain Jogador jogador}  
     * {@link Jogador#recebeMao(Pedra, Pedra, Pedra, Pedra, Pedra, Pedra) 
     * receber} 
     * {@link Pedra#isCarroca() forem todas carroça}, a partida termina 
     * imediatamente com {@link Vitoria vitória} pra a dupla desse jogador. Essa
     * vitória vale 1 ponto.
     */
    SEIS_CARROCAS_NA_MAO(1), 

    /**
     * Um dos {@linkplain Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() era uma carroça} em um dos 
     * {@linkplain Lado lados} da {@linkplain Mesa mesa}, mas não poderia encaixara 
     * carroça no outro lado, pois o {@link Numero número} lá era diferente. 
     * Essa vitória vale 2 pontos.
     */
    CARROCA(2), 

    /**
     * Um dos {@linkplain Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() não era uma carroça} e que poderia se
     * encaixar tanto {@linkplain Mesa#getNumeroEsquerda() num lado da mesa} como 
     * {@linkplain Mesa#getNumeroDireita() no outro}.
     * 
     * Essa vitória vale 3 pontos.
     */
    LA_E_LO(3), 

    /**
     * Um dos {@linkplain Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() era uma carroça} e que poderia se
     * encaixar tanto {@linkplain Mesa#getNumeroEsquerda() num lado da mesa} como 
     * {@linkplain Mesa#getNumeroDireita() no outro} (pois eram o mesmo {@link Numero
     * número}.
     * 
     * Essa vitória vale 4 pontos.
     */
    CRUZADA(4);

    private final int pontos;

    private Vitoria(final int pontos) {
        this.pontos = pontos;
    }

    /**
     * Quantos pontos vale essa vitória.
     * 
     * @return Quantos pontos vale a vitória.
     */
    public int getPontos(){
        return this.pontos;
    }
    
    /**
     * Diz qual é o tipo de vitória que acontece quando se
     * bate com uma dada {@linkplain Pedra pedra}, fechando ou não a mesa.
     * @param ultimaPedra a última {@linkplain Pedra pedra} da mão do {@link 
     * Jogador}, que ele {@link Jogador#joga() jogou} e bateu.
     * @param fechouAMesa Diz se, depois dessa última {@linkplain Jogada 
     * jogada}, a mesa ficou com {@link Mesa#getNumeroEsquerda() o número de um
     * lado} igual ao {@link  Mesa#getNumeroDireita() número do outro}.
     * 
     * @return {@link #CRUZADA}, se a {@code ultimaPedra} for uma {@linkplain 
     * Pedra#isCarroca() carroça} e tiver fechado a mesa, ou {@link #LA_E_LO} se
     * tive fechado a mesa com uma pedra que não é carroça, ou {@link #CARROCA},
     * se a última pedra foi uma carroça que não fechou a mesa ou {@link 
     * #BATIDA_SIMPLES} caso nenhuma dessas condições acontecer.
     */
    public static Vitoria tipoDeBatida(
            final Pedra ultimaPedra, 
            final boolean fechouAMesa) {

        final boolean carroca = ultimaPedra.isCarroca();

        return
            carroca && fechouAMesa  ?   CRUZADA
            : fechouAMesa           ?   LA_E_LO
            : carroca               ?   CARROCA
            :                           BATIDA_SIMPLES;
    }
    
}
