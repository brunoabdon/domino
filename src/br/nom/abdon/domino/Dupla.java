package br.nom.abdon.domino;

public class Dupla {
	private int pontos;
	
	private final Jogador jogador1;
	private final Jogador jogador2;

	public Dupla(Jogador jogador1, Jogador jogador2) {
		this.jogador1 = jogador1;
		this.jogador2 = jogador2;
		this.pontos = 0;
	}
	public Jogador getJogador1() {
		return jogador1;
	}
	public Jogador getJogador2() {
		return jogador2;
	}
	
	public int getPontos() {
		return pontos;
	}
	public void adicionaPontos(int pontos) {
		this.pontos += pontos;
	}
	
	public boolean contem(Jogador jogador){
		return this.jogador1 == jogador || this.jogador2 == jogador;
	}

	
	/**
	 * Retorna 0 se o Jogador1 for comecar 
	 * ou 1 se o Jogador 2 for comecar;
	 * 
	 * @return O que eu acabei de dizer;
	 */
	public int quemComeca() {
		//fudeu. como faz?
		return 1;
	}
	
}
