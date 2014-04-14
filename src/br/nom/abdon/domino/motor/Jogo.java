package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.LoggerDominoEventListener;
import br.nom.abdon.domino.motor.util.DominoEventBroadcaster;



public class Jogo {

	private Dupla dupla1, dupla2;
	private DominoEventBroadcaster eventBroadcaster;
	
	public Jogo(Dupla dupla1, Dupla dupla2) {
		if(dupla1 == null || dupla2 == null) throw new IllegalArgumentException("W.O.!!!");
		this.dupla1 = dupla1;
		this.dupla2 = dupla2;
		
		configuraEventListners();
		
	}
	
	public void jogar(){
		
		eventBroadcaster.jogoComecou(dupla1.getJogador1().getNome(), 
						dupla2.getJogador1().getNome(), 
						dupla1.getJogador2().getNome(), 
						dupla2.getJogador2().getNome());
		
		try {
			Dupla ultimaDuplaQueVenceu = null;
			int multiplicadorDobrada = 1;
			while(!alguemVenceu()){
				this.eventBroadcaster.partidaComecou(dupla1.getPontos(), dupla2.getPontos(), multiplicadorDobrada != 1);
				Partida partida = new Partida(dupla1, dupla2, eventBroadcaster);
				
				ResultadoPartida resultado = partida.jogar(ultimaDuplaQueVenceu);
				if(resultado == ResultadoPartida.EMPATE){
					multiplicadorDobrada *=2;
				} else {
					ultimaDuplaQueVenceu = getDuplaVencedora(resultado);
					atualizaPlacar(ultimaDuplaQueVenceu,resultado.getTipoDeVitoria(),multiplicadorDobrada);
					multiplicadorDobrada = 1;
				}
			}
			
			eventBroadcaster.jogoAcabou(dupla1.getPontos(), dupla2.getPontos());
			
			
		} catch (BugDeJogadorException e) {
			System.err.println("Jogador " + e.getJogadorBuguento() + " fez merda.");
			Pedra pedra = e.getPedra();
			if(pedra  != null){
				System.err.println(pedra);
			}
			
			e.printStackTrace();
		}
		
	}

	private void configuraEventListners() {
		eventBroadcaster = new DominoEventBroadcaster();
		eventBroadcaster.addEventListneter(new LoggerDominoEventListener());
		
		jogadorAtento(dupla1.getJogador1());
		jogadorAtento(dupla1.getJogador2());
		jogadorAtento(dupla2.getJogador1());
		jogadorAtento(dupla2.getJogador2());
		
	}

	private void jogadorAtento(Jogador jogador) {
		if(jogador instanceof DominoEventListener){
			eventBroadcaster.addEventListneter((DominoEventListener)jogador);
		}
	}

	private Dupla getDuplaVencedora(ResultadoPartida resultado) {
		return dupla1.contem(resultado.getVencedor())?dupla1:dupla2;
	}

	private void atualizaPlacar(Dupla duplaDoVencdor , Vitoria tipoDeBatida, int multiplicadorDobrada) {
		duplaDoVencdor.adicionaPontos(tipoDeBatida.getPontos() * multiplicadorDobrada);
	}

	private boolean alguemVenceu() {
		return dupla1.getPontos() >= 6 || dupla2.getPontos() >= 6;
	}

	public void addEventListner(DominoEventListener eventListener) {
		this.eventBroadcaster.addEventListneter(eventListener);

	}
}
