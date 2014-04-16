package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;

class BugDeJogadorException extends Exception {
	
    private Jogador jogadorBuguento;
    private Pedra pedra;

    public BugDeJogadorException(String msg, Jogador jogadorBuguento) {
            this(msg,jogadorBuguento,null);
    }

    public BugDeJogadorException(
            String msg, 
            Jogador jogadorBuguento, 
            Pedra pedra) {
        
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
