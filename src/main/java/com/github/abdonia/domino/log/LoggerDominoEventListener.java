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
package com.github.abdonia.domino.log;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

import java.util.Collection;
import java.util.stream.Collectors;

import java.io.PrintStream;

import java.util.stream.Collector;

import com.github.abdonia.domino.eventos.DominoEventListener;
import java.io.Console;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Escuta tudo o que vai acontecendo no jogo e loga em um {@link PrintWriter}.
 * 
 * @author Bruno Abdon
 */
public class LoggerDominoEventListener implements OmniscientDominoEventListener{

    private static final Collector<CharSequence, ?, String> JOINING_DORME = 
        Collectors.joining(" | ", "(Dorme: ", ")");
    
    private String[] nomeDosJogadores; 

    private final PrintWriter printWriter;

    private int contadorRecebimentoDePedra;

    private int baseDoPaddingDePedra;
    private int baseDoPaddingDeLado;
    private int baseDoPaddingDeTocToc;

    /**
     * Cria uma instância que vai logar os {@link DominoEventListener eventos
     * do jogo} no {@link Console} (caso um exista) ou na {@link System#out 
     * saida padrão}.
     */
    public LoggerDominoEventListener(){
        this(getDefaultPrintWritter());
    }

    /**
     * Cria uma instância que vai logar os {@link DominoEventListener eventos
     * do jogo} no {@link OutputStream} passado como parâmetro.
     * 
     * @param os  um stream onde devem ser logados os acontecimentos 
     * do jogo.
     */
    public LoggerDominoEventListener(final OutputStream os){
        this(new PrintWriter(os,true));
    }
    
    /**
     * Cria uma instância que vai logar os {@link DominoEventListener eventos
     * do jogo} no {@link PrintWriter} passado como parâmetro.
     * 
     * @param printWriter  uma wirter onde devem ser logados os acontecimentos 
     * do jogo.
     */
    public LoggerDominoEventListener(final PrintWriter printWriter){
        this.printWriter = printWriter;
    }

    /**
     * Se o programa estiver rodado em um {@link Console}, retorna o 
     * {@link Console#writer() writer do console}. Caso contrário, retorna um
     * {@link PrintStream} que escreve na {@link System#out saída padrão}, 
     * usando o {@link Charset#defaultCharset() charset default} e com  
     * {@link PrintStream#flush() flush} automático.
     * 
     * @return Um {@link PrintStream} apropriado pra ser usado caso nenhum seja
     * especificado;
     */
    private static PrintWriter getDefaultPrintWritter() {
        final Console console = System.console();
        return console != null
                ? console.writer()
                : new PrintWriter(System.out,true);
    }
    
    @Override 
    public void partidaComecou(
            final int pontosDupla1, 
            final int pontosDupla2, 
            final boolean ehDobrada) {

        imprimeUmaBarrinha();
        this.printWriter.println("Começando partida\n");
        imprimePlacar(pontosDupla1,pontosDupla2);
        imprimeUmaBarrinha();	
    }
    
    @Override
    public void decididoQuemComeca(
            final int jogador, 
            final boolean consentimentoMutuo){
        int companheiro = jogador + (jogador < 3 ? 2 : -2);
        
        final String nomeJogadorQueComecou = nomeDosJogadores[jogador-1];
        final String nomeCompanheiro = nomeDosJogadores[companheiro-1];

        this.printWriter.print("-");	
        this.printWriter.print(nomeCompanheiro);
        this.printWriter.println(": Quer começar?");
        this.printWriter.print("-");	
        this.printWriter.print(nomeJogadorQueComecou);
        this.printWriter.print(": Quero.\n-");
        this.printWriter.print(nomeCompanheiro);
        
        if(consentimentoMutuo){
            this.printWriter.println(": Vai la.");
        } else {
            this.printWriter.print(": Eu tambem.\n[");
            this.printWriter.print(nomeJogadorQueComecou);
            this.printWriter.println(" escolhido aleatoriamente]");
        }
        imprimeUmaBarrinha();
    }


    @Override
    public void jogadorRecebeuPedras(
            final int quemFoi, 
            final Collection<Pedra> pedras) {
        this.printWriter.println("Mão de " + nomeDosJogadores[quemFoi-1] + ":");
        pedras.stream().forEach(
            (pedra) -> printWriter.println(formataPedra(pedra, 20)));

        contadorRecebimentoDePedra++;
        if(contadorRecebimentoDePedra == 4){
            imprimeUmaBarrinha();
            contadorRecebimentoDePedra = 0;
        }
    }

    @Override
    public void dormeDefinido(
            final Collection<Pedra> pedras) {
        this.printWriter
            .println(
                pedras
                .stream()
                .map(Object::toString)
                .collect(JOINING_DORME));
        imprimeUmaBarrinha();
    }

