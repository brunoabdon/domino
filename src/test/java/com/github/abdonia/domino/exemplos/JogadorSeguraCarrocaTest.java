package com.github.abdonia.domino.exemplos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.abdonia.domino.JogadorTest;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;

class JogadorSeguraCarrocaTest extends JogadorTest<JogadorSeguraCarroca> {
    
    private JogadorSeguraCarroca jogador;
    
    @BeforeEach
    void init() {
        this.jogador = getJogador();
    }
    
    @Override
    protected JogadorSeguraCarroca makeNewInstance() {
        return new JogadorSeguraCarroca();
    }

    @ValueSource(ints={5,6,7,8,100,2200,-1})
    @ParameterizedTest(name = "Senta na cadeira {0}")
    void testaSentarForadaMesa(final int cadeira) {
        
        final Mesa mesa = mock(Mesa.class);
        
        assertThrows(
            IllegalArgumentException.class,
            () -> jogador.sentaNaMesa(mesa, cadeira)
        );
    }

    @Test
    void testaSentarSemMesa() {
        final Mesa mesa = null;
        final int cadeira = 3;
        
        assertThrows(
            IllegalArgumentException.class,
            () -> jogador.sentaNaMesa(mesa, cadeira)
        );
    }

    @Test
    void testaSentarDuasVezes() {
        final Mesa mesa = mock(Mesa.class);
        final int cadeira = 3;
        
        jogador.sentaNaMesa(mesa, cadeira);
        
        assertThrows(IllegalStateException.class,
            () -> jogador.sentaNaMesa(mesa, cadeira)
        );
    }
    
    
    @Test
    void testaRecebeMaoEmPe() {
        assertThrows(IllegalStateException.class,
            () -> jogador.recebeMao(
                Pedra.CARROCA_DE_DUQUE,
                Pedra.CARROCA_DE_LIMPO,
                Pedra.CARROCA_DE_QUADRA,
                Pedra.TERNO_QUADRA,
                Pedra.TERNO_SENA,
                Pedra.TERNO_QUINA
            )
        );
    }

    @Test
    void testaRecebeMaoFaltandoPedra() {
        final Mesa mesa = mock(Mesa.class);
        final int cadeira = 3;
        
        jogador.sentaNaMesa(mesa, cadeira);
        
        assertThrows(IllegalArgumentException.class,
            () -> jogador.recebeMao(
                Pedra.CARROCA_DE_DUQUE,
                Pedra.CARROCA_DE_LIMPO,
                null,
                Pedra.TERNO_QUADRA,
                Pedra.TERNO_SENA,
                Pedra.TERNO_QUINA
            )
        );
    }

    @Test
    void testaRecebePedraRepetida() {
        final Mesa mesa = mock(Mesa.class);
        final int cadeira = 3;
        
        jogador.sentaNaMesa(mesa, cadeira);
        
        assertThrows(IllegalArgumentException.class,
            () -> jogador.recebeMao(
                Pedra.CARROCA_DE_DUQUE,
                Pedra.CARROCA_DE_LIMPO,
                Pedra.LIMPO_DUQUE,
                Pedra.TERNO_QUINA,
                Pedra.TERNO_SENA,
                Pedra.TERNO_QUINA
            )
        );
        
    }
    
}
