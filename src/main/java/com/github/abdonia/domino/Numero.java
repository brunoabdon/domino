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
    LIMPO(0), PIO(1), DUQUE(2), TERNO(3), QUADRA(4), QUINA(5), SENA(6);
	
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
