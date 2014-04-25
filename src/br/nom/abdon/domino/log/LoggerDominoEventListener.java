package br.nom.abdon.domino.log;

import java.util.Collection;

import java.io.PrintStream;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.OmniscientDominoEventListener;

/**
 * Escuta tudo o que vai acontecendo no jogo e loga no {@link System#out}.
 * 
 * @author bruno
 */
public class LoggerDominoEventListener implements OmniscientDominoEventListener {

    private String nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4;

    private final PrintStream printStream;

    int contadorRecebimentoDePedra;

    public int baseDoPaddingDePedra;
    public int baseDoPaddingDeLado;
    public int baseDoPaddingDeTocToc;

    public LoggerDominoEventListener(){
        this(System.out);
    }

    public LoggerDominoEventListener(PrintStream printStream){
        this.printStream = printStream;
    }

    @Override 
    public void partidaComecou(int pontosDupla1, int pontosDupla2, boolean ehDobrada) {
            this.printStream.println("======================================");	
            this.printStream.println("Comecando partida\n");
            imprimePlacar(pontosDupla1,pontosDupla2);
            this.printStream.println("=================");	
    }

    @Override
    public void jogadorRecebeuPedras(String quemFoi, Collection<Pedra> pedras) {
        this.printStream.println("Mão de " + quemFoi + ":");
        pedras.stream().forEach((pedra)->printStream.println(formataPedra(pedra, 20)));

        contadorRecebimentoDePedra++;
        if(contadorRecebimentoDePedra == 4){
            this.printStream.println("=================");
            contadorRecebimentoDePedra = 0;
        }
    }


    @Override 
    public void jogadorJogou(String nomeDoJogador, Lado lado, Pedra pedra) {

        StringBuilder sb = new StringBuilder(nomeDoJogador).append(":");
        sb.append(formataPedra(pedra, baseDoPaddingDePedra-sb.length()));

        if(lado != null){
            int padLado = baseDoPaddingDeLado - sb.length();
            sb.append(String.format("%1$" + padLado + "s","(" + (lado == Lado.ESQUERDO?"E":"D") + ")"));
        }


        this.printStream.println(sb);

    }

    @Override
    public void jogadorTocou(String nomeDoJogador){
            this.printStream.print(nomeDoJogador + ":");
            int leftPad = baseDoPaddingDeTocToc-nomeDoJogador.length();
            String strToc = String.format("%1$" + leftPad + "s","\"toc toc\"");
            this.printStream.println(strToc);
    }

    @Override
    public void jogoComecou(String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4){

            this.printStream.println("Comecou o jogo");

            this.nomeDoJogador1 = nomeDoJogador1;
            this.nomeDoJogador2 = nomeDoJogador2;
            this.nomeDoJogador3 = nomeDoJogador3;
            this.nomeDoJogador4 = nomeDoJogador4;

            imprimePlacar(0,0);

            int maiorTamanhoDeNome = 
                    Math.max(nomeDoJogador1.length(), 
                            Math.max(nomeDoJogador2.length(), 
                                    Math.max(nomeDoJogador3.length(), 
                                            nomeDoJogador4.length())));

            this.baseDoPaddingDePedra = maiorTamanhoDeNome + 13;
            this.baseDoPaddingDeLado = baseDoPaddingDePedra + 13;
            this.baseDoPaddingDeTocToc = baseDoPaddingDePedra + 4;
    }

    private void imprimePlacar(int placarDupla1, int placarDupla2) {
        this.printStream.println(
                nomeDoJogador1 + " e " + nomeDoJogador3 
                + " " + placarDupla1 + " x " + placarDupla2 + " " 
                + nomeDoJogador2 + " e " + nomeDoJogador4);
    }

    @Override
    public void jogadorBateu(String nomeDoJogador, Vitoria tipoDeVitoria) {
        if(tipoDeVitoria == Vitoria.CONTAGEM_DE_PONTOS){
            this.printStream.print("\nTravou. " + nomeDoJogador + " ganhou pela contagem.");
        } else if(tipoDeVitoria == Vitoria.SEIS_CARROCAS_NA_MAO){
            this.printStream.print("\nCagada! " + nomeDoJogador + " tirou 6 carroças na mão! A Dupla ganha automaticamente.");
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
    public void partidaVoltou(String nomeDoJogador) {
        this.printStream.println("Não vai ter partida!"
                + nomeDoJogador 
                + " tem 5 carroças na mão."
                + "\nVoltem as pedras...");   
    }
    
    @Override
    public void jogoAcabou(int placarDupla1,int placarDupla2) {

        this.printStream.println("Acabou!");
        imprimePlacar(placarDupla1,placarDupla2);

        int min = placarDupla1 < placarDupla2 ? placarDupla1 : placarDupla2;

        if(min == 0){
            this.printStream.println("   =======================================   ");
            this.printStream.println("   =======================================   ");
            this.printStream.println("   ===========    BUXUDINHA!!!      ======   ");
            this.printStream.println("   =======================================   ");
            this.printStream.println("   ===========    BUXUDINHA!!!      ======   ");
            this.printStream.println("   =======================================   ");
            this.printStream.println("   ===========    BUXUDINHA!!!      ======   ");
            this.printStream.println("   =======================================   ");
            this.printStream.println("   =======================================   ");
        } else if (min == 1){
            this.printStream.println("   =======================================   ");
            this.printStream.println("   ===========    INCHADINHA!      ======   ");
            this.printStream.println("   =======================================   ");
        }
    }

    private String formataPedra(Pedra pedra, final int leftpadInicial) {
        final String pedraStr = pedra.toString();
        final int distaciaDaBarraProFim = pedraStr.substring(pedraStr.indexOf("|")).length();
        final int leftPadPedra = leftpadInicial + distaciaDaBarraProFim;
        return String.format("%1$" + leftPadPedra + "s", pedraStr);
    }
}