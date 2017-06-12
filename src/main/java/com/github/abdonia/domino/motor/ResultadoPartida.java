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

import com.github.abdonia.domino.Vitoria;

/**
 * O resultado final de uma {@link Partida}, dizendo se alguma {@link 
 * JogadorWrapper jogador} {@link Batida bateu}, se {@link #EMPATE empatou} ou 
 * se {@link Volta as pedras voltaram}.
 * 
 * @author Bruno Abdon
 */
class ResultadoPartida {
	
    private final JogadorWrapper jogadorRelevante;

    /**
     * Singleton que representa o resultado das partidas que terminam em 
     * emptate (por contagem de pontos).
     */
    public static final ResultadoPartida EMPATE = new ResultadoPartida();

    private ResultadoPartida() {
        this(null);
    }

    private ResultadoPartida(final JogadorWrapper jogadorRelevante) {
        this.jogadorRelevante = jogadorRelevante;
    }

    /**
     * O {@link JogadorWrapper jogador} que protagoniza esse fim de {@link 
     * Partida partida}, seja por que {@link Batida venceu} ou por que causou o 
     * {@link Volta cancelamento da partida}.
     * <p>Não existe um jogador relevante no caso de um {@linkplain #EMPATE 
     * empate}.
     * 
     * @return O {@link JogadorWrapper jogador} relevante pra esse resultado 
     * (que vai ser {@code null} no caso de um {@linkplain #EMPATE empate}).
     */
    JogadorWrapper getJogadorRelevante() {
        return jogadorRelevante;
    }

    /**
     * Um {@link ResultadoPartida} indicando que um determinado 
     * {@linkplain JogadorWrapper jogador} bateu.
     */
    public static final class Batida extends ResultadoPartida{

        private final Vitoria tipoDeVitoria;

        /**
         * Cria uma Batida indicando que dado {@link JogadorWrapper jogador} 
         * venceu a partida.
         * 
         * @param tipo Como foi a vitória.
         * @param vencedor Quem venceu.
         * 
         */
        public Batida(final Vitoria tipo, final JogadorWrapper vencedor) {
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
    
    /**
     * Um {@link ResultadoPartida} indicando que um determinado {@linkplain  
     * JogadorWrapper jogador} recebeu cinco carroças já de início, e a partida 
     * deve ser cancelada.
     */
    public static final class Volta extends ResultadoPartida{
        /**
         * Cria uma Volta indicando que dado {@link JogadorWrapper jogador} 
         * recebeu cinco {@linkplain com.github.abdonia.domino.Pedra#isCarroca() 
         * carroças} já de início.
         * 
         * @param jogadorComCincoCarrocas Quem tinha as 5 {@linkplain 
         * com.github.abdonia.domino.Pedra#isCarroca() carroças}.
         */
        public Volta(final JogadorWrapper jogadorComCincoCarrocas){
            super(jogadorComCincoCarrocas);
        }
    }
}
