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
     * A {@link Mesa mesa} foi trancada (isto é, todos os {@link Jogador 
     * jogadores} {@link Jogada#TOQUE} tocaram seguidamente, nenhum tem uma 
     * {@link Pedra pedra} jogável na mão) e ganhou aquele que 
     * somava menos {@link Pedra#numeroDePontos pontos} nas pedras da mão. Essa
     * vitória vale 1 ponto.
     */
    CONTAGEM_DE_PONTOS(1), 

    /**
     * Um dos {@link Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} (que {@link Pedra#isCarroca() não era uma carroça} em um dos 
     * {@link Lado lados} da {@link Mesa mesa} (e depois o {@link 
     * Mesa#getNumeroEsquerda() número de um lado da mesa} não ficou igual ao
     * {@link  Mesa#getNumeroDireita() número do outro lado}. Essa
     * vitória vale 1 ponto.
     */
    BATIDA_SIMPLES(1), 

    /**
     * Caso raro: Se as seis {@link Pedra pedras} que o {@link Jogador jogador}  
     * {@link Jogador#recebeMao(com.github.abdonia.domino.Pedra[]) receber} 
     * {@link Pedra#isCarroca() forem todas carroça}, a partida termina 
     * imediatamente com {@link Vitoria vitória} pra a dupla desse jogador. Essa
     * vitória vale 1 ponto.
     */
    SEIS_CARROCAS_NA_MAO(1), 

    /**
     * Um dos {@link Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() era uma carroça} em um dos 
     * {@link Lado lados} da {@link Mesa mesa}, mas não poderia encaixara 
     * carroça no outro lado, pois o {@link Numero número} lá era diferente. 
     * Essa vitória vale 2 pontos.
     */
    CARROCA(2), 

    /**
     * Um dos {@link Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() não era uma carroça} e que poderia se
     * encaixar tanto {@link Mesa#getNumeroEsquerda() num lado da mesa} como 
     * {@link Mesa#getNumeroDireita() no outro}.
     * 
     * Essa vitória vale 3 pontos.
     */
    LA_E_LO(3), 

    /**
     * Um dos {@link Jogador jogadores} bateu jogando sua última {@link Pedra 
     * pedra} que {@link Pedra#isCarroca() era uma carroça} e que poderia se
     * encaixar tanto {@link Mesa#getNumeroEsquerda() num lado da mesa} como 
     * {@link Mesa#getNumeroDireita() no outro} (pois eram o mesmo {@link Numero
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
}
