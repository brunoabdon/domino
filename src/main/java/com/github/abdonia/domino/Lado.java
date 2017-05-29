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
 * Identifica as duas "<em>pontas</em>" da fila de dominós na mesa, por 
 * convenção como a "<em>ponta do lado esquerdo</em>" e a "<em>ponta do lado 
 * direito</em>". É apenas uma convenção: Poderia ser "<em>ponta de cima</em>" e
 * "<em>ponta de baixo</em>", "<em>primeira ponta</em>" e "<em>segunda
 * ponta</em>", etc.
 * 
 * @author Bruno Abdon
 */
public enum Lado {

    /**
     * Um dos lados da {@link Mesa} onde se pode coloca uma {@link Pedra}.
     */
    ESQUERDO,

    /**
     * Um dos lados da {@link Mesa} onde se pode coloca uma {@link Pedra}.
     */
    DIREITO
}
