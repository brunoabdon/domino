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

import com.github.abdonia.domino.Pedra;

/**
 * Gerador de eventos aleatórios durante a partida, responsável por embaralhar
 * as {@linkplain Pedra pedras} e por definir quem na {@linkplain Dupla dupla} 
 * deve começar uma {@linkplain Partida partida} em caso de empate na 
 * {@linkplain com.github.abdonia.domino.Jogador#getVontadeDeComecar() vontade 
 * demonstrada para começar a jogar}.
 * 
 * @author Bruno Abdon
 */
interface RandomGoddess {

    /**
     * Retorna um array contendo as 28 {@linkplain Pedra pedras} do dominó numa
     * ordem qualquer.
     * 
     * @return Um array das 28 {@link Pedra pedras}.
     * 
     */
    public Pedra[] embaralha();
    
    /**
     * Diz, aleatóriamente, se o primeiro {@linkplain JogadorWrapper jogador} 
     * da {@linkplain Dupla dupla} (e não o segundo) é quem deve {@linkplain 
     * JogadorWrapper#joga() fazer a primeira jogada} da  partida (em caso de 
     * empate na {@linkplain 
     * com.github.abdonia.domino.Jogador#getVontadeDeComecar() vontade 
     * demonstrada para começar a jogar}).
     * 
     * @return {@code true} se o primeiro jogador da {@linkplain Dupla dupla}
     * deve ser quem começa, ou {@code false} se é o segundo quem deve.
     */
    public boolean primeiroJogadorComeca();
    
}
