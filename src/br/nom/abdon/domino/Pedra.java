package br.nom.abdon.domino;

public enum Pedra implements Comparable<Pedra>{

	//0
	CARROCA_DE_LIMPO            (Numero.LIMPO), 

	//1
	LIMPO_PIO       (Numero.LIMPO, Numero.PIO),
	
	//2
	CARROCA_DE_PIO                (Numero.PIO),
	LIMPO_DUQUE   (Numero.LIMPO, Numero.DUQUE),

	//3
	LIMPO_TERNO   (Numero.LIMPO, Numero.TERNO),
	PIO_DUQUE       (Numero.PIO, Numero.DUQUE),
	
	//4
	LIMPO_QUADRA (Numero.LIMPO, Numero.QUADRA),
	PIO_TERNO       (Numero.PIO, Numero.TERNO),
	CARROCA_DE_DUQUE            (Numero.DUQUE),
	
	//5
	LIMPO_QUINA   (Numero.LIMPO, Numero.QUINA),
	PIO_QUADRA     (Numero.PIO, Numero.QUADRA),
	DUQUE_TERNO   (Numero.DUQUE, Numero.TERNO),
	
	//6
	LIMPO_SENA     (Numero.LIMPO, Numero.SENA),
	PIO_QUINA       (Numero.PIO, Numero.QUINA),
	DUQUE_QUADRA (Numero.DUQUE, Numero.QUADRA),
	CARROCA_DE_TERNO            (Numero.TERNO),
	
	//7
	PIO_SENA         (Numero.PIO, Numero.SENA),
	DUQUE_QUINA   (Numero.DUQUE, Numero.QUINA),
	TERNO_QUADRA (Numero.TERNO, Numero.QUADRA),
	
	//8
	DUQUE_SENA     (Numero.DUQUE, Numero.SENA),
	TERNO_QUINA   (Numero.TERNO, Numero.QUINA),
	CARROCA_DE_QUADRA          (Numero.QUADRA),
	
	//9
	TERNO_SENA     (Numero.TERNO, Numero.SENA),
	QUADRA_QUINA (Numero.QUADRA, Numero.QUINA),

	//10
	QUADRA_SENA   (Numero.QUADRA, Numero.SENA),
	CARROCA_DE_QUINA            (Numero.QUINA),

	//11
	QUINA_SENA     (Numero.QUINA, Numero.SENA),
	
	//12
	CARROCA_DE_SENA              (Numero.SENA);
	
	/**
	 * Uma array auxliar, contendo soh as carrocas, em ordem 
	 * crescente (de {@link Numero#LIMPO limpo} a 
	 * {@link Numero#SENA sena})
	 */
	public static final Pedra[] carrocas = 
			new Pedra[]{
				CARROCA_DE_LIMPO, 
				CARROCA_DE_PIO, 
				CARROCA_DE_DUQUE, 
				CARROCA_DE_TERNO, 
				CARROCA_DE_QUADRA, 
				CARROCA_DE_QUINA, 
				CARROCA_DE_SENA};
	
	
	
	private final Numero primeiroNumero;
	private final Numero segundoNumero;
	
	private final int numeroDePontos;
	
	private Pedra(Numero primeiroNumero, Numero secundoNumero){
		this.primeiroNumero = primeiroNumero;
		this.segundoNumero = secundoNumero;
		this.numeroDePontos = primeiroNumero.getNumeroDePontos() + segundoNumero.getNumeroDePontos();
	}
	
	private Pedra(Numero numeroDaCarroca){
		this(numeroDaCarroca,numeroDaCarroca);
	}
	
	public Numero getPrimeiroNumero() {
		return primeiroNumero;
	}

	public Numero getSegundoNumero() {
		return segundoNumero;
	}

	/**
	 * A soma dos dois numeros. Usado quando tranca e tem que
	 * contar os pontos na mao.
	 * @return A soma dos dois numeros;
	 */
	public int getNumeroDePontos() {
		return this.numeroDePontos;
	}
	
	/**
	 * Metodo auxiliar que diz se um dos dois {@link Numero}s dessa 
	 * pedra eh o numero dado como paramentro.
	 * 
	 * @param numero Um {@link Numero}, pra testar se essa pedra tem ele
	 * @return <code>true</code> se um dos dois {@link Numero}s dessa pedra
	 * for o dado como parametro.
	 */
	public boolean temNumero(Numero numero){
		return numero == primeiroNumero || numero == segundoNumero; 
	}
	
	/**
	 * Diz se essa peca eh uma carroca.
	 * @return
	 */
	public boolean isCarroca(){
		return this.primeiroNumero == this.segundoNumero;
	}
	
	@Override
	public String toString() {
		return "[" + primeiroNumero + "|" + segundoNumero + "]";
	}

}
