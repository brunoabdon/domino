package com.github.abdonia.domino;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.abdonia.domino.eventos.DominoEventListener;

public abstract class JogadorTest <J extends Jogador>{

    private static Pedra[][] PEDRAS; 
    
    private J jogador;
    private DominoEventListener eventListener;
    
    private static DominoEventListener DEAF_EVENT_LISTENER;

    protected abstract J makeNewInstance();
    
    @BeforeAll
    final static void setUp() {
        DEAF_EVENT_LISTENER = mock(DominoEventListener.class); 
    }
    
    @BeforeEach
    final void criaJogador() throws Exception {
        this.jogador = makeNewInstance();
        this.eventListener = 
            this.jogador instanceof DominoEventListener
                ? (DominoEventListener) this.jogador
                : DEAF_EVENT_LISTENER
            ;
    }

    @ValueSource(ints={0,1,2,3})
    @ParameterizedTest(name = "Senta na cadeira {0}")
    void testaSentarNaMesa(final int cadeira) {
        
        final Mesa mesa = mock(Mesa.class);
        
        assertDoesNotThrow(
            () -> jogador.sentaNaMesa(mesa, cadeira)
        );
    }

    @MethodSource("maosComDozao")
    @ParameterizedTest(name="Joga dozão.")
    void testaComecaComDozao(
            final Pedra p1,
            final Pedra p2,
            final Pedra p3,
            final Pedra p4,
            final Pedra p5,
            final Pedra p6
            ) {

        final Mesa mesa = mock(Mesa.class);
        final int cadeira = 3;

        eventListener.jogoComecou("Joao", "Alfredo", "Maria", "Poliana");
        eventListener.partidaComecou(0, 0, false);
        
        jogador.sentaNaMesa(mesa, cadeira);
        jogador.recebeMao(p1,p2,p3,p4,p5,p6);
        
        final Jogada jogada = jogador.joga();
        assertNotNull(jogada);
        assertSame(Pedra.CARROCA_DE_SENA, jogada.getPedra());
    }
    
    
    static Stream<Pedra[]> maosComDozao() {
        return Arrays.asList(
            new Pedra[] {p(6,6),p(0,1),p(3,4),p(5,6),p(5,5),p(4,2)},
            new Pedra[] {p(3,4),p(6,6),p(0,1),p(5,6),p(0,5),p(4,2)},
            new Pedra[] {p(0,1),p(3,4),p(6,6),p(5,6),p(0,5),p(4,2)},
            new Pedra[] {p(2,2),p(1,1),p(4,4),p(6,6),p(0,5),p(4,2)},
            new Pedra[] {p(0,0),p(3,2),p(2,6),p(5,6),p(0,5),p(6,6)},
            new Pedra[] {p(0,6),p(1,6),p(2,6),p(6,6),p(4,5),p(5,6)}
        ).stream();
    }
    
    public J getJogador() {
        return jogador;
    }

    static {
        PEDRAS = new Pedra[7][];
        
        for (int i = 0; i <= 6; i++) {
           PEDRAS[i] = new Pedra[7-i];
        }
        
        for (final Pedra pedra : Pedra.values()) {
            int p1 = pedra.getPrimeiroNumero().getPontos();
            int p2 = pedra.getSegundoNumero().getPontos();
            PEDRAS[p1][p2-p1] = pedra;
        }
        
    }
    
    static Pedra p(int i, int j) {
        int a = Math.min(i, j);
        int b = Math.max(i, j);
        return PEDRAS[a][b-a];
    }
    
}
