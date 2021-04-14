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
import java.util.Collection;
import java.util.List;

import com.github.abdonia.domino.Pedra;

/**
 * Métodos utilitários pra a implementação dos {@linkplain
 * com.github.abdonia.domino.Jogador jogadores} de exemplo.
 *
 * @author Bruno Abdon
 */
class JogadorUtils {

    private JogadorUtils() {
        super();
    }

    /**
     * Retorna qual é a {@linkplain Pedra#compareTo(Enum) maior} {@linkplain
     * Pedra#isCarroca() carroça} entre um conjunto de {@linkplain Pedra pedras}
     * ou lança {@link java.util.NoSuchElementException} caso não
     * exista uma carroça no conjunto.
     *
     * @param pedras um conjunto de {@linkplain Pedra pedras} de onde se quer
     * identificar a maior {@linkplain Pedra#isCarroca() carroça}.
     *
     * @return A maior {@linkplain Pedra#isCarroca() carroça} entre as
     * {@linkplain Pedra pedras} passsadas como parâmetro.
     *
     * @throws java.util.NoSuchElementException caso não exista uma {@linkplain
     * Pedra#isCarroca() carroça} entre as {@linkplain Pedra pedras} passsadas
     * como parâmetro.
     */
    public static Pedra aMaiorCarroca(final Collection<Pedra> pedras) {
        return pedras
                .parallelStream()
                .filter(Pedra::isCarroca)
                .max(Pedra::compareTo)
                .get();
    }
    /**
     * Constrói uma {@linkplain List lista} com as seis {@linkplain Pedra
     * pedras} passadas como parâmetro, na ordem em que foram passadas.
     *
     * @param pedra1 A primeira {@linkplain Pedra pedra}.
     * @param pedra2 A segunda {@linkplain Pedra pedra}.
     * @param pedra3 A terceira {@linkplain Pedra pedra}.
     * @param pedra4 A quarta {@linkplain Pedra pedra}.
     * @param pedra5 A quinta {@linkplain Pedra pedra}.
     * @param pedra6 A última {@linkplain Pedra pedra}.
     * @return Uma {@linkplain List lista} com as 6 {@linkplain Pedra pedras}.
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
