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

import java.util.Iterator;

/**
 * A visão que um {@link Jogador} tem de como está a mesa a cada momento, ou 
 * seja, qual a lista de {@link Pedra pedras} nela e quantas pedras cada jogador
 * ainda tem na mão.
 * 
 * <p>As números expostos nas duas pontas da lista são referidas como {@link 
 * #getNumeroEsquerda() número da esquerda} e {@link #getNumeroDireita() número
 * da direita}, mas o nome é só uma convenção. Não tem nada a ver com pra que 
 * lado as pedras estão (isso não importa).</p>
 *   
 * @author Bruno Abdon
 */
public interface Mesa extends Iterable<Pedra>{

    /**
     * O {@link Numero} da cabeça da esquerda, ou {@code null} caso a mesa 
     * {@link #taVazia() esteja vazia}.
     * 
     * @return O número da cabeça da esquerda, ou {@code null} caso a mesa 
     * {@link #taVazia() esteja vazia}.
     */
    public Numero getNumeroEsquerda();

    /**
     * O {@link Numero} da cabeça da direita, ou {@code null} caso a mesa 
     * {@link #taVazia() esteja vazia}.
     * 
     * @return O número da cabeça da direita, ou {@code null} caso a mesa 
     * {@link #taVazia() esteja vazia}.
     */
    public Numero getNumeroDireita();

    /**
     * Um {@link Iterator} que permite percorer todas as {@link Pedra pedras} da 
     * mesa, no sentido {@link #getNumeroEsquerda() esquerda}-pra-{@link
     * #getNumeroDireita() direita}.
     * 
     * <p>O iterator será "read-only", ou seja, uma chamada a {@link
     * Iterator#remove()} é ilegal e vai causar {@link 
     * UnsupportedOperationException}.</p>
     * 
     * @return um {@link Iterator} pra ver as {@link Pedra pedras} desta mesa.
     */
    @Override
    public default Iterator<Pedra> iterator(){
        return iteratorEsquedaPraDireita();
    }

    /**
     * Um {@link Iterator} que permite percorer todas as {@link Pedra pedras} da 
     * mesa, no sentido {@link #getNumeroEsquerda() esquerda}-pra-{@link
     * #getNumeroDireita() direita}.
     * 
     * <p>O iterator será "read-only", ou seja, uma chamada a {@link
     * Iterator#remove()} é ilegal e vai causar {@link 
     * UnsupportedOperationException}.</p>
     * 
     * @return um {@link Iterator} pra ver as {@link Pedra pedras} desta mesa.
     */
    public Iterator<Pedra> iteratorEsquedaPraDireita();

    /**
     * Um {@link Iterator} que permite percorer todas as {@link Pedra pedras} da 
     * mesa, no sentido {@link #getNumeroDireita() direita}-pra-{@link
     * #getNumeroEsquerda() esquerda}.
     * 
     * <p>O iterator será "read-only", ou seja, uma chamada a {@link
     * Iterator#remove()} é ilegal e vai causar {@link 
     * UnsupportedOperationException}.</p>
     * 
     * @return um {@link Iterator} pra ver as {@link Pedra pedras} desta mesa.
     */
    public Iterator<Pedra> iteratorDireitaPraEsquerda();

    /**
     * Diz quantas {@link  Pedra pedras} um dado {@link Jogador} tem na mão no 
     * momento. <b>Importante:</b> Os jogadores são identificados pelos números
     * de 1 a 4 (e não de zero a três, como nerds esperariam), fazendo então que 
     * as duplas sejam <i>1 e 3</i> contra <i>2 e 4</i>. Todo jogador é avisado 
     * sobre a posição que esta sentado no início do jogo pelo método {@link 
     * Jogador#sentaNaMesa(Mesa,int)}.
     * 
     * @param qualJogador qual o jogador que se deseja saber (de 1 a 4).
     * 
     * @return Quantas pedras o jogador de indice {@code qualJogador} tem
     * na mão.
     * 
     * @throws IllegalArgumentException Se for passado um número que não seja 1,
     * 2, 3 nem 4.
     * 
     */
    public int quantasPedrasOJogadoresTem(int qualJogador);

    /**
     * Diz quantas {@link Pedra pedras} tem na mesa.
     * 
     * @return quantas  {@link Pedra pedras} tem na mesa.
     */
    public default int quantasPecas(){
        return toArray().length;
    }

    /**
     * Retorna a lista de {@link Pedra}s da mesa como um array, no sentido 
     * {@link #getNumeroEsquerda() esquerda}-pra-{@link #getNumeroDireita() 
     * direita}. O array pode ser modificado a vontade (isso não influenciará no
     * estado real da mesa).
     * 
     * @return a lista de {@link Pedra}s como um array.
     */
    public Pedra[] toArray();

    /**
     * Diz se a mesa está vazia (ou seja, deve-se jogar a primeira {@link Pedra
     * pedra} da partida);
     * 
     * @return {@code true} só se nao tiver {@link Pedra} na mesa.
     */
    public default boolean taVazia(){
        return this.quantasPecas() == 0;
    }
}