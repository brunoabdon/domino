package br.nom.abdon.domino.eventos;

/**
 * Interface para receber eventos com informações sigilosas que acontecem 
 * durante o jogo. Deve ser implementado por UIs, Loggers, etc. mas não por
 * jogadores.
 * 
 * @author bruno
 */
public interface DominoRootEventListener {
    
    /**
     *
     * @param eventoSecreto
     */
    public void eventoAconteceu(EventoSigiloso eventoSecreto);
    
    
}
