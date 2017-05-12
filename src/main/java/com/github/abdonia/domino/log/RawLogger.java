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
package com.github.abdonia.domino.log;


import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.github.abdonia.domino.eventos.DominoEventListener;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Um {@link DominoEventListener} que loga o que aconteceu no jogo como uma 
 * sequência de caractéres mais apropriada para máquinas (e não humanos) lerem.
 * @author Bruno Abdon
 */
public class RawLogger implements OmniscientDominoEventListener{

    private List<LogPartida> logPartidas;
    
    private LogPartida logPartidaAtual;

    private final PrintStream printStream;

    /**
     * Cria uma instância que vai logar os {@link DominoEventListener eventos
     * do jogo} na {@link System#out saida padrão}.
     */
    public RawLogger(){
        this(System.out);
    }

    /**
     * Cria uma instância que vai logar os {@link DominoEventListener eventos
     * do jogo} na stream passada como parâmetro.
     * @param printStream uma stream onde devem ser logados os acontecimentos 
     * do jogo.
     */
    public RawLogger(final PrintStream printStream){
        this.printStream = printStream;
    }    

    private CharSequence formatEnum(Enum e) {
        return
            new StringBuilder(e.getDeclaringClass().getSimpleName())
                .append(".")
                .append(e.name());
    }
    
    class LogPartida{
        final Integer[] ordemOriginalPedras;
        final boolean foiDobrada;
        
        int placarFinalDupla1;
        int placarFinalDupla2;
        Vitoria tipoDeVitoria;
        Integer jogador;
        
        final Collection<CharSequence> pedrasJogadas = new LinkedList<>();
        final Collection<CharSequence> ladosJogadas = new LinkedList<>();
//        final Collection<Integer> escolhidos = new LinkedList<>();
        final Collection<Boolean> jogador1Comeca = new LinkedList<>();
        
        boolean empatou(){
            return jogador == null && tipoDeVitoria == null;
        }
        boolean bateram(){
            return jogador != null && tipoDeVitoria != null;
        };
        boolean voltou(){
            return jogador != null && tipoDeVitoria == null;
        };
                
        LogPartida(final boolean ehDobrada){
            this.ordemOriginalPedras = new Integer[28];
            this.foiDobrada= ehDobrada;
        }
        
        private <C extends  Collection<E>, E extends  Enum<E>> void printEnums(
            final StringBuilder sb,
            final String titulo,
            final C enums
            ){
            sb.append("\n")
                .append(titulo)
                .append(": ");
            
            enums.stream().forEach((e) -> {
                sb.append(e == null? "null":e.ordinal()).append(",");
            });
        }
        
        private void print() {
            
            final StrBuilder sb = new StrBuilder("//partida\n");
            sb.setNullText("null");
            
            sb.append("final int[] ordemPedras = new int[]{")
                .appendWithSeparators(this.ordemOriginalPedras, ",")
                .appendln("};");
            
            sb.append("final boolean[] jogador1Comeca = new boolean[]{")
                .appendWithSeparators(this.jogador1Comeca, ",")
                .appendln("};");
            
            sb.append("final Lado[] lados = new Lado[]{")
                .appendWithSeparators(this.ladosJogadas, ",")
                .appendln("};");
            
            sb.append("final Pedra[] pedras = new Pedra[]{")
                .appendWithSeparators(this.pedrasJogadas, ",")
                .appendln("};");
            
            sb.append("\nResultado: ");

            if(empatou()){
                sb.append("E");
            } else {
                sb.append(voltou()?"V":"B").append(" ").append(jogador);
            }  

            RawLogger.this.printStream.println(sb.toString());
        }
    }
    
    @Override
    public void jogoComecou(
            final String nomeDoJogador1,
            final String nomeDoJogador2,
            final String nomeDoJogador3,
            final String nomeDoJogador4) {
        this.logPartidas = new LinkedList<>();
    }

    @Override
    public void partidaComecou(
            final int placarDupla1, 
            final int placarDupla2, 
            final boolean ehDobrada) {
        if(this.logPartidaAtual != null){
            this.logPartidas.add(logPartidaAtual);
        }
        this.logPartidaAtual = new LogPartida(ehDobrada);
    }
    
    @Override
    public void jogadorRecebeuPedras(int quemFoi, Collection<Pedra> mao) {
        this.preenchePedaco((quemFoi-1) * 6, mao);
    }

    @Override
    public void dormeDefinido(Collection<Pedra> dorme) {
        this.preenchePedaco(24, dorme);
    }

    @Override
    public void jogoAcabou(int placarDupla1, int placarDupla2) {
        this.printStream.println("RAW LOG: ");
        logPartidas.stream().forEach(
            (logPartida) -> {
                logPartida.print();
        });
    }

    @Override
    public void partidaVoltou(int jogador) {
        this.logPartidaAtual.jogador = jogador;
    }

    @Override
    public void jogadorBateu(int quemFoi, Vitoria tipoDeVitoria) {
        this.logPartidaAtual.jogador = quemFoi;
        this.logPartidaAtual.tipoDeVitoria = tipoDeVitoria;
    }
    
    @Override
    public void jogadorTocou(int jogador) {
        this.logPartidaAtual.pedrasJogadas.add(null);
        this.logPartidaAtual.ladosJogadas.add(null);
    }

    @Override
    public void jogadorJogou(int jogador, Lado lado, Pedra pedra) {
        this.logPartidaAtual.pedrasJogadas.add(formatEnum(pedra));
        this.logPartidaAtual.ladosJogadas.add(formatEnum(lado));
    }

    @Override
    public void decididoQuemComeca(int jogador, boolean consentimentoMutuo) {
        if(!consentimentoMutuo) 
            this.logPartidaAtual
                .jogador1Comeca
                .add(jogador%2!=0);
    }
    
    private void preenchePedaco(int idx, Collection<Pedra> pedras){
        for (Pedra pedra : pedras) {
            this.logPartidaAtual.ordemOriginalPedras[idx++] = pedra.ordinal();
        }
    }    
}
