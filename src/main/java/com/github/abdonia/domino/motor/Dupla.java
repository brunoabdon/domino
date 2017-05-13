/*
 * Copyright (C) 2016 Bruno Abdon <brunoabdon+github@gmail.com>
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

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

class Dupla {
    private int pontos;

    private final JogadorWrapper jogador1;
    private final JogadorWrapper jogador2;

    Dupla(final JogadorWrapper jogador1, final JogadorWrapper jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;

        this.pontos = 0;
    }
    public JogadorWrapper getJogador1() {
        return jogador1;
    }
    public JogadorWrapper getJogador2() {
        return jogador2;
    }

    public int getPontos() {
        return pontos;
    }

    void adicionaPontos(final int pontos) {
        this.pontos += pontos;
    }

    boolean contem(final JogadorWrapper jogador){
        return this.jogador1 == jogador || this.jogador2 == jogador;
    }

    /**
     * Retorna um número negativo se o Jogador1 for começar, positivo se o 
     * Jogador 2 for começar, ou zero caso empatem na vontade.
     * 
     * @return O que eu acabei de dizer;
     * @throws BugDeJogadorException Se algum jogador se enrolar até pra dizer 
     * se quer começar ou não.
     */
    int quemComeca() throws BugDeJogadorException {
            final int vontadeDo1 = jogador1.vontadeDeComecar();
            validaVontade(vontadeDo1,jogador1);

            final int vontadeDo2 = jogador2.vontadeDeComecar();
            validaVontade(vontadeDo2,jogador2);
            return vontadeDo1 - vontadeDo2; 
    }

    private void validaVontade(final int vontade, final JogadorWrapper jogador) 
            throws BugDeJogadorException {
        if(vontade < 0 || vontade > 10){
            throw new BugDeJogadorException(
                    Falha.NAO_SABE_SE_COMECE,
                    jogador);
        }
    }
    
    boolean venceu() {
        return this.getPontos() >= 6;
    }

    @Override
    public String toString() {
        return this.jogador1 + " e "  + this.jogador2 + ", " + this.pontos;
    }
}