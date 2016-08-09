package com.github.abdonia.domino.log;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;
import java.util.Collection;
import java.util.stream.Collectors;

import java.io.PrintStream;

import java.util.stream.Collector;

/**
 * Escuta tudo o que vai acontecendo no jogo e loga no {@link System#out}.
 * 
 * @author bruno
 */
public class LoggerDominoEventListener implements OmniscientDominoEventListener{

    private static final Collector<CharSequence, ?, String> JOINING_DORME = 
        Collectors.joining(" | ", "(Dorme: ", ")");
    
    private String[] nomeDosJogadores; 

    private final PrintStream printStream;

    private int contadorRecebimentoDePedra;

    private int baseDoPaddingDePedra;
    private int baseDoPaddingDeLado;
    private int baseDoPaddingDeTocToc;

    public LoggerDominoEventListener(){
        this(System.out);
    }

    public LoggerDominoEventListener(final PrintStream printStream){
        this.printStream = printStream;
    }

    @Override 
    public void partidaComecou(
            final int pontosDupla1, 
            final int pontosDupla2, 
            final boolean ehDobrada) {

        this.printStream.println("======================================");	
        this.printStream.println("Começando partida\n");
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

        this.printStream.print("-");	
        this.printStream.print(nomeCompanheiro);
        this.printStream.println(": Quer começar?");
        this.printStream.print("-");	
        this.printStream.print(nomeJogadorQueComecou);
        this.printStream.print(": Quero.\n-");
        this.printStream.print(nomeCompanheiro);
        
        if(consentimentoMutuo){
            this.printStream.println(": Vai la.");
        } else {
            this.printStream.print(": Eu tambem.\n[");
            this.printStream.print(nomeJogadorQueComecou);
            this.printStream.println(" escolhido aleatoriamente]");
        }
        imprimeUmaBarrinha();
    }


    @Override
    public void jogadorRecebeuPedras(
            final int quemFoi, 
            final Collection<Pedra> pedras) {
        this.printStream.println("Mão de " + nomeDosJogadores[quemFoi-1] + ":");
        pedras.stream().forEach(
            (pedra) -> printStream.println(formataPedra(pedra, 20)));

        contadorRecebimentoDePedra++;
        if(contadorRecebimentoDePedra == 4){
            imprimeUmaBarrinha();
            contadorRecebimentoDePedra = 0;
        }
    }

    @Override
    public void dormeDefinido(
            final Collection<Pedra> pedras) {
        printStream
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

        this.printStream.println(sb);
    }

    @Override
    public void jogadorTocou(final int jogador){
        final String nomeDoJogador = nomeDosJogadores[jogador-1];
        this.printStream.print(nomeDoJogador + ":");

        final int leftPad = baseDoPaddingDeTocToc-nomeDoJogador.length();
        final String strToc = 
            String.format("%1$" + leftPad + "s","\"toc toc\"");
        this.printStream.println(strToc);
    }

    @Override
    public void jogoComecou(
            final String nomeDoJogador1, final String nomeDoJogador2, 
            final String nomeDoJogador3, final String nomeDoJogador4){

            this.printStream.println("++++++++++++++++++++++++++++++++");    
            this.printStream.println("Comecou o jogo");

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
        this.printStream.println(
            nomeDosJogadores[0] + " e " + nomeDosJogadores[2]
            + " " + placarDupla1 + " x " + placarDupla2 + " " 
            + nomeDosJogadores[1] + " e " + nomeDosJogadores[3]);
    }

    @Override
    public void jogadorBateu(final int jogador, final Vitoria tipoDeVitoria) {
        final String nomeDoJogador = nomeDosJogadores[jogador-1];
        
        if(tipoDeVitoria == Vitoria.CONTAGEM_DE_PONTOS){
            this.printStream.print(
                "\nTravou. " + nomeDoJogador + " ganhou pela contagem.");
        } else if(tipoDeVitoria == Vitoria.SEIS_CARROCAS_NA_MAO){
            this.printStream.print(
                "\nCagada! " 
                + nomeDoJogador 
                + " tirou 6 carroças na mão! A Dupla ganha automaticamente.");
        } else {
            this.printStream.print("\n" + nomeDoJogador + " bateu!");
            if (tipoDeVitoria != Vitoria.BATIDA_SIMPLES) {
                this.printStream.println(" (" + tipoDeVitoria + ")");
            }
        }
        this.printStream.println();
    }

    @Override
    public void partidaEmpatou(){
        this.printStream.println("Empatou. A proxima vale dobrada.");   
    }
    
    @Override
    public void partidaVoltou(final int jogador) {
        this.printStream.println("Não vai ter partida! "
            + nomeDosJogadores[jogador-1] 
            + " tem 5 carroças na mão."
            + "\nVoltem as pedras...");   
    }
    
    @Override
    public void jogoAcabou(final int placarDupla1, final int placarDupla2) {

        this.printStream.println("Acabou!");
        imprimePlacar(placarDupla1,placarDupla2);

        final int min = 
            placarDupla1 < placarDupla2 
                ? placarDupla1 
                : placarDupla2;

        if(min == 0){
            imprimeUmaBarrona();
            for (int i = 0; i < 3; i++) {
                imprimeUmaBarrona();
                this.printStream.println("   =======     BUXUDINHA!!!   =====");
            }
            for (int i = 0; i < 2; i++) {
                imprimeUmaBarrona();
            }
        } else if (min == 1){
            imprimeUmaBarrona();
            this.printStream.println("   ======     INCHADINHA!   ======");
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
        this.printStream.println("=================");
    }

    private void imprimeUmaBarrona() {
        this.printStream.println("   ===================================");
    }
}