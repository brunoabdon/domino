package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;

public interface DominoEventListener {

	public void comecouJogo(String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4);
	
	public void comecouPartida(int placarDupla1, int placarDupla2, boolean ehDobrada);
	
	public void jogardorJogou(String nomeDoJogador, Pedra pedra, Lado lado);
	
	public void jogadorTocou(String nomeDoJogador);
	
	public void jogadorBateu(String nomeDoJogador, Vitoria tipoDeVitoria);
	
	public void jogoAcabou(int placarDupla1, int placarDupla2);
	
}
