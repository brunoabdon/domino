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

import com.github.abdonia.domino.exemplos.JogadorMamao;

/**
 *
 * @author Bruno Abdon <brunoabdon+github@gmail.com>
 */
public class UtilsTests {

    public static JogadorWrapper makeJogador() {
        return makeJogador("bruno");
    }

    public static JogadorWrapper makeJogador(final String nome) {
        return new JogadorWrapper(new JogadorMamao(), nome);
    }
}
