package br.nom.abdon.domino;

/**
 * Uma jogada que um {@link Jogador} decidiu fazer. Ela diz qual {@link Pedra}
 * ele vai jogar e, caso seja necessário, em que cabeça jogar (obrigatório apenas
 * quando a pedra se encaixa nas duas cabeças da mesa e nao é uma carroça).
 * 
 * Quando o jogador toca, deve usar a jogada singleton {@link #TOQUE} (não deve
 * usar <code>null</code>, por exemplo).
 * 
 * @author bruno
 *
 */
public final class Jogada {
	private Pedra pedra;
	private Lado lado;
	
	/**
	 * A jogada singleton que um {@link Jogador} deve retornar quando vai tocar.
	 */
	public static final Jogada TOQUE = new Jogada();

	/**
	 * A jogada de uma determinada {@link Pedra}. O {@link Lado} da {@link Mesa} onde 
	 * colocar ela nao é especificado. Caso a pedra possa ser encaixada tanto de um 
	 * lado como de outro, o sistema vai verificar e gerar um erro incorrígivel. 
	 * 
	 * @param pedra a pedra que se quer jogar.
	 */
	public Jogada(Pedra pedra) {
		this.pedra = pedra;
	}

	/**
	 * A jogada de uma determinada {@link Pedra} de um determinado {@link Lado} da
	 * {@link Mesa}.  
	 * 
	 * Se o lado for <code>null</code>, é equilvalente a {@link #Jogada(Pedra)}
	 * 
	 * @param pedra a pedra que quer jogar.
	 * @param lado o lado da mesa pra colocar a pedra.
	 */
	public Jogada(Pedra pedra, Lado lado) {
		this(pedra);
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
		return this == TOQUE ? "toc toc" : (this.pedra.toString() + "(" + this.lado + ")");
	}
}
