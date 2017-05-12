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
package com.github.abdonia.domino.motor;

import java.util.EventListener;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;

/**
 * Um jogo de dominó entre 4 {@link Jogador jogadores}, divididos em 2 duplas,
 * que vai ser extenter por várias partidas, até que uma das duplas alcumule 6 
 * pontos (Ver {@link Vitoria} para saber quantos pontos uma dupla ganha em cada 
 * tipo de vitória).
 * 
 * @author Bruno Abdon
 */
public class Jogo {

    private final MesaImpl mesa;
    private final DominoEventBroadcaster eventBroadcaster;
    private final RandomGoddess fortuna;

    private static final Function<String,DominoEventListener> EVT_LIST_INSTANC =
        s-> DominoUtils.instancia(DominoEventListener.class, s);
    
    /**
     * Cria um jogo de dominó de acordo com as configurações passadas:
     * Os {@link Jogador}es e os {@link EventListener}ers informados no 
     * parâmetro <code>dominoConfig</code> serão  instanciados. Se uma 
     * {@link DominoConfig#getNomeRandomizadora() geradora de aleatoriedade} for 
     * informada, será instanciada. Se não os eventos aleatórios serão baseados
     * em R{@link java.util.Random}.
     * 
     * @param configuracao A configuração do jogo.
     * 
     */
    public Jogo(final DominoConfig configuracao){

        final JogadorWrapper jogador1dupla1 = 
            JogadorWrapper
                .criaJogador(
                    configuracao.getNomeJogador1Dupla1(), 
                    configuracao.getClasseJogador1Dupla1());
        
        final JogadorWrapper jogador1dupla2 = 
            JogadorWrapper
                .criaJogador(
                    configuracao.getNomeJogador1Dupla2(), 
                    configuracao.getClasseJogador1Dupla2());

        final JogadorWrapper jogador2dupla1 = 
            JogadorWrapper
                .criaJogador(
                    configuracao.getNomeJogador2Dupla1(), 
                    configuracao.getClasseJogador2Dupla1()); 

        final JogadorWrapper jogador2dupla2 = 
            JogadorWrapper
                .criaJogador(
                    configuracao.getNomeJogador2Dupla2(), 
                    configuracao.getClasseJogador2Dupla2());
        
        this.eventBroadcaster = 
            configuraEventListners(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2);
        
        final Consumer<DominoEventListener> addLisntener = 
            (eventListener) -> {
                this.eventBroadcaster
                    .addEventListener(
                        eventListener,
                        true);
        };

        //adicionando os eventListeners ao jogo
        configuracao
            .getEventListeners()
            .stream()
            .map(EVT_LIST_INSTANC)
            .forEach(addLisntener);

        final String nomeDeusaRandomizacao = 
            configuracao.getNomeRandomizadora();
        
        this.fortuna = 
            nomeDeusaRandomizacao == null 
                ? new DefaultRandomGoddess()
                : DominoUtils.instancia(
                    RandomGoddess.class,
                    nomeDeusaRandomizacao);
        
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

    /**
     * Roda um jogo com os quatro jogadores, notificando o andamento aos 
     * {@link EventListener}s passados no construtor.
     */
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
 }