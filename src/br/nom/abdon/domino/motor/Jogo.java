package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;

public class Jogo {

	private final Dupla dupla1, dupla2;
	private final DominoEventBroadcaster eventBroadcaster;
	
	public Jogo(final Dupla dupla1, final Dupla dupla2) {
		if(dupla1 == null || dupla2 == null) throw new IllegalArgumentException("W.O.!!!");
		this.dupla1 = dupla1;
		this.dupla2 = dupla2;

		this.eventBroadcaster = configuraEventListners(dupla1, dupla2);
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
				Partida partida = new Partida(dupla1, dupla2, this.eventBroadcaster);
				
				ResultadoPartida resultado = partida.jogar(ultimaDuplaQueVenceu);
				if(resultado == ResultadoPartida.EMPATE){
					multiplicadorDobrada *=2;
				} else {
					ultimaDuplaQueVenceu = getDuplaVencedora(resultado);
					atualizaPlacar(ultimaDuplaQueVenceu,resultado.getTipoDeVitoria(),multiplicadorDobrada);
					multiplicadorDobrada = 1;
				}
			}
			
			this.eventBroadcaster.jogoAcabou(dupla1.getPontos(), dupla2.getPontos());
			
			
		} catch (BugDeJogadorException e) {
			System.err.println("Jogador " + e.getJogadorBuguento() + " fez merda.");
			Pedra pedra = e.getPedra();
			if(pedra  != null){
				System.err.println(pedra);
			}
			
			e.printStackTrace();
		}
		
	}

	private DominoEventBroadcaster configuraEventListners(final Dupla dupla1, final Dupla dupla2) {
                final DominoEventBroadcaster broadcaster = new DominoEventBroadcaster();

		jogadorAtento(broadcaster, dupla1.getJogador1());
		jogadorAtento(broadcaster, dupla1.getJogador2());
		jogadorAtento(broadcaster, dupla2.getJogador1());
		jogadorAtento(broadcaster, dupla2.getJogador2());
                return broadcaster;
		
	}

	private void jogadorAtento(final DominoEventBroadcaster eventBroadcaster, final Jogador jogador) {
		if(jogador instanceof DominoEventListener){
			eventBroadcaster.addEventListener((DominoEventListener)jogador,false);
		}
	}

	private Dupla getDuplaVencedora(final ResultadoPartida resultado) {
		return dupla1.contem(resultado.getVencedor())?dupla1:dupla2;
	}

	private void atualizaPlacar(final Dupla duplaDoVencdor , final Vitoria tipoDeBatida, final int multiplicadorDobrada) {
		duplaDoVencdor.adicionaPontos(tipoDeBatida.getPontos() * multiplicadorDobrada);
	}

	private boolean alguemVenceu() {
		return dupla1.getPontos() >= 6 || dupla2.getPontos() >= 6;
	}

	public void addEventListener(final DominoEventListener eventListener) {
		this.eventBroadcaster.addEventListener(eventListener,true);
	}
}
