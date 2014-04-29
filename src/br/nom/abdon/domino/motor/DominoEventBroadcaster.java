package br.nom.abdon.domino.motor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.OmniscientDominoEventListener;

class DominoEventBroadcaster implements 
        DominoEventListener, OmniscientDominoEventListener {

    private final List<DominoEventListener> eventListeners;
    private final List<OmniscientDominoEventListener> omniscientEventListeners;

    private final Map<String, Consumer<DominoEventListener>> cachedToques;
    private final Map<String,Map<Optional<Lado>,Function<Pedra, Consumer<DominoEventListener>>>> cachedJogadasNoLado;
    
    private static final Function<String, Function<Lado, Function<Pedra, Consumer<DominoEventListener>>>> jogadaPorUmJogador =
        (nome) -> {return (l) -> (p) -> (el) -> {el.jogadorJogou(nome, l, p);};};

    private static final Optional<Lado> optionalEsquerda = Optional.ofNullable(Lado.ESQUERDO);
    private static final Optional<Lado> optionalDireita = Optional.ofNullable(Lado.DIREITO);
    private static final Optional<Lado> optionalLadoNenhum = Optional.empty();

    public DominoEventBroadcaster() {
        this.eventListeners = new LinkedList<>();
        this.omniscientEventListeners = new LinkedList<>();
        
        this.cachedJogadasNoLado = new HashMap<>(4);
        this.cachedToques = new HashMap<>(4);

    }

    public void addEventListener(
            DominoEventListener eventListener, boolean permiteAcessoTotal) {
        this.eventListeners.add(eventListener);
        
        if(permiteAcessoTotal 
                && eventListener instanceof OmniscientDominoEventListener){
            OmniscientDominoEventListener omniscientEventListener = 
                    (OmniscientDominoEventListener) eventListener;
            this.omniscientEventListeners.add(omniscientEventListener);
        } 
    }

    private <E> void broadCastEvent(
            Collection<E> listeners, Consumer<? super E> c) {
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
    public void partidaVoltou(String nomeDoJogador) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.partidaVoltou(nomeDoJogador);
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
        final Function<Pedra, Consumer<DominoEventListener>> jodadaDoJogadorDaqueleLado =
            this.cachedJogadasNoLado.get(nomeDoJogador).get(optional(lado));
        
        final Consumer<DominoEventListener> jogadaDessaPedraDesseLadoPorEsseJogador = 
            jodadaDoJogadorDaqueleLado.apply(pedra);

        broadCastEvent(eventListeners, jogadaDessaPedraDesseLadoPorEsseJogador);
                
    }

    private Optional<Lado> optional(Lado lado) {
        return lado == Lado.ESQUERDO 
                ? optionalEsquerda : lado == Lado.DIREITO 
                ? optionalDireita
                : optionalLadoNenhum;
    }
                
    @Override
    public void partidaComecou(
            int placarDupla1, int placarDupla2, boolean ehDobrada) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.partidaComecou(
                            placarDupla1, placarDupla2, ehDobrada);
                }
        );
    }

    @Override
    public void jogoComecou(
            String nomeDoJogador1, String nomeDoJogador2, 
            String nomeDoJogador3, String nomeDoJogador4) {
        
        broadCastEvent(eventListeners,
            (eventListener) -> {
                eventListener.jogoComecou(
                        nomeDoJogador1, nomeDoJogador2, 
                        nomeDoJogador3, nomeDoJogador4);
            }
        );

        montaCacheJogadasDoJogador(nomeDoJogador1);
        montaCacheJogadasDoJogador(nomeDoJogador2);
        montaCacheJogadasDoJogador(nomeDoJogador3);
        montaCacheJogadasDoJogador(nomeDoJogador4);
    }

    private void montaCacheJogadasDoJogador(final String nomeDoJogador) {
        final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado = new HashMap<>(3);
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
        final Optional<Lado> talvezLado = optional(lado);
        return jogadasDoJogadorDoLado.put(talvezLado,jogadaPorEsseJogdor.apply(lado));
    }

    @Override
    public void jogadorRecebeuPedras(String quemFoi, Collection<Pedra> pedras) {
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
