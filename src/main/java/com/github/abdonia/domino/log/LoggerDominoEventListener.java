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

import java.io.Console;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

/**
 * Um {@link DominoEventListener} que escuta tudo o que vai acontecendo no jogo
 * e logando em um {@link PrintWriter}.
 *
 * @author Bruno Abdon
 */
public class LoggerDominoEventListener implements OmniscientDominoEventListener{

    private static final Collector<CharSequence, ?, String> JOINING_DORME =
        Collectors.joining(" | ", "(Dorme: ", ")");

    private static final Function<Pedra,String> FORMATA_PEDRA_MAO =
        p -> formataPedra(p, 20);

    private static final UnaryOperator<String> MAO_DE =
        s -> "Mao de " + s + ":\n";

    private static final Comparator<Pedra> COMP_PEDRA_POR_MENOR_NUMERO =
        Comparator
            .comparing(Pedra::getPrimeiroNumero)
            .thenComparing(Pedra::getSegundoNumero);

    private String[] nomeDosJogadores;
    private String[] maoDeJogadores;

    private final PrintWriter printWriter;

    private int contadorRecebimentoDePedra;

    private int baseDoPaddingDePedra;
    private int baseDoPaddingDeLado;
    private int baseDoPaddingDeTocToc;

    /**
     * Cria uma instância que vai logar os {@linkplain DominoEventListener
     * eventos do jogo} no {@link Console} (caso um exista) ou na {@linkplain
     * System#out saida padrão}.
     */
    public LoggerDominoEventListener(){
        this(getDefaultPrintWritter());
    }

    /**
     * Cria uma instância que vai logar os {@linkplain DominoEventListener
     * eventos do jogo} no {@link OutputStream} passado como parâmetro.
     *
     * @param os  um stream onde devem ser logados os acontecimentos
     * do jogo.
     */
    public LoggerDominoEventListener(final OutputStream os){
        this(new PrintWriter(os,true));
    }

    /**
     * Cria uma instância que vai logar os {@linkplain  DominoEventListener
     * eventos do jogo} no {@link PrintWriter} passado como parâmetro.
     *
     * @param printWriter  uma wirter onde devem ser logados os acontecimentos
     * do jogo.
     */
    public LoggerDominoEventListener(final PrintWriter printWriter){
        this.printWriter = printWriter;
    }

    /**
     * Se o programa estiver rodando em um {@link Console}, retorna o
     * {@linkplain Console#writer() writer do console}. Caso contrário, retorna
     * um {@link PrintStream} que escreve na {@linkplain  System#out saída
     * padrão}, usando o {@linkplain  Charset#defaultCharset() charset default}
     * e com {@linkplain  PrintStream#flush() flush} automático.
     *
     * @return Um {@link PrintStream} apropriado pra ser usado caso nenhum seja
     * especificado;
     */
    private static PrintWriter getDefaultPrintWritter() {
        final Console console = System.console();
        return console != null
                ? new PrintWriter(console.writer(),true)
                : new PrintWriter(System.out,true);
    }

    @Override
    public void partidaComecou(
            final int pontosPrimeiraDupla,
            final int pontosSegundaDupla,
            final boolean ehDobrada) {

        imprimeUmaBarrinha();
        this.printWriter.println("Iniciando a partida\n");
        imprimePlacar(pontosPrimeiraDupla,pontosSegundaDupla);
        imprimeUmaBarrinha();
    }

    @Override
    public void decididoQuemComeca(
            final int jogador,
            final boolean consentimentoMutuo){

        final int companheiro = jogador ^ 1<<1;

        final String nomeJogadorQueComecou = nomeDosJogadores[jogador];
        final String nomeCompanheiro = nomeDosJogadores[companheiro];

        final StringBuilder dialogo =
            new StringBuilder("-")
            .append(nomeCompanheiro)
            .append(": Quer ser o primeiro?\n-")
            .append(nomeJogadorQueComecou)
            .append(": Quero.\n-")
            .append(nomeCompanheiro);

        if(consentimentoMutuo){
           dialogo.append(": Por mim, ok.");
        } else {
           dialogo
                .append(": Idem.\n[Decidido aleatoriamente que ")
                .append(nomeJogadorQueComecou)
                .append(" vai joga primeiro]");
        }
        this.printWriter.println(dialogo);
        imprimeUmaBarrinha();
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

        final Collector<CharSequence, ?, String> joiningMao =
            Collectors.joining("\n", this.maoDeJogadores[quemFoi], "");

        this.printWriter.println(
            asSortedStream(pedra1,pedra2,pedra3,pedra4,pedra5,pedra6)
                .map(FORMATA_PEDRA_MAO)
                .collect(joiningMao)
        );

        if(++this.contadorRecebimentoDePedra == 4){
            imprimeUmaBarrinha();
            contadorRecebimentoDePedra = 0;
        }
    }

