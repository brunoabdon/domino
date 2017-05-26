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
 * O quanto um {@link Jogador jogador} deseja ser o primeiro de sua dupla a 
 * jogar (quando sua dupla foi a vencedora da partida anterior).
 * 
 * <p>Com a exceção da primeira partida do jogo, que é iniciada pelo {@link 
 * Jogador} que tiver a {@link Pedra#CARROCA_DE_SENA carroça de sena}, cada 
 * partida é iniciada por um dos jogadores da dupla que venceu a partida 
 * anterior. Pra decidir qual dos dois vai começar, cada um deles, depois de 
 * {@link Jogador#recebeMao(com.github.abdonia.domino.Pedra[]) receber suas 
 * pedras}, vai ser {@link Jogador#vontadeDeComecar() perguntado "o quanto ele 
 * deseja começar a partida"}. O jogador da dupla que tiver mais vontade começa. 
 * Caso aconteça um empate, um dos dois vai ser escolhido aleatóriamente.
 * </p>
 * 
 * @author Bruno Abdon
 */
public enum Vontade {

    /**
     * É muito importante pra o {@link Jogador} que não seja ele o membro da 
     * dupla a iniciar a partida.
     */
    NAO_QUERO_MESMO,

    /**
     * O {@link Jogador} prefere que não seja ele o membro da dupla a iniciar a 
     * partida.
     */
    NAO_QUERO,

    /**
     * O {@link Jogador} não se importa se sera ele ou seu parceiro que irá 
     * inciar a partida.
     */
    TANTO_FAZ,

    /**
     * O {@link Jogador} prefere que seja ele o membro da dupla a iniciar a 
     * partida.
     */
    QUERO,

    /**
     * É muito importante pra o {@link Jogador} que seja ele o membro da dupla a 
     * iniciar a partida.
     */
    QUERO_MUITO;
}
