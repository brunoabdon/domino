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

    private final Map<Integer, Consumer<DominoEventListener>> cachedToques;
    private final Map<Integer,Map<Optional<Lado>,Function<Pedra, Consumer<DominoEventListener>>>> cachedJogadasNoLado;
    
    private static final Function<Integer, Function<Lado, Function<Pedra, Consumer<DominoEventListener>>>> jogadaPorUmJogador =
        (num) -> {return (l) -> (p) -> (el) -> {el.jogadorJogou(num, l, p);};};

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
    public void partidaVoltou(int jogador) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.partidaVoltou(jogador);
                }
        );
    }

    
    @Override
    public void jogadorBateu(int quemFoi, Vitoria tipoDeVitoria) {
        broadCastEvent(eventListeners,
                (eventListener) -> {
                    eventListener.jogadorBateu(quemFoi, tipoDeVitoria);
                }
        );
    }

    @Override
    public void jogadorTocou(int nomeDoJogador) {
        broadCastEvent(eventListeners, cachedToques.get(nomeDoJogador));
    }

    @Override
    public void jogadorJogou(int jogador, Lado lado, Pedra pedra) {
        final Function<Pedra, Consumer<DominoEventListener>> jodadaDoJogadorDaqueleLado =
            this.cachedJogadasNoLado.get(jogador).get(optional(lado));
        
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

        montaCacheJogadasDoJogador(1);
        montaCacheJogadasDoJogador(2);
        montaCacheJogadasDoJogador(3);
        montaCacheJogadasDoJogador(4);
    }

    private void montaCacheJogadasDoJogador(final int jogador) {
        final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado = new HashMap<>(3);
        final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogdor = jogadaPorUmJogador.apply(jogador);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, Lado.ESQUERDO);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, Lado.DIREITO);
        fazCacheDaJogadaDoLado(jogadasDoJogadorDoLado, jogadaPorEsseJogdor, null);
        this.cachedJogadasNoLado.put(jogador, jogadasDoJogadorDoLado);
        
        cachedToques.put(jogador, (eventListener) -> {eventListener.jogadorTocou(jogador);});
    }

    private Function<Pedra, Consumer<DominoEventListener>> fazCacheDaJogadaDoLado(
            final Map<Optional<Lado>, Function<Pedra, Consumer<DominoEventListener>>> jogadasDoJogadorDoLado, 
            final Function<Lado, Function<Pedra, Consumer<DominoEventListener>>> jogadaPorEsseJogdor,
            Lado lado) {
        final Optional<Lado> talvezLado = optional(lado);
        return jogadasDoJogadorDoLado.put(talvezLado,jogadaPorEsseJogdor.apply(lado));
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
