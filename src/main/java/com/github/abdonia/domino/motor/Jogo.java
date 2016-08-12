package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import java.util.Random;

public class Jogo {

    private final MesaImpl mesa;
    private final DominoEventBroadcaster eventBroadcaster;
    private final RandomGoddess fortuna;
    
    /**
     * Cria um jogo de dominó com os jogadores passados sentados em ordem na 
     * mesa (duplas 0 e 2 contra 1 e 3), onde a classe {@link Random} é usada
     * pra gerar a aleatóriedade (embaralhar as pedreas e decidir quem da dupla 
     * começa uma partida, se empatarem na {@link Jogador#vontadeDeComecar() 
     * vontade demonstrada para começar a jogar}.
     * 
     * @param jogador1dupla1 O primeiro jogador da primeira dupla.
     * @param jogador1dupla2 O primeiro jogador da segunda dupla.
     * @param jogador2dupla1 O segundo jogador da primeira dupla.
     * @param jogador2dupla2 O segundo jogador da segunda dupla.
     */
    public Jogo(
        final JogadorWrapper jogador1dupla1, 
        final JogadorWrapper jogador1dupla2, 
        final JogadorWrapper jogador2dupla1, 
        final JogadorWrapper jogador2dupla2) {
            this(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2, 
                new DefaultRandomGoddess());
    }
    
    /**
     * Cria um jogo de dominó com os jogadores passados sentados em ordem na 
     * mesa (duplas 0 e 2 contra 1 e 3), onde a aleatóriedade é gerada pelo
     * gerador randômico passado. 
     * 
     * Aa {@link RandomGoddess#embaralha() lista embaralhada de pedras retornada 
     * pelo gerador randômico} será distribuida entre os jogadores {@link 
     * Jogador#sentaNaMesa(com.github.abdonia.domino.Mesa, int) sentados na 
     * mesa}, onde o jogador na candeira <pre>x</pre> receberá as  pedras do 
     * índices <pre>x*6</pre> até <pre>x*6 + 5</pre>. As pedras nos últimos 
     * quatro indices vão para o dorme.
     * 
     * @param jogador1dupla1 O primeiro jogador da primeira dupla.
     * @param jogador1dupla2 O primeiro jogador da segunda dupla.
     * @param jogador2dupla1 O segundo jogador da primeira dupla.
     * @param jogador2dupla2 O segundo jogador da segunda dupla.
     * @param fortuna O gerador de aleatóriedade a ser usado para
     * embaralhar as cartas e para decidir quem na dupla deve começar uma 
     * partida em caso de empate na {@link Jogador#vontadeDeComecar() vontade
     * demonstrada para começar a jogar}.
     */
    public Jogo(
        final JogadorWrapper jogador1dupla1, 
        final JogadorWrapper jogador1dupla2, 
        final JogadorWrapper jogador2dupla1, 
        final JogadorWrapper jogador2dupla2,
        final RandomGoddess fortuna) {
            
        if(jogador1dupla1 == null 
            || jogador2dupla1 == null 
            || jogador1dupla2 == null 
            || jogador2dupla2 == null) 
            throw new IllegalArgumentException("W.O.!!!");

        this.fortuna = fortuna;
        
        this.eventBroadcaster = 
            configuraEventListners(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2);
        
        this.mesa = 
            new MesaImpl(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2, 
                this.fortuna,
                eventBroadcaster);
        
        jogador1dupla1.sentaNaMesa(this.mesa, 1);
        jogador1dupla2.sentaNaMesa(this.mesa, 2);
        jogador2dupla1.sentaNaMesa(this.mesa, 3);
        jogador2dupla2.sentaNaMesa(this.mesa, 4);
    }

