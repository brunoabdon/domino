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
 * Um jogador da partida. Interface que deve ser implementada pelas classes que
 * forem jogar.
 * 
 * @author bruno
 */
public interface Jogador {

    /**
     * O jogador toma uma das quatro posições pra jogar no inicio do Jogo.
     * <b>Importante:</b> Os jogadores são identificados pelos números de 1 a 4
     * (e não de 0 a 3, como nerds esperariam) no sentido anti-horário (fazendo
     * então que as duplas sejam <i>1 e 3</i> contra <i>2 e 4</i>).
     * 
     * Esta numeração é consistente com a usada em {@link 
     * Mesa#quantasPedrasOJogadoresTem(int)}.
     * 
     * @param mesa A mesa do jogo do dominó, de onde se poderá descobrir, na 
     * hora de {@link #joga() jogar}, que {@link Pedra}s estão dispostas, qual o
     * {@link Numero} aparece em cada {@link Lado} e quantas pedras cada 
     * {@link Jogador} tem na mão.
     * 
     * @param cadeiraQueSentou O número da cadeira em que o jogador se sentou 
     * (entre 1 e 4).
     */
    public void sentaNaMesa(Mesa mesa, int cadeiraQueSentou);

    /**
     * O jogador recebe sua mão: 6 {@link Pedra}s no início de cada partida.
     * 
     * @param pedra1 A primeira {@link Pedra pedra} da mão.
     * @param pedra2 A segunda {@link Pedra pedra} da mão.
     * @param pedra3 A terceira {@link Pedra pedra} da mão.
     * @param pedra4 A quarta {@link Pedra pedra} da mão.
     * @param pedra5 A quinta {@link Pedra pedra} da mão.
     * @param pedra6 A última {@link Pedra pedra} da mão.
     */
    public void recebeMao(
            Pedra pedra1,
            Pedra pedra2,
            Pedra pedra3,
            Pedra pedra4,
            Pedra pedra5,
            Pedra pedra6);

    /**
     * Está na vez deste jogador jogar. Deve retornar uma {@link Jogada} dizendo
     * qual peca quer jogar e de que {@link Lado} da mesa ela deve ser jogada.
     * 
     * Obviamente, o jogador deve ter {@link #recebeMao(Pedra[]) recebido esssa
     * pedra} nessa partida e não ter jogado ela ainda.
     * 
     * É responsabilidade do jogador saber que, se for a primeira rodada da
     * primeira partida, ele deve comecar com o {@link Pedra#CARROCA_DE_SENA
     * dozão}, ou {@link Pedra carroça de quina}, etc. (se o sistema disser que
     * este jogador deve ser o primeiro a jogar, então é certo que este jogador
     * é quem tem a maior carroça).

     * Para tocar, deve retornar o singleton {@link Jogada#TOQUE}. Retornar
     * <code>null</code> ou um pedra-beba cancela o jogo imediatamente.
     *  
     * @return A {@link Jogada} que o jogador decidiu fazer.
     */
    public Jogada joga();

    /**
     * Usado na primeira rodada de uma partida quando a dupla desse {@link
     * Jogador} ganhou a partida anterior. Um dos dois jogadores da dupla deve
     * fazer a primeira {@link Jogada}. Cada jogador deve dizer, através deste 
     * método, "<i>quanto ele quer ser o jogador a fazer a primeira jogada</i>".
     * O que que tiver mais vontade começa. Em caso de empate, um dois dois vai 
     * ser escolhido aleatoriamente.
     * 
     * @return A vontade deste jogador de ser o primeiro a jogar nessa partida.
     */
    public Vontade vontadeDeComecar();
}