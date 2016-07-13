package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;

public class Jogo {

    private final MesaImpl mesa;
    private final DominoEventBroadcaster eventBroadcaster;

    public Jogo(
        JogadorWrapper jogador1dupla1, 
        JogadorWrapper jogador1dupla2, 
        JogadorWrapper jogador2dupla1, 
        JogadorWrapper jogador2dupla2) {
            
        if(jogador1dupla1 == null 
            || jogador2dupla1 == null 
            || jogador1dupla2 == null 
            || jogador2dupla2 == null) 
            throw new IllegalArgumentException("W.O.!!!");

        this.eventBroadcaster = configuraEventListners(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2);
        
        this.mesa = new MesaImpl(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2, 
                eventBroadcaster);
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
                    new Partida(this.mesa, this.eventBroadcaster);

                ResultadoPartida resultado = partida.jogar(ultimaDuplaQueVenceu);
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

        Jogador jogador = jogadorWrapper.getWrapped();
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