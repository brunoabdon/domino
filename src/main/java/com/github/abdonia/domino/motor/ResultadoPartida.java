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

import com.github.abdonia.domino.Vitoria;

class ResultadoPartida {
	
    private final JogadorWrapper jogadorRelevante;

    public static final ResultadoPartida EMPATE = new ResultadoPartida();

    private ResultadoPartida() {
        this(null);
    }

    private ResultadoPartida(final JogadorWrapper jogadorRelevante) {
        this.jogadorRelevante = jogadorRelevante;
    }

    JogadorWrapper getJogadorRelevante() {
        return jogadorRelevante;
    }

    static ResultadoPartida.Volta volta(
            final JogadorWrapper jogadorComCincoCarrocas){
        return new Volta(jogadorComCincoCarrocas);
    }
    
    static Batida batida(final Vitoria tipo, final JogadorWrapper vencedor){
        return new Batida(tipo,vencedor);
    }
    
    /**
     * Um {@link ResultadoPartida} indicando que um determinado {@link Jogador} 
     * bateu.
     */
    public static final class Batida extends ResultadoPartida{

        private final Vitoria tipoDeVitoria;

        private Batida(final Vitoria tipo, final JogadorWrapper vencedor) {
            super(vencedor);
            this.tipoDeVitoria = tipo;
        }

        public JogadorWrapper getVencedor() {
            return super.getJogadorRelevante();
        }

        public Vitoria getTipoDeVitoria() {
            return tipoDeVitoria;
        }
    }
    
    public static final class Volta extends ResultadoPartida{
        private Volta(final JogadorWrapper jogadorComCincoCarrocas){
            super(jogadorComCincoCarrocas);
        }
    }
}
