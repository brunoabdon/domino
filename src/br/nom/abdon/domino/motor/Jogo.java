package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.exemplos.JogadorMamao;



public class Jogo {

	private Dupla dupla1, dupla2;
	
	public Jogo(Dupla dupla1, Dupla dupla2) {
		if(dupla1 == null || dupla2 == null) throw new IllegalArgumentException("W.O.!!!");
		this.dupla1 = dupla1;
		this.dupla2 = dupla2;
	}
	
	public void jogar(){
		try {
			Dupla ultimaDuplaQueVenceu = null;
			int multiplicadorDobrada = 1;
			while(!alguemVenceu()){
				System.out.println("Comecando partida. " + dupla1 + " x " + dupla2);
				Partida partida = new Partida(dupla1, dupla2);
				
				ResultadoPartida resultado = partida.jogar(ultimaDuplaQueVenceu);
				if(resultado == ResultadoPartida.EMPATE){
					multiplicadorDobrada *=2;
				} else {
					ultimaDuplaQueVenceu = getDuplaVencedora(resultado);
					atualizaPlacar(ultimaDuplaQueVenceu,resultado.getTipoDeVitoria(),multiplicadorDobrada);
					multiplicadorDobrada = 1;
				}
			}
		} catch (BugDeJogadorException e) {
			System.err.println("Jogador " + e.getJogadorBuguento() + " fez merda.");
			Pedra pedra = e.getPedra();
			if(pedra  != null){
				System.err.println(pedra);
			}
			
			e.printStackTrace();
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


	public static void main(String[] args) {
		Jogador jogador1 = new JogadorMamao();
		Jogador jogador2 = new JogadorMamao();
		Jogador jogador3 = new JogadorMamao();
		Jogador jogador4 = new JogadorMamao();
		
		Dupla dupla1 = new Dupla(jogador1, jogador2);
		Dupla dupla2 = new Dupla(jogador3, jogador4);
		
		Jogo jogo = new Jogo(dupla1, dupla2);
		jogo.jogar();
	}
	
	
	
}
