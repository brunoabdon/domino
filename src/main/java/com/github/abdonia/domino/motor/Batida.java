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
import com.github.abdonia.domino.Vitoria;

/**
 * Um {@link ResultadoPartida} indicando que um determinado {@link Jogador} 
 * bateu.
 */
class Batida extends ResultadoPartida{

    private final Vitoria tipoDeVitoria;

    public Batida(final Vitoria tipoDeVitoria, final JogadorWrapper vencedor) {
        super(vencedor);
        this.tipoDeVitoria = tipoDeVitoria;
    }
    
    public JogadorWrapper getVencedor() {
        return super.getJogadorRelevante();
    }

    public Vitoria getTipoDeVitoria() {
        return tipoDeVitoria;
    }
}
