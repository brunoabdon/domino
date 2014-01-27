package br.nom.abdon.domino;

public class Jogada {
	private Pedra pedra;
	private Lado lado;
	
	public static final Jogada TOQUE = new Jogada();
	
	public Jogada(Pedra pedra, Lado lado) {
		this.pedra = pedra;
		this.lado = lado;
	}

	private Jogada() {}


	public Pedra getPedra() {
		return pedra;
	}

	public void setPedra(Pedra pedra) {
		this.pedra = pedra;
	}

	public Lado getLado() {
		return lado;
	}

	public void setLado(Lado lado) {
		this.lado = lado;
	}
	
	@Override
	public String toString() {
		return this == TOQUE ? "toc toc " : (this.pedra.toString() + "(" + this.lado + ")");
	}
}
