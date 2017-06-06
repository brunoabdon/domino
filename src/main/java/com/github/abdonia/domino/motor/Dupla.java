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

import com.github.abdonia.domino.Vontade;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

/**
 * Uma dupla de {@link JogadorWrapper jogadores} num jogo, com seu placar.
 * 
 * @author Bruno Abdon
 */
class Dupla {
    private int pontos;

    private final JogadorWrapper jogador1;
    private final JogadorWrapper jogador2;

    /**
     * Cria uma dupla com dois {@link JogadorWrapper jogadores}.
     * @param jogador1 O primeiro jogador da dupla.
     * @param jogador2 O segundo jogador da dupla.
     */
    Dupla(final JogadorWrapper jogador1, final JogadorWrapper jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;

        this.pontos = 0;
    }
    /**
     * Retorna o primeiro {@link JogadorWrapper jogador} da dupla.
     * @return o primeiro {@link JogadorWrapper jogador} da dupla.
     */
    public JogadorWrapper getJogador1() {
        return jogador1;
    }

    /**
     * Retorna o segundo {@link JogadorWrapper jogador} da dupla.
     * @return o segundo {@link JogadorWrapper jogador} da dupla.
     */
    public JogadorWrapper getJogador2() {
        return jogador2;
    }

    /**
     * Retorna quantos pontos essa dupla tem.
     * @return quantos pontos essa dupla tem.
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Adiciona uma dada quantidade de pontos ao placar da dupla.
     * @param pontos Quntos pontos adicionar.
     */
    void adicionaPontos(final int pontos) {
        this.pontos += pontos;
    }

    /**
     * Diz se um dado {@link JogadorWrapper jogadore} pertence a essa dupla.
     * @param jogador Um jogador.
     * @return {@code true} se e somente se este jogador
     * for um dos 2 jogadores da dupla.
     */
    boolean contem(final JogadorWrapper jogador){
        return this.jogador1 == jogador || this.jogador2 == jogador;
    }

    /**
     * Retorna um número positivo se o {@link #getJogador1() jogador1} for 
     * começar, negativo se o {@link #getJogador2() jogador2 } for começar, ou 
     * zero caso empatem na vontade.
     * 
     * @return um número positivo se o {@link #getJogador1() jogador1} for 
     * começar, negativo se o {@link #getJogador2() jogador2 } for começar, ou 
     * zero caso empatem na vontade.
     * 
     * @throws BugDeJogadorException Se algum jogador se enrolar até pra dizer 
     * se quer começar ou não (com a {@link BugDeJogadorException#getFalha() 
     * falha} {@link Falha#NAO_SABE_SE_COMECE}).
     */
    int quemComeca() throws BugDeJogadorException {
        final Vontade vontadeDo1 = perguntaVontade(jogador1);
        final Vontade vontadeDo2 = perguntaVontade(jogador2);

        return vontadeDo1.compareTo(vontadeDo2); 
    }

    /**
     * Pergunta ao dado {@link JogadorWrapper jogador} {@link 
     * JogadorWrapper#getVontadeDeComecar() qual a vontade dele começar a partida},
     * levantando {@link BugDeJogadorException} caso ele retorne 
     * {@code null}.
     * 
     * @param jogador Um jogador.
     * 
     * @return A {@link JogadorWrapper#getVontadeDeComecar() vontade dele de 
     * começar a partida} (caso ele responda corretamente com um valor não 
     * nulo).
     * @throws BugDeJogadorException caso o jogador retorne {@code null}
     * erroneamente.
     */
    private static Vontade perguntaVontade(
            final JogadorWrapper jogador) 
                throws BugDeJogadorException {

        final Vontade vontade = jogador.getVontadeDeComecar();

        if(vontade == null){
            throw new BugDeJogadorException(
                    Falha.NAO_SABE_SE_COMECE,
                    jogador);
        }
        
        return vontade;
    }
    
    /**
     * Diz se essa dupla já tem pelo menos 6 {@link #getPontos() pontos}.
     * @return 
     */
    boolean venceu() {
        return this.getPontos() >= 6;
    }

    @Override
    public String toString() {
        return this.jogador1 + " e "  + this.jogador2 + ", " + this.pontos;
    }
}