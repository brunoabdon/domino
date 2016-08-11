package com.github.abdonia.domino.motor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

class DominoEventBroadcaster implements 
        DominoEventListener, OmniscientDominoEventListener {

    private final List<DominoEventListener> eventListeners;
    private final List<OmniscientDominoEventListener> omniscientEventListeners;

    private final Map<Integer, Consumer<DominoEventListener>> cachedToques;
    private final Map<Integer,Map<Optional<Lado>,Function<Pedra, Consumer<DominoEventListener>>>> cachedJogadasNoLado;
    
    private static final Function<Integer, Function<Lado, Function<Pedra, Consumer<DominoEventListener>>>> JOGADA_POR_UM_JOGADOR =
        (num) -> {return (l) -> (p) -> (el) -> {el.jogadorJogou(num, l, p);};};

    private static final Optional<Lado> OPTIONAL_ESQ = 
        Optional.ofNullable(Lado.ESQUERDO);
    private static final Optional<Lado> OPTIONAL_DIR =  
        Optional.ofNullable(Lado.DIREITO);
    private static final Optional<Lado> OPTIONAL_LADO_NENHUM = Optional.empty();

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
        
        if(permiteAcessoTotal 
            && eventListener instanceof OmniscientDominoEventListener){
            
            final OmniscientDominoEventListener omniscientEventListener = 
                    (OmniscientDominoEventListener) eventListener;
            this.omniscientEventListeners.add(omniscientEventListener);
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
        broadCastEvent(
            eventListeners,
            (eventListener) -> {
                eventListener.partidaEmpatou();
            }
        );
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
            this.cachedJogadasNoLado.get(jogador).get(optional(lado));
        
        final Consumer<DominoEventListener> 
                jogadaDessaPedraDesseLadoPorEsseJogador = 
            jogadaDoJogadorDaqueleLado.apply(pedra);

        broadCastEvent(eventListeners, jogadaDessaPedraDesseLadoPorEsseJogador);
    }

    private Optional<Lado> optional(final Lado lado) {
        return lado == Lado.ESQUERDO 
                ? OPTIONAL_ESQ 
                : lado == Lado.DIREITO 
                    ? OPTIONAL_DIR
                    : OPTIONAL_LADO_NENHUM;
    }
                
    @Override
    public void partidaComecou(
            final int placarDupla1, 
            final int placarDupla2, 
            final boolean ehDobrada) {
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener
                    .partidaComecou(
                        placarDupla1, 
                        placarDupla2, 
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
        final String nomeDoJogador1, final String nomeDoJogador2, 
        final String nomeDoJogador3, final String nomeDoJogador4) {
        
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener.jogoComecou(
                    nomeDoJogador1, nomeDoJogador2, 
                    nomeDoJogador3, nomeDoJogador4);
            }
        );

        montaCacheJogadasDoJogador(1);
        montaCacheJogadasDoJogador(2);
        montaCacheJogadasDoJogador(3);
        montaCacheJogadasDoJogador(4);
    }

    private void montaCacheJogadasDoJogador(final int jogador) {

        final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> 
            jogadasDoJogadorDoLado = new HashMap<>(3);

        final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> 
            jogadaPorEsseJogador = JOGADA_POR_UM_JOGADOR.apply(jogador);

        final Consumer<DominoEventListener> toqueDesseJogador = 
            (eventListener) -> {eventListener.jogadorTocou(jogador);};
        
        cacheJogadasDoLado(
            jogadasDoJogadorDoLado, 
            jogadaPorEsseJogador, 
            Lado.ESQUERDO);
        
        cacheJogadasDoLado(
            jogadasDoJogadorDoLado, 
            jogadaPorEsseJogador,
            Lado.DIREITO);

        cacheJogadasDoLado(
            jogadasDoJogadorDoLado, 
            jogadaPorEsseJogador, 
            null);
        
        this.cachedJogadasNoLado.put(jogador, jogadasDoJogadorDoLado);
        this.cachedToques.put(jogador, toqueDesseJogador);
    }

    private Function<Pedra, Consumer<DominoEventListener>> cacheJogadasDoLado(
            final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado, 
            final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogador,
            final Lado lado) {
        
        final Optional<Lado> talvezLado = optional(lado);

        final Function<Pedra, Consumer<DominoEventListener>> 
            jogadaDesseJogadorDesseLado = 
                jogadaPorEsseJogador.apply(lado);
        return 
            jogadasDoJogadorDoLado.put(talvezLado, jogadaDesseJogadorDesseLado);
    }

    @Override
    public void jogadorRecebeuPedras(int quemFoi, Collection<Pedra> pedras) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.jogadorRecebeuPedras(quemFoi, pedras);
            }
        );
    }

    @Override
    public void dormeDefinido(Collection<Pedra> pedras) {
        broadCastEvent(omniscientEventListeners,
            (eventListener) -> {
                eventListener.dormeDefinido(pedras);
            }
        );
    }
}