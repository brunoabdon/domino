package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Vitoria;

class ResultadoPartida {
	
	private final JogadorWrapper vencedor;
	private final Vitoria tipoDeVitoria;
	
	public static final ResultadoPartida EMPATE = new ResultadoPartida(null,null);
	
	public ResultadoPartida(Vitoria tipoDeVitoria, JogadorWrapper vencedor) {
		this.tipoDeVitoria = tipoDeVitoria;
		this.vencedor = vencedor;
	}

	public JogadorWrapper getVencedor() {
		return vencedor;
	}

	public Vitoria getTipoDeVitoria() {
		return tipoDeVitoria;
	}
	
}
