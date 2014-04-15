package br.nom.abdon.domino;

import java.util.Iterator;

/**
 * A visão de como está a mesa no momento, ou seja, qual a lista 
 * de {@link Pedra}s nela.
 * 
 * As cabeças da lista de pedras são referidas como cabeça da esquerda e 
 * cabeça da direita, mas o nome é só uma convenção. Não tem nada a ver 
 * com pra que lado as pedras estão (isso não importa).
 *   
 * @author bruno
 */
public interface Mesa extends Iterable<Pedra>{

	/**
	 * O {@link Numero} da cabeça da esquerda.
	 * 
	 * @return O número da cabeça da esquerda.
	 */
	public Numero getNumeroEsquerda();
	
	/**
	 * O {@link Numero} da cabeça da direita.
	 * 
	 * @return O número da cabeça da direita.
	 */
	public Numero getNumeroDireita();

	/**
	 * Um {@link Iterator} que permite percorer todas as {@link Pedra}s da {@link Mesa}, 
	 * no sentido {@link #getNumeroEsquerda() esquerda}-pra-{@link #getNumeroDireita() direita};
	 * 
	 * O iterator será "read-only", ou seja, uma chamada a {@link Iterator#remove()}
	 * é ilegal e vai causar {@link UnsupportedOperationException}.
         * 
         * @return um Iterator pra ver as pedras da mesa
         */
	@Override
	public Iterator<Pedra> iterator();

	/**
	 * Um {@link Iterator} que permite percorer todas as pedras da mesa, no sentido
	 * {@link #getNumeroEsquerda() esquerda}-pra-{@link #getNumeroDireita() direita}
	 * 
	 * O iterator será "read-only", ou seja, uma chamada a {@link Iterator#remove()}
	 * é ilegal e vai causar {@link UnsupportedOperationException}.
         * 
         * @return um Iterator pra ver as pedras da mesa
	 */
	public Iterator<Pedra> iteratorEsquedaPraDireita();

	/**
	 * Um {@link Iterator} que permite percorer todas as {@link Pedra}s da mesa, no sentido
	 * {@link #getNumeroDireita() direita}-pra-{@link #getNumeroEsquerda() esquerda};
	 * 
	 * O iterator será "read-only", ou seja, uma chamada a {@link Iterator#remove()}
	 * é ilegal e vai causar {@link UnsupportedOperationException}.
         * 
         * @return um Iterator pra ver as pedras da mesa
	 */
	public Iterator<Pedra> iteratorDireitaPraEsquerda();
	
        
        /**
         * Diz quantas {@link  Pedra}s um dado {@link Jogador} tem na mão no momento.
         * @param qualJogador qual o jogador que se deseja saber.
         * @return Quantas pedras o jogador de indice <code>qualJogador</code> tem
         * na mão.
         */
        public int quantasPedrasOJogadoresTem(int qualJogador);
        
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
	 * Diz se a mesa está vazia (ou seja, deve-se jogar a 
	 * primeira {@link Pedra pedra} da partida);
	 * @return <code>true</code> so se nao tiver {@link Pedra} na mesa.
	 */
	public boolean taVazia();


}
