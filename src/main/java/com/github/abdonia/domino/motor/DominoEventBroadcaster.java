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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

/**
 * Um {@link OmniscientDominoEventListener} que ecoa os eventos que ouve pra uma 
 * lista de eventlisteners registrados.
 * 
 * @author Bruno Abdon
 */
class DominoEventBroadcaster implements OmniscientDominoEventListener {

    private final List<DominoEventListener> eventListeners;
    private final List<OmniscientDominoEventListener> omniscientEventListeners;

    private final Map<Integer, Consumer<DominoEventListener>> cachedToques;
    private final Map<Integer,Map<Lado,Function<Pedra, Consumer<DominoEventListener>>>> cachedJogadasNoLado;
    
    private static final Function<Integer, Function<Lado, Function<Pedra, Consumer<DominoEventListener>>>> JOGADA_POR_UM_JOGADOR =
        num -> l -> p -> el -> el.jogadorJogou(num, l, p);

    private static final Predicate<DominoEventListener> IS_OMNISCIENT = 
        e -> e instanceof OmniscientDominoEventListener;
    
    public DominoEventBroadcaster() {
        this.eventListeners = new LinkedList<>();
        this.omniscientEventListeners = new LinkedList<>();
        
        this.cachedJogadasNoLado = new HashMap<>(4);
        this.cachedToques = new HashMap<>(4);
    }

    public void addEventListener(
            final DominoEventListener eventListener,
            final boolean permiteAcessoTotal) {
        
        this.eventListeners.add(eventListener);
        if(permiteAcessoTotal && IS_OMNISCIENT.test(eventListener)){
            this.omniscientEventListeners
                .add((OmniscientDominoEventListener)eventListener);
        }
    }
    
    public void addEventListeners(
            final Collection<DominoEventListener> eventListeners,
            final boolean permiteAcessoTotal) {

        this.eventListeners.addAll(eventListeners);
        
        if(permiteAcessoTotal){
            eventListeners
            .parallelStream()
            .filter(IS_OMNISCIENT)
            .map(e -> (OmniscientDominoEventListener) e)
            .forEach(this.omniscientEventListeners::add);
        }
    }
    
    private <E> void broadCastEvent(
            final Collection<E> listeners, 
            final Consumer<? super E> c) {
        listeners.parallelStream().forEach(c);
    }

    @Override
    public void jogoAcabou(final int placarDupla1, final int placarDupla2) {
        broadCastEvent(
            eventListeners,
            (eventListener) -> {
                eventListener.jogoAcabou(placarDupla1, placarDupla2);
            }
        );
    }

    @Override
    public void partidaEmpatou() {
        broadCastEvent(eventListeners, DominoEventListener::partidaEmpatou);
    }

    @Override
    public void partidaVoltou(final int jogador) {
        broadCastEvent(
            eventListeners,
            (eventListener) -> {
                eventListener.partidaVoltou(jogador);
            }
        );
    }
    
    @Override
    public void jogadorBateu(final int quemFoi, final Vitoria tipoDeVitoria) {
        broadCastEvent(
            eventListeners,
            (eventListener) -> {
                eventListener.jogadorBateu(quemFoi, tipoDeVitoria);
            }
        );
    }

    @Override
    public void jogadorTocou(final int quemFoi) {
        broadCastEvent(eventListeners, cachedToques.get(quemFoi));
    }

    @Override
    public void jogadorJogou(
            final int jogador, 
            final Lado lado, 
            final Pedra pedra) {
        final Function<Pedra, Consumer<DominoEventListener>> 
                jogadaDoJogadorDaqueleLado =
            this.cachedJogadasNoLado.get(jogador).get(lado);
        
        final Consumer<DominoEventListener> 
                jogadaDessaPedraDesseLadoPorEsseJogador = 
            jogadaDoJogadorDaqueleLado.apply(pedra);

        broadCastEvent(eventListeners, jogadaDessaPedraDesseLadoPorEsseJogador);
    }

