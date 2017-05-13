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

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import java.util.List;

/**
 *
 * @author Bruno Abdon
 */
public class LogJogo {

    private final boolean[] jogador1Comeca;
    private final List<LogPartida> logPartidas; 
    
    public LogJogo(
            final boolean[] jogador1Comeca,
            final List<LogPartida> logPartidas){
        this.jogador1Comeca = jogador1Comeca;
        this.logPartidas = logPartidas;
    }
    
    public class LogPartida{
    
        private final int placarInicialDupla1;
        private final int placarInicialDupla2;
        private final Pedra[] ordemPedras;
        private final Jogada[] jogadas;
        public final boolean voltou;
        public final boolean empatou;
        public final Vitoria vitoria;
        
        public LogPartida(
                final int placarInicialDupla1,
                final int placarInicialDupla2,
                final Pedra[] ordemPedras,
                final Jogada[] jogadas,
                final boolean voltou,
                final boolean empatou,
                final Vitoria vitoria) {
            this.placarInicialDupla1 = placarInicialDupla1;
            this.placarInicialDupla2 = placarInicialDupla2;
            this.ordemPedras = ordemPedras;
            this.jogadas =  jogadas;
            this.voltou = voltou;
            this.empatou = empatou;
            this.vitoria = vitoria;
        }
    }

    public Iterator iterator(){
        return new Iterator(this);
    }
    
    public class Iterator{
        private int idxJogadas = 0;
        private final java.util.Iterator<LogPartida> partidasIterator;
        
        public LogPartida logPartida;
        private final LogJogo logJogo;

        public Iterator(final LogJogo logJogo) {
            this.logJogo = logJogo;
            this.partidasIterator = this.logJogo.logPartidas.iterator();
            this.logPartida = this.partidasIterator.next();
        }
        
        public Jogada nextJogada(){
            if(this.logPartida.jogadas.length == idxJogadas){
                this.logPartida = partidasIterator.next();
            }
            return this.logPartida.jogadas[idxJogadas++];
        }
    }
}