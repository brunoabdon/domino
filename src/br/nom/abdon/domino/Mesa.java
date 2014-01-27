package br.nom.abdon.domino;

import java.util.Iterator;

/**
 * A visao de como estah a mesa no momento, ou seja, qual a lista 
 * de {@link Pedra}s nela.
 * 
 * As cabecas da lista de pedra sao chamadas de cabeca da esquerda e 
 * cabeca da direita, mas o nome eh soh uma convencao. Nao tem nada a
 * ver com pra que lado as pedras estao (isso nao importa).
 *   
 * @author bruno
 */
public interface Mesa extends Iterable<Pedra>{

	/**
	 * O numero da cabeca da esquerda.
	 * 
	 * @return O numero da cabeca da esquerda.
	 */
	public Numero getNumeroEsquerda();
	
	/**
	 * O numero da cabeca da direita.
	 * 
	 * @return O numero da cabeca da direita.
	 */
	public Numero getNumeroDireita();

	/**
	 * Um {@link Iterator} que permite percorer todas as pedras da mesa, no sentido
	 * esquerda-pra-direita;
	 */
	@Override
	public Iterator<Pedra> iterator();

	/**
	 * Um {@link Iterator} que permite percorer todas as pedras da mesa, no sentido
	 * {@link #getNumeroEsquerda() esquerda}-pra-{@link #getNumeroDireita() direita}
	 */
	public Iterator<Pedra> iteratorEsquedaPraDireita();

	/**
	 * Um {@link Iterator} que permite percorer todas as {@link Pedra}s da mesa, no sentido
	 * {@link #getNumeroDireita() direita}-pra-{@link #getNumeroEsquerda() esquerda};
	 */
	public Iterator<Pedra> iteratorDireitaPraEsquerda();
	
	/**
	 * Diz quantas {@link Pedra}s tem na mesa
	 * @return quantas  {@link Pedra}s tem na mesa
	 */
	public int quantasPecas();
	
	/**
	 * Retorna a lista de {@link Pedra}s como um array, no sentido 
	 * {@link #getNumeroEsquerda() esquerda}-pra-{@link #getNumeroDireita() direita}
	 * @return a lista de {@link Pedra}s como um array
	 */
	public Pedra[] toArray();
	
	/**
	 * Diz se a mesa esta vazia (ou seja, deve-se jogar a 
	 * primeira {@link Pedra pedra} da partida);
	 * @return <code>true</code> so se nao tiver {@link Pedra} na mesa.
	 */
	public boolean taVazia();


}
