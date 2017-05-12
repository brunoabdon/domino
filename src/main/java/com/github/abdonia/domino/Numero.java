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
 * Os sete números que podem aparcer numa face de uma {@link Pedra} de dominó.
 * @author bruno
 */
public enum Numero {

    /**
     * A face sem nenhum ponto.
     */
    LIMPO(0),

    /**
     * A face com 1 ponto.
     */
    PIO(1),

    /**
     * A face com 2 pontos.
     */
    DUQUE(2),

    /**
     * A face com 3 pontos.
     */
    TERNO(3),

    /**
     * A face com 4 pontos.
     */
    QUADRA(4),

    /**
     * A face com 5 pontos.
     */
    QUINA(5),

    /**
     * A face com 6 pontos.
     */
    SENA(6);
	
    private final int numeroDePontos;

    private Numero(final int numeroDePontos) {
        this.numeroDePontos = numeroDePontos;
    }

    /**
     * Quantos pontinhos tem nesse número. Usado pra contar quantos pontos uma 
     * {@link Pedra} tem quando alguem tranca a {@link Mesa}.
     * 
     * @return Quantos pontinhos tem nesse número.
     */
    public int getNumeroDePontos() {
        return numeroDePontos;
    }
}