    @Override
    public void dormeDefinido(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4) {
        this.printWriter
            .println(
                asSortedStream(pedra1,pedra2,pedra3,pedra4)
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
                new StringBuilder(nomeDosJogadores[jogador])
                .append(":");
        sb.append(formataPedra(pedra, baseDoPaddingDePedra-sb.length()));

        if(lado != null){
            final int padLado = baseDoPaddingDeLado - sb.length();
            sb.append(
                String.format(
                    "%1$" + padLado + "s",
                    "(" + (lado == Lado.ESQUERDO?"ESQ":"DIR") + ")"));
        }

        this.printWriter.println(sb);
    }

    @Override
    public void jogadorTocou(final int jogador){
        final String nomeDoJogador = nomeDosJogadores[jogador];
        this.printWriter.print(nomeDoJogador + ":");

        final int leftPad = baseDoPaddingDeTocToc-nomeDoJogador.length();

        this.printWriter
            .println(String.format("%1$" + leftPad + "s","\"toc toc\""));
    }

    @Override
    public void jogoComecou(
            final String nomeDoJogador0, final String nomeDoJogador1,
            final String nomeDoJogador2, final String nomeDoJogador3){

            this.printWriter.println("++++++++++++++++++++++++++++++++");
            this.printWriter.println("Jogo iniciado");

            this.nomeDosJogadores =
                new String[]{
                    nomeDoJogador0,
                    nomeDoJogador1,
                    nomeDoJogador2,
                    nomeDoJogador3};

            this.maoDeJogadores =
                Stream
                    .of(this.nomeDosJogadores)
                    .map(MAO_DE)
                    .toArray(String[]::new);

            imprimePlacar(0,0);

            final int maiorTamanhoDeNome =
                Stream.of(this.nomeDosJogadores)
                        .map(String::length)
                        .max(Integer::compare)
                        .get();

            this.baseDoPaddingDePedra = maiorTamanhoDeNome + 13;
            this.baseDoPaddingDeLado = baseDoPaddingDePedra + 15;
            this.baseDoPaddingDeTocToc = baseDoPaddingDePedra + 4;
    }

    private void imprimePlacar(
            final int placarPrimeiraDupla, final int placarSegundaDupla) {
        this.printWriter.println(
            nomeDosJogadores[0] + " e " + nomeDosJogadores[2]
            + " " + placarPrimeiraDupla + " x " + placarSegundaDupla + " "
            + nomeDosJogadores[1] + " e " + nomeDosJogadores[3]);
    }

    @Override
    public void jogadorBateu(final int jogador, final Vitoria tipoDeVitoria) {
        final String nomeDoJogador = nomeDosJogadores[jogador];

        if(tipoDeVitoria == Vitoria.CONTAGEM_DE_PONTOS){
            this.printWriter.print(
                "\nTravou. " + nomeDoJogador + " ganhou pela contagem.");
        } else if(tipoDeVitoria == Vitoria.SEIS_CARROCAS_NA_MAO){
            this.printWriter.print(
                "\nCagada! "
                + nomeDoJogador
                + " tirou 6 carrocas na mao! A Dupla ganha automaticamente.");
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
        this.printWriter.println("Empatou. A partida que vem vale dobrada.");
    }

    @Override
    public void partidaVoltou(final int jogador) {
        this.printWriter.println("Partida cancelada! "
            + nomeDosJogadores[jogador]
            + " tem 5 carroças na mão."
            + "\nVoltem as pedras...");
    }

    @Override
    public void jogoAcabou(
            final int placarPrimeiraDupla, final int placarSegundaDupla) {

        this.printWriter.println("Acabou!");
        imprimePlacar(placarPrimeiraDupla,placarSegundaDupla);

        final int min =
            placarPrimeiraDupla < placarSegundaDupla
                ? placarPrimeiraDupla
                : placarSegundaDupla;

        if(min == 0){
            imprimeUmaBarrona();
            for (int i = 0; i < 3; i++) {
                imprimeUmaBarrona();
                this.printWriter.println("   =======     BUXUDINHA!!!   ====");
            }
            for (int i = 0; i < 2; i++) {
                imprimeUmaBarrona();
            }
        } else if (min == 1){
            imprimeUmaBarrona();
            this.printWriter.println("   ======     INCHADINHA!   ======");
            imprimeUmaBarrona();
        }
        this.printWriter.flush();
    }


    @Override
    public void jogadorJogouPedraInvalida(
            final int quemFoi,
            final Pedra pedra,
            final Numero numero) {

        final String msg =
            new StringBuilder(nomeDosJogadores[quemFoi])
            .append(" quis jogar ")
            .append(pedra)
            .append(" na ponta que era de ")
            .append(numero)
            .toString();

        imprimeErroFatal(msg,false);
    }

    @Override
    public void jogadorErrouVontadeDeComecar(int quemFoi) {
        final String msg =
            new StringBuilder(nomeDosJogadores[quemFoi])
            .append(", tens que escolher um número de 0 a 10.")
            .toString();

        imprimeErroFatal(msg,false);
    }

    @Override
    public void jogadorFaleceu(final int quemFoi, final String causaMortis) {
        final String msg =
            new StringBuilder("GRAVE: ")
            .append(nomeDosJogadores[quemFoi])
            .append(" veio a falecer.\nCausa mortis: ")
            .append(causaMortis)
            .toString();

        imprimeErroFatal(msg,false);
    }

    @Override
    public void jogadorJogouPedraNenhuma(int quemFoi) {
        final String msg =
            new StringBuilder(nomeDosJogadores[quemFoi])
            .append(" jogou pedra nenhuma.")
            .toString();

        imprimeErroFatal(msg,false);
    }

    @Override
    public void jogadorComecouErrando(int quemFoi) {
        final String msg =
            new StringBuilder("O jogo abre com a maior carroca, ")
            .append(nomeDosJogadores[quemFoi])
            .append(".")
            .toString();

        imprimeErroFatal(msg,false);
    }

    @Override
    public void jogadorTocouTendoPedraPraJogar(int quemFoi) {
        final String msg =
            new StringBuilder(nomeDosJogadores[quemFoi])
            .append(" disse que tocou, mas tem pedra pra jogar.")
            .toString();

        imprimeErroFatal(msg,true);
    }

    @Override
    public void jogadorJogouPedraQueNaoTinha(int quemFoi, Pedra pedra) {
        final String msg =
            new StringBuilder(nomeDosJogadores[quemFoi])
            .append(" jogou ")
            .append(pedra)
            .append(" sem ter essa pedra na mao. Tirou do bolso?")
            .toString();

        imprimeErroFatal(msg,true);
    }

    private static String formataPedra(
            final Pedra pedra, final int leftpadInicial) {

        final String pedraStr = pedra.toString();

        final int distaciaDaBarraProFim =
            pedraStr.substring(pedraStr.indexOf("|")).length();

        final int leftPadPedra = leftpadInicial + distaciaDaBarraProFim;

        return String.format("%1$" + leftPadPedra + "s", pedraStr);
    }

    private void imprimeUmaBarrinha() {
        this.printWriter.println("=============");
    }

    private void imprimeUmaBarrona() {
        this.printWriter.println("   ===============================");
    }

    private void imprimeErroFatal(final String msg, boolean foiRoubo){
        imprimeUmaBarrinhaDeErro();
        this.printWriter.println("              Bug de Jogador!\n");
        this.printWriter.println(msg);
        this.printWriter.print("\nJogo cancelado. ");
        this.printWriter.println(
                foiRoubo
                    ? "Deixe de roubo."
                    : "Aprenda as regras."
        );
        imprimeUmaBarrinhaDeErro();

    }
    private void imprimeUmaBarrinhaDeErro() {
        this.printWriter.println("\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    }

    private static Stream<Pedra> asSortedStream(final Pedra ... pedras){
        return
            Arrays.asList(pedras).stream().sorted(COMP_PEDRA_POR_MENOR_NUMERO);
    }
}
