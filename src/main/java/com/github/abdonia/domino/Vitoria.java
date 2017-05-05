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
 * Os tipos de batida que dá pra se ganhar uma partida.
 * @author bruno
 */
public enum Vitoria {
    CONTAGEM_DE_PONTOS(1), 
    BATIDA_SIMPLES(1), 
    SEIS_CARROCAS_NA_MAO(1), 
    CARROCA(2), 
    LA_E_LO(3), 
    CRUZADA(4);

    private final int pontos;

    private Vitoria(final int pontos) {
        this.pontos = pontos;
    }

    /**
     * Quantos pontos vale essa vitória.
     * 
     * @return Quantos pontos vale a vitória.
     */
    public int getPontos(){
        return this.pontos;
    }
}
