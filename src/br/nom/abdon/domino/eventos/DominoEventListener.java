package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Jogador;

/**
 * Interface para receber eventos públicos do que acontece durante o jogo.
 * 
 * {@link Jogador}es que implementarem esta interface serao automaticamente 
 * registrados para serem avisados dos eventos.
 * 
 * @author bruno
 *
 */
public interface DominoEventListener {
	
	/**
	 * Algum evento acounteceu no jogo
	 * @param eventoDomino Os dados que especificam o evento ocorrido;
	 * @see EventoDomino
	 * @see EventoDomino.Tipo
	 */
	public void eventoAconteceu(EventoDomino eventoDomino);
	
}
