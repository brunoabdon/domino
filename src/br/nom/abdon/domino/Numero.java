package br.nom.abdon.domino;

public enum Numero {
	LIMPO(0), PIO(1), DUQUE(2), TERNO(3), QUADRA(4), QUINA(5), SENA(6);
	
	private final int numeroDePontos;

	private Numero(int numeroDePontos) {
		this.numeroDePontos = numeroDePontos;
	}

	public int getNumeroDePontos() {
		return numeroDePontos;
	}

}
