package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;

public class BugDeJogadorException extends Exception {
	
	private Jogador jogadorBuguento;
	private Pedra pedra;

	public BugDeJogadorException(String msg, Jogador jogadorBuguento) {
		this(msg,jogadorBuguento,null);
	}

	public BugDeJogadorException(String msg, Jogador jogadorQueJogou, Pedra pedra) {
		super(msg);
		this.jogadorBuguento = jogadorQueJogou;
		this.pedra = pedra;
		
	}

	public Jogador getJogadorBuguento() {
		return jogadorBuguento;
	}

	public Pedra getPedra() {
		return pedra;
	}

}
