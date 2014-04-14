package br.nom.abdon.domino.motor.util;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import java.util.LinkedList;
import java.util.List;

import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.DominoRootEventListener;
import br.nom.abdon.domino.eventos.EventoSigiloso;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DominoEventBroadcaster implements DominoEventListener, DominoRootEventListener {

    private final List<DominoEventListener> eventListeners;
    private final List<DominoRootEventListener> rootEventListeners;

    private final Map<String, Consumer<DominoEventListener>> cachedToques;
    private final Map<String,Map<Optional<Lado>,Function<Pedra, Consumer<DominoEventListener>>>> cachedJogadasNoLado;
    
    private final static Function<String, Function<Lado, Function<Pedra, Consumer<DominoEventListener>>>> jogadaPorUmJogador =
            (nome) -> {return (l) -> {return (p) -> {return (el) -> {el.jogadorJogou(nome, l, p);};};};};
    
    public DominoEventBroadcaster() {
        this.eventListeners = new LinkedList<>();
        this.rootEventListeners = new LinkedList<>();
        
        this.cachedJogadasNoLado = new HashMap<>(4);
        this.cachedToques = new HashMap<>(4);

    }

    public void addEventListneter(DominoEventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    public void addEventListneter(DominoRootEventListener rootEventListener) {
        this.rootEventListeners.add(rootEventListener);
    }

    private <E> void broadCastEvent(Collection<E> listeners, Consumer<? super E> c) {
        if(c == null)System.out.println(c);
        listeners.parallelStream().forEach(c);
    }

    @Override
    public void jogoAcabou(int placarDupla1, int placarDupla2) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.jogoAcabou(placarDupla1, placarDupla2);
                }
        );
    }

    @Override
    public void partidaEmpatou() {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.partidaEmpatou();
                }
        );
    }

    @Override
    public void jogadorBateu(String quemFoi, Vitoria tipoDeVitoria) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.jogadorBateu(quemFoi, tipoDeVitoria);
                }
        );
    }


    @Override
    public void jogadorTocou(String nomeDoJogador) {
        broadCastEvent(eventListeners, cachedToques.get(nomeDoJogador));
    }

    @Override
    public void jogadorJogou(String nomeDoJogador, Lado lado, Pedra pedra) {
        final Function<Pedra, Consumer<DominoEventListener>> jodadaDoJogadorDaqueleLado 
                = this.cachedJogadasNoLado.get(nomeDoJogador).get(Optional.ofNullable(lado));
        
        final Consumer<DominoEventListener> jogadaDessaPedraDesseLadoPorEsseJogador = 
                    jodadaDoJogadorDaqueleLado.apply(pedra);

        broadCastEvent(eventListeners, jogadaDessaPedraDesseLadoPorEsseJogador);
                
    }
                
    @Override
    public void partidaComecou(int placarDupla1, int placarDupla2, boolean ehDobrada) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.partidaComecou(placarDupla1, placarDupla2, ehDobrada);
                }
        );
    }

    @Override
    public void jogoComecou(String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.jogoComecou(nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4);
                }
        );

        montaCacheJogadasDoJogador(nomeDoJogador1);
        montaCacheJogadasDoJogador(nomeDoJogador2);
        montaCacheJogadasDoJogador(nomeDoJogador3);
        montaCacheJogadasDoJogador(nomeDoJogador4);
        
        
    }

    private void montaCacheJogadasDoJogador(final String nomeDoJogador) {
        final Map<Optional<Lado>,Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado = new HashMap<>(3);
        final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogdor = jogadaPorUmJogador.apply(nomeDoJogador);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, Lado.ESQUERDO);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, Lado.DIREITO);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, null);
        this.cachedJogadasNoLado.put(nomeDoJogador, jogadasDoJogadorDoLado);
        
        cachedToques.put(nomeDoJogador, (eventListener) -> {eventListener.jogadorTocou(nomeDoJogador);});
    }

    private Function<Pedra, Consumer<DominoEventListener>> fazCacheDaJogadaDoLado(
            final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado, 
            final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogdor,
            Lado lado) {
        Optional<Lado> talvezLado = Optional.ofNullable(lado);
        return jogadasDoJogadorDoLado.put(talvezLado,jogadaPorEsseJogdor.apply(lado));
    }

    public void eventoAconteceu(EventoSigiloso eventoSecreto) {
        rootEventListeners.parallelStream().forEach(
                (eventListener) -> {
                    eventListener.eventoAconteceu(eventoSecreto);
                }
        );
    }

}
