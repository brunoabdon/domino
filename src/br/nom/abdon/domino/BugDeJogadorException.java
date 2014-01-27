package br.nom.abdon.domino;

public class BugDeJogadorException extends RuntimeException {
	
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

	public void setJogadorBuguento(Jogador jogadorBuguento) {
		this.jogadorBuguento = jogadorBuguento;
	}

}
