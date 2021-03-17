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

import com.github.abdonia.domino.Pedra;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Implementação de {@link  RandomGoddess} baseada em {@link Random}.
 *
 * @author Bruno Abdon
 */
class DefaultRandomGoddess implements RandomGoddess{

    private static final Random RAND = new Random();

    @Override
    public Pedra[] embaralha() {
        final Pedra[] pedras = Pedra.values();
        Collections.shuffle(Arrays.asList(pedras),RAND);
        return pedras;
    }

    @Override
    public boolean primeiroJogadorComeca() {
        return RAND.nextBoolean();
    }
}