    public void jogar(){
        
        final Dupla dupla1 = mesa.getDupla1();
        final Dupla dupla2 = mesa.getDupla2();
        
        //lancando o evento...
        this.avisaQueJogoComecou(dupla1, dupla2);

        try {
            Dupla ultimaDuplaQueVenceu = null;
            int multiplicadorDobrada = 1;
            while(!alguemVenceu()){

                this.eventBroadcaster.partidaComecou(
                    dupla1.getPontos(), 
                    dupla2.getPontos(), 
                    multiplicadorDobrada != 1);

                final Partida partida = 
                    new Partida(this.mesa, this.fortuna, this.eventBroadcaster);

                final ResultadoPartida resultado = 
                    partida.jogar(ultimaDuplaQueVenceu);

                if(resultado == ResultadoPartida.EMPATE){
                    multiplicadorDobrada *=2;
                } else if(resultado instanceof Batida){
                    final Batida batida = (Batida) resultado;

                    ultimaDuplaQueVenceu = getDuplaVencedora(batida);

                    atualizaPlacar(
                        ultimaDuplaQueVenceu,
                        batida.getTipoDeVitoria(),
                        multiplicadorDobrada);

                    multiplicadorDobrada = 1;
                }
            }

            this.eventBroadcaster.jogoAcabou(
                dupla1.getPontos(), 
                dupla2.getPontos());

        } catch (BugDeJogadorException e) {
            System.err.println("Tá Fazendo merda, " + e.getJogadorBuguento());
            final Pedra pedra = e.getPedra();
            if(pedra != null){
                System.err.println(pedra);
            }
            e.printStackTrace(System.err);
        }
    }

    private void avisaQueJogoComecou(final Dupla dupla1, final Dupla dupla2) {
        
        final JogadorWrapper primeiroJogadorDaDupla1 = dupla1.getJogador1();
        final JogadorWrapper primeiroJogadorDaDupla2 = dupla2.getJogador1();
        final JogadorWrapper segundoJogadorDaDupla1 = dupla1.getJogador2();
        final JogadorWrapper segundoJogadorDaDupla2 = dupla2.getJogador2();
        
        eventBroadcaster.jogoComecou(
            primeiroJogadorDaDupla1.getNome(),
            primeiroJogadorDaDupla2.getNome(),
            segundoJogadorDaDupla1.getNome(),
            segundoJogadorDaDupla2.getNome());
    }

    private DominoEventBroadcaster configuraEventListners(
        final JogadorWrapper jogador1dupla1, 
        final JogadorWrapper jogador1dupla2, 
        final JogadorWrapper jogador2dupla1, 
        final JogadorWrapper jogador2dupla2) {
            
        final DominoEventBroadcaster broadcaster = new DominoEventBroadcaster();

        jogadorAtento(broadcaster, jogador1dupla1);
        jogadorAtento(broadcaster, jogador2dupla1);
        jogadorAtento(broadcaster, jogador1dupla2);
        jogadorAtento(broadcaster, jogador2dupla2);
        
        return broadcaster;
    }

    private void jogadorAtento(
            final DominoEventBroadcaster eventBroadcaster,
            final JogadorWrapper jogadorWrapper) {

        final Jogador jogador = jogadorWrapper.getWrapped();
        if(jogador instanceof DominoEventListener){
            eventBroadcaster
                .addEventListener((DominoEventListener)jogador,false);
        }
    }

    private Dupla getDuplaVencedora(final Batida resultado) {
        return this.mesa.getDuplaDoJogador(resultado.getVencedor());
    }

    private void atualizaPlacar(
        final Dupla duplaDoVencedor, 
        final Vitoria tipoDeBatida, 
        final int multiplicadorDobrada) {
        
        final int pontosDaPartida = 
            tipoDeBatida.getPontos() * multiplicadorDobrada;
        
        duplaDoVencedor.adicionaPontos(pontosDaPartida);
    }

    private boolean alguemVenceu() {
        return mesa.getDupla1().venceu() || mesa.getDupla2().venceu();
    }

    public void addEventListener(final DominoEventListener eventListener) {
        this.eventBroadcaster.addEventListener(eventListener,true);
    }
 }