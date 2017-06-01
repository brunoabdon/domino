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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno Abdon
 */
public class ExMachina implements RandomGoddess{

    private static final Pedra[] TODAS = Pedra.values();
    
    //por desencargo. eh 28.
    private static final int QUANTAS_PEDRAS = TODAS.length;

    private int[] ordemPedras;
    private boolean[] jogador1Comeca;
    private int idxJogador1Comeca;

    public ExMachina(
            final int[] ordemPedras, 
            final boolean[] jogador1Comeca) {
        this();
        this.ordemPedras = ordemPedras;
        this.jogador1Comeca = jogador1Comeca;
    }

    public ExMachina() {
        this.idxJogador1Comeca = 0;
    }
    
    @Override
    public List<Pedra> embaralha() {
        final List<Pedra> pedras = new ArrayList<>(QUANTAS_PEDRAS);
        
        final int[] ordem = getOrdem();
        
        for (int i = 0; i < QUANTAS_PEDRAS; i++) {
            pedras.add(TODAS[ordem[i]]);
        }
        return pedras;
    }

    @Override
    public boolean jogador1Comeca() {
        return jogador1Comeca[idxJogador1Comeca++];
    }

    public int[] getOrdem() {
        return ordemPedras;
    }

    public void setOrdem(int[] ordem) {
        this.ordemPedras = ordem;
    }
}
