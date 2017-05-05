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
package com.github.abdonia.domino.eventos;

import java.util.Collection;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;

/**
 * Interface para receber eventos com informações sigilosas que acontecem 
 * durante o jogo. Deve ser implementado por UIs, Loggers, etc. mas não por
 * jogadores.
 * 
 * @author bruno
 */
public interface OmniscientDominoEventListener extends DominoEventListener {

    /**
     * Avisa que, no início de uma partida, um derterminado {@link Jogador}
     * recebeu dadas {@link Pedra}s.
     * 
     * @param quemFoi O jogador em questão (identificado pelo número da 
     * cadeira).
     * 
     * @param pedras Uma coleção <em>não modificável</em> das 6 pedras que o 
     * jogador recebeu.
     */
    public default void jogadorRecebeuPedras(
            final int quemFoi, Collection<Pedra> pedras){
    }
    
    /**
     * As pedras foram distribuidas, e as quatro que sobraram foram pro dorme.
     * 
     * @param pedras As pedras que foram pro dorme.
     */
    public default void dormeDefinido(final Collection<Pedra> pedras){
        
    }
}
