package br.nom.abdon.domino;

/**
 * Uma jogada que um jogador decidiu fazer. Ela diz qual pedra ele vai jogar e,
 * caso seja necessario, em que cabeca jogar (obrigatorio apenas quando a pedra
 * se encaixa nas duas cabecas da mesa e nao eh uma carroca).
 * 
 * Quando o jogador toca, deve usar a jogada singleton {@link #TOQUE} (nao deve
 * usar <code>null</code>, por exemplo).
 * 
 * @author bruno
 *
 */
public final class Jogada {
	private Pedra pedra;
	private Lado lado;
	
	public static final Jogada TOQUE = new Jogada();

	/**
	 * A jogada de uma determinada pedra. O lado da mesa onde colocar ela nao eh 
	 * especificado. Caso a pessa possa ser encaixada tando de um lado como de outro,
	 * o sistema vai verificar e gerar um erro incorrigivel). 
	 * 
	 * @param pedra a pedra que quer jogar.
	 */
	public Jogada(Pedra pedra) {
		this.pedra = pedra;
	}

	/**
	 * A jogada de uma determinada {@link Pedra} de um determinado {@link Lado} da
	 * {@link Mesa}.  
	 * 
	 * Se o lado for nulo, eh equilvalente a {@link #Jogada(Pedra)}
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
		return this == TOQUE ? "toc toc " : (this.pedra.toString() + "(" + this.lado + ")");
	}
}
