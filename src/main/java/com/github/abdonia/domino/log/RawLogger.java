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
package com.github.abdonia.domino.log;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.text.TextStringBuilder;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;


/**
 * Um {@link DominoEventListener} que loga o que aconteceu no jogo como uma
 * sequência de caractéres mais apropriada para máquinas (e não humanos) lerem.
 *
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

    private CharSequence formatEnum(Enum<?> e) {
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
        }
        boolean voltou(){
            return jogador != null && tipoDeVitoria == null;
        }

        LogPartida(final boolean ehDobrada){
            this.ordemOriginalPedras = new Integer[28];
            this.foiDobrada = ehDobrada;
        }

        private void print() {

            final TextStringBuilder sb = new TextStringBuilder("//partida\n");
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
    public void jogadorRecebeuPedras(
            final int quemFoi,
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        this.preenchePedaco(
                (quemFoi-1) * 6,
                Arrays.asList(pedra1,pedra2,pedra3,pedra4,pedra5,pedra6));
    }

    @Override
    public void dormeDefinido(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4) {
        this.preenchePedaco(24, Arrays.asList(pedra1,pedra2,pedra3,pedra4));
    }

    @Override
    public void jogoAcabou(final int placarDupla1, final int placarDupla2) {
        this.printStream.println("RAW LOG: ");
        logPartidas.stream().forEach(LogPartida::print);
    }

    @Override
    public void partidaVoltou(final int jogador) {
        this.logPartidaAtual.jogador = jogador;
    }

    @Override
    public void jogadorBateu(final int quemFoi, final Vitoria tipoDeVitoria) {
        this.logPartidaAtual.jogador = quemFoi;
        this.logPartidaAtual.tipoDeVitoria = tipoDeVitoria;
    }

    @Override
    public void jogadorTocou(final int jogador) {
        this.logPartidaAtual.pedrasJogadas.add(null);
        this.logPartidaAtual.ladosJogadas.add(null);
    }

    @Override
    public void jogadorJogou(
            final int jogador, final Lado lado, final Pedra pedra) {
        this.logPartidaAtual.pedrasJogadas.add(formatEnum(pedra));
        this.logPartidaAtual.ladosJogadas.add(formatEnum(lado));
    }

    @Override
    public void decididoQuemComeca(
            final int jogador,
            final boolean consentimentoMutuo) {
        if(!consentimentoMutuo)
            this.logPartidaAtual
                .jogador1Comeca
                .add(jogador%2!=0);
    }

    private void preenchePedaco(int idx, final Collection<Pedra> pedras){
        for (final Pedra pedra : pedras) {
            this.logPartidaAtual.ordemOriginalPedras[idx++] = pedra.ordinal();
        }
    }
}