    @Override 
    public void jogadorJogou(
            final int jogador, 
            final Lado lado, 
            final Pedra pedra) {

        final StringBuilder sb = 
                new StringBuilder(nomeDosJogadores[jogador-1])
                .append(":");
        sb.append(formataPedra(pedra, baseDoPaddingDePedra-sb.length()));

        if(lado != null){
            final int padLado = baseDoPaddingDeLado - sb.length();
            sb.append(
                String.format(
                    "%1$" + padLado + "s",
                    "(" + (lado == Lado.ESQUERDO?"E":"D") + ")"));
        }

        this.printWriter.println(sb);
    }

    @Override
    public void jogadorTocou(final int jogador){
        final String nomeDoJogador = nomeDosJogadores[jogador-1];
        this.printWriter.print(nomeDoJogador + ":");

        final int leftPad = baseDoPaddingDeTocToc-nomeDoJogador.length();
        final String strToc = 
            String.format("%1$" + leftPad + "s","\"toc toc\"");
        this.printWriter.println(strToc);
    }

    @Override
    public void jogoComecou(
            final String nomeDoJogador1, final String nomeDoJogador2, 
            final String nomeDoJogador3, final String nomeDoJogador4){

            this.printWriter.println("++++++++++++++++++++++++++++++++");    
            this.printWriter.println("Começou o jogo");

            this.nomeDosJogadores = 
                new String[]{
                    nomeDoJogador1,
                    nomeDoJogador2,
                    nomeDoJogador3,
                    nomeDoJogador4};

            imprimePlacar(0,0);

            final int maiorTamanhoDeNome = 
                Math.max(nomeDoJogador1.length(), 
                    Math.max(nomeDoJogador2.length(), 
                        Math.max(nomeDoJogador3.length(), 
                            nomeDoJogador4.length())));

            this.baseDoPaddingDePedra = maiorTamanhoDeNome + 13;
            this.baseDoPaddingDeLado = baseDoPaddingDePedra + 13;
            this.baseDoPaddingDeTocToc = baseDoPaddingDePedra + 4;
    }

    private void imprimePlacar(final int placarDupla1, final int placarDupla2) {
        this.printWriter.println(
            nomeDosJogadores[0] + " e " + nomeDosJogadores[2]
            + " " + placarDupla1 + " x " + placarDupla2 + " " 
            + nomeDosJogadores[1] + " e " + nomeDosJogadores[3]);
    }

    @Override
    public void jogadorBateu(final int jogador, final Vitoria tipoDeVitoria) {
        final String nomeDoJogador = nomeDosJogadores[jogador-1];
        
        if(tipoDeVitoria == Vitoria.CONTAGEM_DE_PONTOS){
            this.printWriter.print(
                "\nTravou. " + nomeDoJogador + " ganhou pela contagem.");
        } else if(tipoDeVitoria == Vitoria.SEIS_CARROCAS_NA_MAO){
            this.printWriter.print(
                "\nCagada! " 
                + nomeDoJogador 
                + " tirou 6 carroças na mão! A Dupla ganha automaticamente.");
        } else {
            this.printWriter.print("\n" + nomeDoJogador + " bateu!");
            if (tipoDeVitoria != Vitoria.BATIDA_SIMPLES) {
                this.printWriter.println(" (" + tipoDeVitoria + ")");
            }
        }
        this.printWriter.println();
    }

    @Override
    public void partidaEmpatou(){
        this.printWriter.println("Empatou. A próxima vale dobrada.");   
    }
    
    @Override
    public void partidaVoltou(final int jogador) {
        this.printWriter.println("Não vai ter partida! "
            + nomeDosJogadores[jogador-1] 
            + " tem 5 carroças na mão."
            + "\nVoltem as pedras...");   
    }
    
    @Override
    public void jogoAcabou(final int placarDupla1, final int placarDupla2) {

        this.printWriter.println("Acabou!");
        imprimePlacar(placarDupla1,placarDupla2);

        final int min = 
            placarDupla1 < placarDupla2 
                ? placarDupla1 
                : placarDupla2;

        if(min == 0){
            imprimeUmaBarrona();
            for (int i = 0; i < 3; i++) {
                imprimeUmaBarrona();
                this.printWriter.println("   =======     BUXUDINHA!!!   =====");
            }
            for (int i = 0; i < 2; i++) {
                imprimeUmaBarrona();
            }
        } else if (min == 1){
            imprimeUmaBarrona();
            this.printWriter.println("   ======     INCHADINHA!   ======");
            imprimeUmaBarrona();
        }
    }

    private String formataPedra(Pedra pedra, final int leftpadInicial) {
        final String pedraStr = pedra.toString();
        
        final int distaciaDaBarraProFim = 
            pedraStr.substring(pedraStr.indexOf("|")).length();
        
        final int leftPadPedra = leftpadInicial + distaciaDaBarraProFim;
        
        return String.format("%1$" + leftPadPedra + "s", pedraStr);
    }
    
    private void imprimeUmaBarrinha() {
        this.printWriter.println("=================");
    }

    private void imprimeUmaBarrona() {
        this.printWriter.println("   ===================================");
    }
}