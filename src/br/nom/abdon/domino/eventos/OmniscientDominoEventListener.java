package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Pedra;
import java.util.Collection;

/**
 * Interface para receber eventos com informações sigilosas que acontecem 
 * durante o jogo. Deve ser implementado por UIs, Loggers, etc. mas não por
 * jogadores.
 * 
 * @author bruno
 */
public interface OmniscientDominoEventListener extends DominoEventListener {
    
    public default void jogadorRecebeuPedras(final String quemFoi, Collection<Pedra> pedras){
        
    }
    
}
