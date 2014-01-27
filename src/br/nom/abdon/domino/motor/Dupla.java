package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;

class Dupla {
	private int pontos;
	
	private final Jogador jogador1;
	private final Jogador jogador2;

	private final String nomeJogador1;
	private final String nomeJogador2;

	public Dupla(Jogador jogador1, String nomeJogador1, Jogador jogador2, String nomeJogador2) {
		if(jogador1 == null || jogador2 == null) throw new IllegalArgumentException("Só pode dupla de dois");
		
		if(nomeJogador1 == null || nomeJogador2 == null) throw new IllegalArgumentException("João SemNome não joga");

		this.jogador1 = jogador1;
		this.jogador2 = jogador2;

		this.nomeJogador1 = nomeJogador1;
		this.nomeJogador2 = nomeJogador2;
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
	 * @throws BugDeJogadorException  
	 */
	public int quemComeca() throws BugDeJogadorException {
		int vontadeDo1 = jogador1.vontadeDeComecar();
		validaVontade(vontadeDo1,jogador1);

		int vontadeDo2 = jogador2.vontadeDeComecar();
		validaVontade(vontadeDo2,jogador2);
		
		return vontadeDo1 > vontadeDo2 ? 0 : 1; //se empatar, dane-se. comeca qualquer um. vai o 2.
	}
	private void validaVontade(int vontade, Jogador jogador) throws BugDeJogadorException {
		if(vontade < 0 || vontade > 10){
			throw new BugDeJogadorException("Vontade é de zero a dez só, meu velho. Não inventa",jogador);
		}
	}
	
	@Override
	public String toString() {
		return this.jogador1 + " e "  + this.jogador2 + ", " + this.pontos;
	}
	
	public String getNomeJogador1() {
		return nomeJogador1;
	}
	public String getNomeJogador2() {
		return nomeJogador2;
	}

	public String getNomeJogador(Jogador jogador) {
		return jogador == jogador1 ? nomeJogador1 : jogador == jogador2 ? nomeJogador2 : null;
	}

	
}
