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

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;

class BugDeJogadorException extends Exception {
	
    private final Jogador jogadorBuguento;
    private final Pedra pedra;

    public BugDeJogadorException(
            final String msg, final Jogador jogadorBuguento) {
        this(msg,jogadorBuguento,null);
    }

    public BugDeJogadorException(
            final String msg, 
            final Jogador jogadorBuguento, 
            final Pedra pedra) {
        
        super(msg);
        this.jogadorBuguento = jogadorBuguento;
        this.pedra = pedra;
    }

    public Jogador getJogadorBuguento() {
        return jogadorBuguento;
    }

    public Pedra getPedra() {
        return pedra;
    }
}
