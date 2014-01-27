package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Vitoria;

class ResultadoPartida {
	
	private final Jogador vencedor;
	private final Vitoria tipoDeVitoria;
	
	public static final ResultadoPartida EMPATE = new ResultadoPartida(null,null);
	
	public ResultadoPartida(Vitoria tipoDeVitoria, Jogador vencedor) {
		this.tipoDeVitoria = tipoDeVitoria;
		this.vencedor = vencedor;
	}

	public Jogador getVencedor() {
		return vencedor;
	}

	public Vitoria getTipoDeVitoria() {
		return tipoDeVitoria;
	}
	
}
