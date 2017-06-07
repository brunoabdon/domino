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

import java.util.List;

/**
 * A visão que um {@link Jogador} tem de como está a mesa a cada momento, ou 
 * seja, qual a lista de {@linkplain Pedra pedras} nela e quantas pedras cada 
 * jogador ainda tem na mão.
 * 
 * <p>As números expostos nas duas pontas da lista são referidas como {@link 
 * #getNumeroEsquerda() número da esquerda} e {@link #getNumeroDireita() número
 * da direita}, mas o nome é só uma convenção. Não tem nada a ver com pra que 
 * lado as pedras estão (isso não importa).</p>
 *   
 * @author Bruno Abdon
 */
public interface Mesa {

    /**
     * O {@link Numero} da cabeça da esquerda, ou {@code null} caso não existam
     * {@linkplain #getPedras() pedras na mesa}.
     * 
     * @return O número da cabeça da esquerda, ou {@code null} caso não existam
     * {@linkplain #getPedras() pedras na mesa}.
     */
    public Numero getNumeroEsquerda();

    /**
     * O {@link Numero} da cabeça da direita, ou {@code null} caso não existam
     * {@linkplain #getPedras() pedras na mesa}.
     * 
     * @return O número da cabeça da direita, ou {@code null} caso não existam
     * {@linkplain #getPedras() pedras na mesa}.
     */
    public Numero getNumeroDireita();
    
    /**
     * Retorna uma visão somente-leitura da {@linkplain List lista} de 
     * {@linkplain Pedra pedras} na mesa, na ordem da {@linkplain 
     * #getNumeroEsquerda() esquerda} pra {@linkplain #getNumeroDireita() 
     * direita}. 
     * <p>Tentativas de alterar a lista direta ou indiretamene (através de um
     * {@link List#iterator() iterator}) vão resultar em {@link 
     * UnsupportedOperationException}.</p>
     * <p>Durate uma partida, é garantido que será retornada sempre a mesa 
     * instância da lista, que vai sempre refletir situação atual dessa mesa. 
     * Enquanto a referência pra a lista pode ser reutilizada, os {@link 
     * List#iterator() iterators dessa lista} deixam de funcionar se houver uma
     * aleração à lista de pedras.</p>
     * 
     * @return A lista de {@linkplain Pedra pedras} na mesa.
     */
    public List<Pedra> getPedras();

    /**
     * Diz quantas {@linkplain Pedra pedras} um dado {@link Jogador} tem na mão
     * no momento. Os jogadores são identificados pelos números de 0 a 3, 
     * fazendo então que as duplas sejam <i>0 e 2</i> contra <i>1 e 3</i>. Todo 
     * jogador é avisado sobre a posição que esta sentado no início do jogo pelo 
     * método {@link Jogador#sentaNaMesa(Mesa,int)}.
     * 
     * @param qualJogador qual o jogador que se deseja saber (de 0 a 3).
     * 
     * @return Quantas pedras o jogador de indice {@code qualJogador} tem
     * na mão.
     * 
     * @throws IllegalArgumentException Se for passado um número que não seja 0, 
     * 1, 2 nem 3.
     * 
     */
    public int getQuantidadeDePedrasDoJogador(int qualJogador);
}