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

import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 * Um {@link DominoEventListener} que também escuta eventos com informações
 * <em>sigilosas</em> ou <em>técnicas</em> sobre o que acontece durante um jogo,
 * como a lista de {@linkplain Pedra pedras} que cada {@linkplain
 * com.github.abdonia.domino.Jogador jogador} {@linkplain
 * com.github.abdonia.domino.Jogador#recebeMao(Pedra, Pedra, Pedra, Pedra,
 * Pedra, Pedra) recebeu}, a lista de pedras no dorme e erros por partes dos
 * jogadores que levam ao cancelamento do jogo.
 *
 * <p>Essa interface deve ser implementada por UIs, Loggers, etc. mas não por
 * {@link com.github.abdonia.domino.Jogador Jogadores}.</p>
 *
 * @author Bruno Abdon
 */
public interface OmniscientDominoEventListener extends DominoEventListener {

    /**
     * Avisa que, no início de uma partida, um determinado
     * {@linkplain com.github.abdonia.domino.Jogador jogador} recebeu suas
     * {@linkplain Pedra pedras}.
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     * @param pedra1 A primeira {@linkplain Pedra pedra} da mão do jogador.
     * @param pedra2 A segunda {@linkplain Pedra pedra} da mão do jogador.
     * @param pedra3 A terceira {@linkplain Pedra pedra} da mão do jogador.
     * @param pedra4 A quarta {@linkplain Pedra pedra} da mão do jogador.
     * @param pedra5 A quinta {@linkplain Pedra pedra} da mão do jogador.
     * @param pedra6 A última {@linkplain Pedra pedra} da mão do jogador.
     */
    public default void jogadorRecebeuPedras(
            final int quemFoi,
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6){
    }

    /**
     * Avisa que, no início de uma partida, as pedras foram distribuídas e as
     * quatro que sobraram foram pra o dorme.
     *
     * @param pedra1 A primeira {@linkplain Pedra pedra} do dorme.
     * @param pedra2 A segunda {@linkplain Pedra pedra} do dorme.
     * @param pedra3 A terceira {@linkplain Pedra pedra} do dorme.
     * @param pedra4 A última {@linkplain Pedra pedra} do dorme.
     */
    public default void dormeDefinido(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4){
    }

    /**
     * O {@linkplain com.github.abdonia.domino.Jogador jogador} {@linkplain
     * com.github.abdonia.domino.Jogador#joga() jogou} uma {@linkplain Pedra
     * pedra} que não cabia na {@linkplain com.github.abdonia.domino.Mesa mesa}.
     *
     * <p>Isso é um erro grave (bug da implementação do jogador), e faz o jogo
     * ser abortado.</p>
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     * @param pedra a {@link Pedra} que tentou jogar.
     *
     * @param numero o {@link Numero} que a pedra deveria ter pra caber na mesa.
     */
    public default void jogadorJogouPedraInvalida(
        final int quemFoi, final Pedra pedra, final Numero numero){
    }

    /**
     * Foi {@linkplain com.github.abdonia.domino.Jogador#getVontadeDeComecar()
     * perguntado} a um dos {@linkplain com.github.abdonia.domino.Jogador
     * jogadores} da dupla que venceu a partida anterior se ele queria ser o
     * primeiro a jogar, e ele respondeu "{@code null}".
     *
     * <p>Isso é um erro grave (bug da implementação do jogador), e faz o jogo
     * ser abortado.</p>
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     */
    public default void jogadorErrouVontadeDeComecar(final int quemFoi){
    }

    /**
     * O jogo vem por meio deste método informar que o jogador que estava
     * sentado na cadeira cujo número está indicado como parâmetro já não está
     * mais entre nós. Ele teve um crash súbito, mas foi em paz. Em respeito ao
     * luto, e dada a impossibilidade de defunto jogar, o jogo vai ser
     * interrompido.
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     * @param causaMortis Uma mensagem que pode ou não ajudar a identificar
     * o que causou morte do jogador.
     */
    public default void jogadorFaleceu(
        final int quemFoi,
        final String causaMortis){
    }

    /**
     * O {@link com.github.abdonia.domino.Jogador Jogador} retornou {@code null}
     * quando {@linkplain com.github.abdonia.domino.Jogador#joga() perguntado
     * qual seria sua jogada}. Mesmo no caso de não ter uma {@linkplain Pedra
     * pedra} pra jogar, o jogador não deve retornar {@code null}, e sim
     * {@linkplain com.github.abdonia.domino.Jogada#TOQUE tocar} explicitamente.
     *
     * <p>Isso é um erro grave (bug da implementação do jogador), e faz o jogo
     * ser abortado.</p>
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     */
    public default void jogadorJogouPedraNenhuma(final int quemFoi){
    }

    /**
     * O {@linkplain com.github.abdonia.domino.Jogador jogador} era quem
     * tinha a maior {@linkplain Pedra#isCarroca() carroça} (provavelmente o
     * {@linkplain Pedra#CARROCA_DE_SENA Dozão} na mão na primeira rodada da
     * primeira partida, mas começou o jogo {@linkplain
     * com.github.abdonia.domino.Jogada jogando} outra {@linkplain Pedra pedra}.
     *
     * <p>Esse jogador não sabe o básico de dominó. Não faz sentido continuar o
     * jogo.</p>
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     */
    public default void jogadorComecouErrando(final int quemFoi){
    }

    /**
     * O {@linkplain com.github.abdonia.domino.Jogador jogador} {@linkplain
     * com.github.abdonia.domino.Jogada#TOQUE tocou} quando tinha {@linkplain
     * Pedra pedras} na mão que poderiam ser {@linkplain
     * com.github.abdonia.domino.Jogada jogadas}. Isso é roubo (ou bug).
     *
     * <p>Apesar desse tipo de roubo ser comum na vida real, onde a pessoa faz
     * e ninguém percebe, o sistema é onisciente e não vai deixar isso rolar
     * aqui.</p>
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     */
    public default void jogadorTocouTendoPedraPraJogar(final int quemFoi){
    }

    /**
     * O {@linkplain com.github.abdonia.domino.Jogador jogador} jogou uma
     * {@linkplain Pedra pedra} que ele não tinha na mão. Isso é roubo (ou bug)
     * e faz o jogo ser abortado.
     *
     * @param quemFoi O jogador em questão (identificado pelo
     * {@linkplain com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     *
     * @param pedra a {@link Pedra} que o jogador tirou do bolso pra jogar.
     */
    public default void jogadorJogouPedraQueNaoTinha(
            final int quemFoi, final Pedra pedra){
    }
}
