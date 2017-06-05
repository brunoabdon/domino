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
package com.github.abdonia.domino.eventos;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;

/**
 * Interface para receber eventos publicos do que acontece durante o jogo.
 * 
 * {@linkplain com.github.abdonia.domino.Jogador Jogadores} que implementarem 
 * esta interface serão automaticamente registrados para serem avisados dos 
 * eventos.
 * 
 * @author Bruno Abdon
 */
public interface DominoEventListener {

    /**
     * O jogo comecou. O placar está zero a zero (um jogo é a 
     * seqëncia de várias partidas).
     *   
     * @param nomeDoJogador1 nome do jogador 1 (dupla 1)
     * @param nomeDoJogador2 nome do jogador 2 (dupla 2)
     * @param nomeDoJogador3 nome do jogador 3 (dupla 1)
     * @param nomeDoJogador4 nome do jogador 4 (dupla 2)
     */
    public default void jogoComecou(
            final String nomeDoJogador1, 
            final String nomeDoJogador2, 
            final String nomeDoJogador3, 
            final String nomeDoJogador4){
    }

    /**
     * Mais uma partida começou (um jogo tem várias partidas).
     *  
     * @param placarDupla1 Quantos pontos a dupla 1 tem.
     * @param placarDupla2 Quantos pontos a dupla 2 tem
     * @param ehDobrada diz se os pontos dessa partida valeram em dobro, por 
     * causa de um empate na partida anterior (por ser o caso de ser uma 
     * seqüência de empates)
     */
    public default void partidaComecou(
            final int placarDupla1, 
            final int placarDupla2, 
            final boolean ehDobrada){
    }

    /**
     * Foi definido, por consentimento ou aleatoriamente, que {@link 
     * com.github.abdonia.domino.Jogador} da dupla que ganhou a partida anterior 
     * vai começar a partida.
     * 
     * A decisão é tomada {@linkplain 
     * com.github.abdonia.domino.Jogador#vontadeDeComecar() peguntando-se a cada
     * jogador da dupla o quanto ele quer começar a partida}. Quando um dos dois
     * "quer" mais que o outro, diz-se que a decisão foi tomada por 
     * consentimento mútuo. Quando os dois "empatam" sobre quem mais quer 
     * começar, um dos dois é escolhido aleatoriamente, e é dito que não houve 
     * consentimento mútuo na decisão.
     * 
     * @param quemFoi O jogador (identificado pelo {@linkplain  
     * com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira que sentou}) que 
     * vai começar a partida.
     * 
     * @param consentimentoMutuo Diz se a decisão foi tomanda por consentimento
     * mútuo, ou se o jogador teve que ser escolhido aleatoriamente.
     */
    public default void decididoQuemComeca(
        final int quemFoi, final boolean consentimentoMutuo){
    }
    
    /**
     * Um determinado {@link com.github.abdonia.domino.Jogador} {@linkplain 
     * com.github.abdonia.domino.Jogada jogou} uma {@link Pedra} (e nao 
     * {@linkplain com.github.abdonia.domino.Jogada#TOQUE tocou}). (Se ele tiver 
     * batido, além desse evento, também ocorrerá {@link 
     * #jogadorBateu(int, Vitoria)}.
     * 
     * @param quemFoi O jogador (identificado pelo {@linkplain  
     * com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira que sentou}) que 
     * jogou.
     * @param lado onde jogou.
     * @param pedra o que jogou.
     */
    public default void jogadorJogou(
            final int quemFoi, 
            final Lado lado, 
            final Pedra pedra){
    }

    /**
     * Um {@link com.github.abdonia.domino.Jogador} 
     * {@linkplain com.github.abdonia.domino.Jogada#TOQUE tocou}.
     * 
     * @param quemFoi O jogador (identificado pelo {@linkplain  
     * com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira que sentou}) que 
     * tocou.
     */
    public default void jogadorTocou(final int quemFoi){
    }

    /**
     * A partida voltou logo depois de serem distribuidas as {@linkplain Pedra 
     * pedras} porque um dos {@linkplain com.github.abdonia.domino.Jogador 
     * jogadores} tinha 5 {@linkplain Pedra#isCarroca() carroças} na mão. 
     * (Ninguém marca ponto quando isso acontece).
     * 
     * @param quemFoi O jogador (identificado pelo {@linkplain  
     * com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira que sentou}) que 
     * tinha cinco pedras na mão.
     */
    public default void partidaVoltou(final int quemFoi){
    }
    
    /**
     * Um {@link com.github.abdonia.domino.Jogador} bateu e a partida acabou. 
     * O jogo ainda pode continuar.
     * 
     * @param quemFoi O jogador (identificado pelo {@linkplain  
     * com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira que sentou}) que
     * bateu.
     * 
     * @param tipoDeVitoria Como foi a batida.
     */
    public default void jogadorBateu(
            final int quemFoi, final Vitoria tipoDeVitoria){
    }

    /**
     * A partida acabou empatada. O jogo vai continuar.
     */
    public default void partidaEmpatou(){
    }

    /**
     * Uma das duplas fez 6 pontos (ou mais) e o jogo acabou.
     * 
     * @param placarDupla1 quantos pontos tinha a dupla 1
     * @param placarDupla2 quantos pontos tinha a dupla 2
     */
    public default void jogoAcabou(
            final int placarDupla1, final int placarDupla2){
    }	
}