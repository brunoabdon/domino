package com.github.abdonia.domino.eventos;

import java.util.Collection;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;

/**
 * Interface para receber eventos com informações sigilosas que acontecem 
 * durante o jogo. Deve ser implementado por UIs, Loggers, etc. mas não por
 * jogadores.
 * 
 * @author bruno
 */
public interface OmniscientDominoEventListener extends DominoEventListener {

    /**
     * Avisa que, no início de uma partida, um derterminado {@link Jogador}
     * recebeu dadas {@link Pedra}s.
     * 
     * @param quemFoi O jogador em questão (identificado pelo número da 
     * cadeira).
     * 
     * @param pedras Uma coleção <em>não modificável</em> das 6 pedras que o 
     * jogador recebeu.
     */
    public default void jogadorRecebeuPedras(
            final int quemFoi, Collection<Pedra> pedras){
    }
    
    /**
     * As pedras foram distribuidas, e as quatro que sobraram foram pro dorme.
     * 
     * @param pedras As pedras que foram pro dorme.
     */
    public default void dormeDefinido(final Collection<Pedra> pedras){
        
    }
}
