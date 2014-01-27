package br.nom.abdon.domino;


public class Jogo {

	private Dupla dupla1, dupla2;

	public Jogo() {
		jogadoresSentam();
		Dupla duplaVencedoraDaPartidaAnterior = null;
		while(!alguemVenceu()){
			Partida partida = new Partida(dupla1, dupla2);
			partida.jogar(duplaVencedoraDaPartidaAnterior);
			
			ResultadoPartida resultado = jogaPartida();
			atualizaPlacar(resultado);
			duplaVencedoraDaPartidaAnterior = getDuplaVencedora(resultado);
		}
		
	}

	private Dupla getDuplaVencedora(ResultadoPartida resultado) {
		return dupla1.contem(resultado.getVencedor())?dupla1:dupla2;
	}
	
	private void jogadoresSentam() {
		
	}
	

	private ResultadoPartida jogaPartida() {
	}

	private void atualizaPlacar(ResultadoPartida resultado) {
		if(resultado == ResultadoPartida.EMPATE){
			//faz p que mesmo? acumula ou dobra pra proxima
		} else {
			Jogador vencedor = resultado.getVencedor();
			Dupla duplaDoVencdor = dupla1.contem(vencedor)?dupla1:dupla2;
			dupla2.adicionaPontos(resultado.getTipoDeVitoria().getPontos());
		}

	}

	private boolean alguemVenceu() {
		return dupla1.getPontos() >= 6 || dupla2.getPontos() >= 6;
	}

	public Dupla getDupla1() {
		return dupla1;
	}

	public Dupla getDupla2() {
		return dupla2;
	}



	
	
}
