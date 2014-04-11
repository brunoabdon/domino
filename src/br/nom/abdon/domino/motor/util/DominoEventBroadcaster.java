package br.nom.abdon.domino.motor.util;

import java.util.LinkedList;
import java.util.List;

import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.DominoRootEventListener;
import br.nom.abdon.domino.eventos.EventoDomino;
import br.nom.abdon.domino.eventos.EventoSigiloso;

public class DominoEventBroadcaster implements DominoEventListener, DominoRootEventListener {
	
	private final List<DominoEventListener> eventListeners;
	private final List<DominoRootEventListener> rootEventListeners;
	
	public DominoEventBroadcaster() {
		this.eventListeners = new LinkedList<>();
                this.rootEventListeners = new LinkedList<>();
	}
	
	public void addEventListneter(DominoEventListener eventListener){
		this.eventListeners.add(eventListener);
	}
	
	public void addRootEventListneter(DominoRootEventListener rootEventListener){
		this.rootEventListeners.add(rootEventListener);
	}

        
        @Override
	public void eventoAconteceu(EventoDomino eventoDomino) {
            eventListeners.parallelStream().forEach((eventListener) -> {
                eventListener.eventoAconteceu(eventoDomino);
            });
	}

        @Override
	public void eventoAconteceu(EventoSigiloso eventoSecreto) {
            rootEventListeners.parallelStream().forEach(
                    (eventListener) -> {
                        eventListener.eventoAconteceu(eventoSecreto);
                    }
            );
	}

}
