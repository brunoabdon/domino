package br.nom.abdon.domino;

/**
 * Os sete números que podem aparcer numa face de uma {@link Pedra} de dominó.
 * @author bruno
 */
public enum Numero {
    LIMPO(0), PIO(1), DUQUE(2), TERNO(3), QUADRA(4), QUINA(5), SENA(6);
	
    private final int numeroDePontos;

    private Numero(final int numeroDePontos) {
        this.numeroDePontos = numeroDePontos;
    }

    /**
     * Quantos pontinhos tem nesse número. Usado pra contar quantos pontos uma 
     * {@link Pedra} tem quando alguem tranca a {@link Mesa}.
     * 
     * @return Quantos pontinhos tem nesse número.
     */
    public int getNumeroDePontos() {
        return numeroDePontos;
    }
}
