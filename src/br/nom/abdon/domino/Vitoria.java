package br.nom.abdon.domino;

public enum Vitoria {
	CONTAGEM_DE_PONTOS(1), BATIDA_SIMPLES(1), CARROCA(2), LA_E_LO(3), CRUZADA(4);
	
	private final int pontos;
	
	private Vitoria(int pontos) {
		this.pontos = pontos;
	}
	
	public int getPontos(){
		return this.pontos;
	}
}
