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

package com.github.abdonia.domino.motor;

/**
 * Exceção genérica, que representa um erro durante um {@link Jogo} de dominó.
 * 
 * @author Bruno Abdon
 */
public abstract class DominoException extends Exception {

    /**
     * Construtor com causa e mensagem de erro.
     * @param msg a mensagem de erro.
     * @param causa a causa do erro.
     */
    public DominoException(final Exception causa, final String msg) {
        super(msg,causa);
    }

    /**
     * Construtor com mensagem de erro.
     * @param msg a mensagem de erro.
     */
    protected DominoException(String msg) {
        super(msg);
    }
}
