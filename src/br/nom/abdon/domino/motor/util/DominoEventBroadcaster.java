package br.nom.abdon.domino.motor.util;

import java.util.LinkedList;
import java.util.List;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;

public class DominoEventBroadcaster implements DominoEventListener {
	
	private List<DominoEventListener> eventListeners;
	
	public DominoEventBroadcaster() {
		this.eventListeners = new LinkedList<DominoEventListener>();
	}
	
	public void addEventListneter(DominoEventListener eventListener){
		this.eventListeners.add(eventListener);
	}

	@Override
	public void comecouPartida(int placarDupla1, int placarDupla2, boolean ehDobrada) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.comecouPartida(placarDupla1, placarDupla2, ehDobrada);
		}
	}

	@Override
	public void jogardorJogou(String nomeDoJogador, Pedra pedra, Lado lado) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.jogardorJogou(nomeDoJogador, pedra, lado);
		}
	}

	@Override
	public void jogadorTocou(String nomeDoJogador) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.jogadorTocou(nomeDoJogador);
		}
	}

	@Override
	public void comecouJogo(String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.comecouJogo(nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4);
		}
		
	}

	@Override
	public void jogadorBateu(String nomeDoJogador, Vitoria tipoDeVitoria) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.jogadorBateu(nomeDoJogador, tipoDeVitoria);
		}
		
	}

	@Override
	public void jogoAcabou(int placarDupla1, int placarDupla2) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.jogoAcabou(placarDupla1, placarDupla2);
		}
	}
	
}
