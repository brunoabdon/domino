package br.nom.abdon.domino.motor.util;

import java.util.LinkedList;
import java.util.List;

import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.EventoDomino;

public class DominoEventBroadcaster implements DominoEventListener {
	
	private List<DominoEventListener> eventListeners;
	
	public DominoEventBroadcaster() {
		this.eventListeners = new LinkedList<DominoEventListener>();
	}
	
	public void addEventListneter(DominoEventListener eventListener){
		this.eventListeners.add(eventListener);
	}
	
	@Override
	public void eventoAconteceu(EventoDomino eventoDomino) {
		for (DominoEventListener eventListener : eventListeners) {
			eventListener.eventoAconteceu(eventoDomino);
		}
	}

}
