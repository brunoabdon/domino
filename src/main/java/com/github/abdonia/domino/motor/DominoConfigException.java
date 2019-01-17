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

import com.github.abdonia.domino.eventos.DominoEventListener;

/**
 * Uma {@link Exception exceção} indicando que aconteceu algum erro na 
 * tentativa de se criar um {@link Jogo} de dominó (não foram informados quais 
 * seriam os 4 jogadores, não foi possível instanciar a classe de um dos 
 * {@link DominoEventListener listeners} informados, etc...).
 * 
 * @author Bruno Abdon
 */
public class DominoConfigException extends Exception {

    private static final long serialVersionUID = -4270283101087959431L;

    /**
     * Construtor com mensagem de erro.
     * @param msg a mensagem de erro.
     */
    DominoConfigException(final String msg) {
        super(msg);
    }

    /**
     * Construtor com mensagem de erro 
     * {@linkplain String#format(String, Object...) parametrizada}.
     * 
     * @param msg a mensagem de erro.
     * @param params os parâmetros da mensagem.
     */
    DominoConfigException(final String msg, final Object ... params) {
        super(String.format(msg, params));
    }

    /**
     * Construtor com causa e {@linkplain String#format(String, Object...)
     * mensagem parametrizada} de erro.
     * 
     * @param msg a mensagem de erro.
     * @param causa a causa do erro.
     * @param params os parâmetros da mensagem
     */
    DominoConfigException(
            final Exception causa, 
            final String msg, 
            final Object ... params) {
        super(String.format(msg, params),causa);
    }
}
