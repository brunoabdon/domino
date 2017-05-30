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
package com.github.abdonia.domino.exemplos;

import java.util.ArrayList;

import com.github.abdonia.domino.Pedra;

import java.util.Collection;
import java.util.List;

/**
 * Métodos utilitários pra a implementação dos {@link Jogador jogadores} de 
 * exemplo. 
 * 
 * @author Bruno Abdon
 */
class JogadorUtils {

    /**
     * Retorna qual é a {@link Pedra#compareTo(java.lang.Enum) maior} 
     * {@link Pedra#isCarroca() carroça} entre um conjunto de {@link Pedra 
     * pedras}, ou lança {@link java.util.NoSuchElementException} caso não 
     * exista uma carroça no conjunto.
     *  
     * @return A maior {@link Pedra#isCarroca() carroça} entre as {@link Pedra 
     * pedras} passsadas como parâmetro.
     * 
     * @throws java.util.NoSuchElementException caso não exista uma {@link 
     * Pedra#isCarroca() carroça} entre as {@link Pedra pedras} passsadas como 
     * parâmetro.
     */
    public static Pedra aMaiorCarroca(final Collection<Pedra> pedras) {
        return pedras
                .parallelStream()
                .filter(Pedra::isCarroca)
                .max(Pedra::compareTo)
                .get();
    }
    /**
     * Constrói uma {@link List lista} com as seis {@link Pedra pedras} 
     * passadas como parâmetro, na ordem em que foram passadas.
     * 
     * @param pedra1 A primeira {@link Pedra pedra}.
     * @param pedra2 A segunda {@link Pedra pedra}.
     * @param pedra3 A terceira {@link Pedra pedra}.
     * @param pedra4 A quarta {@link Pedra pedra}.
     * @param pedra5 A quinta {@link Pedra pedra}.
     * @param pedra6 A última {@link Pedra pedra}.
     * @return Uma {@link List lista} com as seis {@link Pedra pedras}.
     */
    public static List<Pedra> fazMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
     
        final List<Pedra> mao = new ArrayList<>(6);
        
        mao.add(pedra1);
        mao.add(pedra2);
        mao.add(pedra3);
        mao.add(pedra4);
        mao.add(pedra5);
        mao.add(pedra6);
        
        return mao;
    }
}
