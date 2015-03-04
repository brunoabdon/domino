package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;

class BugDeJogadorException extends Exception {
	
    private final Jogador jogadorBuguento;
    private final Pedra pedra;

    public BugDeJogadorException(
            final String msg, final Jogador jogadorBuguento) {
        this(msg,jogadorBuguento,null);
    }

    public BugDeJogadorException(
            final String msg, 
            final Jogador jogadorBuguento, 
            final Pedra pedra) {
        
        super(msg);
        this.jogadorBuguento = jogadorBuguento;
        this.pedra = pedra;

    }

    public Jogador getJogadorBuguento() {
        return jogadorBuguento;
    }

    public Pedra getPedra() {
        return pedra;
    }
}