    @Override
    public void partidaComecou(
            final int placarDupla0, 
            final int placarDupla1, 
            final boolean ehDobrada) {
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener
                    .partidaComecou(
                        placarDupla0, 
                        placarDupla1, 
                        ehDobrada);
            }
        );
    }

    @Override
    public void decididoQuemComeca(
            final int jogador, final boolean consentimentoMutuo) {
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener.decididoQuemComeca(
                    jogador, consentimentoMutuo);
            }
        );
    }

    @Override
    public void jogoComecou(
        final String nomeDoJogador0, final String nomeDoJogador1, 
        final String nomeDoJogador2, final String nomeDoJogador3) {
        
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener.jogoComecou(
                    nomeDoJogador0, nomeDoJogador1, 
                    nomeDoJogador2, nomeDoJogador3);
            }
        );

        montaCacheJogadasDoJogador(0);
        montaCacheJogadasDoJogador(1);
        montaCacheJogadasDoJogador(2);
        montaCacheJogadasDoJogador(3);
    }

    private void montaCacheJogadasDoJogador(final int jogador) {

        final Map<Lado, Function<Pedra, Consumer<DominoEventListener>>> 
            jogadasDoJogadorDoLado = new HashMap<>(3);

        final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> 
            jogadaPorEsseJogador = JOGADA_POR_UM_JOGADOR.apply(jogador);

        final Consumer<DominoEventListener> toqueDesseJogador = 
            eventListener -> eventListener.jogadorTocou(jogador);
        
        cacheJogadasDoLado(
            jogadasDoJogadorDoLado, 
            jogadaPorEsseJogador, 
            Lado.ESQUERDO);
        
        cacheJogadasDoLado(
            jogadasDoJogadorDoLado, 
            jogadaPorEsseJogador,
            Lado.DIREITO);
        
        this.cachedJogadasNoLado.put(jogador, jogadasDoJogadorDoLado);
        this.cachedToques.put(jogador, toqueDesseJogador);
    }

    private Function<Pedra, Consumer<DominoEventListener>> cacheJogadasDoLado(
            final Map<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado, 
            final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogador,
            final Lado lado) {
        
        final Function<Pedra, Consumer<DominoEventListener>> 
            jogadaDesseJogadorDesseLado = jogadaPorEsseJogador.apply(lado);

        return jogadasDoJogadorDoLado.put(lado, jogadaDesseJogadorDesseLado);
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
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener
                    .jogadorRecebeuPedras(
                        quemFoi, 
                        pedra1, pedra2, pedra3, 
                        pedra4, pedra5, pedra6);
            }
        );
    }

    @Override
    public void dormeDefinido(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.dormeDefinido(pedra1, pedra2, pedra3, pedra4);
            }
        );
    }

    @Override
    public void jogadorJogouPedraQueNãoTinha(
            final int quemFoi, final Pedra pedra) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorJogouPedraQueNãoTinha(quemFoi,pedra);
            }
        );
    }

    @Override
    public void jogadorTocouTendoPedraPraJogar(final int quemFoi) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorTocouTendoPedraPraJogar(quemFoi);
            }
        );
    }

    @Override
    public void jogadorComecouErrando(final int quemFoi) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorComecouErrando(quemFoi);
            }
        );
    }

    @Override
    public void jogadorJogouPedraNenhuma(final int quemFoi) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorJogouPedraNenhuma(quemFoi);
            }
        );
    }

    @Override
    public void jogadorFaleceu(final int quemFoi, final String causaMortis) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorFaleceu(quemFoi, causaMortis);
            }
        );
    }

    @Override
    public void jogadorErrouVontadeDeComeçar(final int quemFoi) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorErrouVontadeDeComeçar(quemFoi);
            }
        );
    }

    @Override
    public void jogadorJogouPedraInvalida(
            final int quemFoi, final Pedra pedra, final Numero numero) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorJogouPedraInvalida(quemFoi,pedra,numero);
            }
        );
    }
}
