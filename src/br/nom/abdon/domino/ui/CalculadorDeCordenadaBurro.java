package br.nom.abdon.domino.ui;

import br.nom.abdon.domino.Lado;

public class CalculadorDeCordenadaBurro implements CalculadorDeCordenadaDePedras {

	private int quantidadePraDireita, quantidadePraEsquerda;
	
	private float xsPraDireita[]; 
	private float ysPraDireita[];
	private Posicao posicoesPraDireita[];
	private boolean direcoesInvertidas[];

	private Lado qualFoiOLado;
	
	
	@Override
	public void init(float larguraDaArea, float alturaDaArea, float larguraDasPedras, float alturaDasPedras) {
		
		xsPraDireita = new float[28];
		ysPraDireita = new float[28];
		posicoesPraDireita = new Posicao[28];
		direcoesInvertidas = new boolean[28];

		
		
		xsPraDireita[0] = (larguraDaArea - alturaDasPedras) / 2;
		ysPraDireita[0] = (alturaDaArea - larguraDasPedras) / 2;
		posicoesPraDireita[0] = Posicao.DEITADO;
		
		int i = 1;
		for (; i < ((larguraDaArea/2)/alturaDasPedras); i++) {
			xsPraDireita[i] = xsPraDireita[i-1] + alturaDasPedras;
			ysPraDireita[i] = ysPraDireita[i-1];
			posicoesPraDireita[i] = Posicao.DEITADO;
			direcoesInvertidas[i] = false;
		}
		
		xsPraDireita[i] = xsPraDireita[i-1] + (alturaDasPedras - larguraDasPedras); 
		ysPraDireita[i] = ysPraDireita[i-1] - alturaDasPedras;
		posicoesPraDireita[i] = Posicao.EM_PE;
		direcoesInvertidas[i] = false;
		
		i++;
		
		for (; i < xsPraDireita.length; i++) {
			xsPraDireita[i] = xsPraDireita[i-1] - alturaDasPedras;
			ysPraDireita[i] = ysPraDireita[i-1];
			posicoesPraDireita[i] = Posicao.DEITADO;
			direcoesInvertidas[i] = true;
		}
		
		
	}

	
	
	@Override
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca) {
		this.qualFoiOLado = lado;
		if(lado == Lado.DIREITO){
			quantidadePraDireita++;
		} else {
			quantidadePraEsquerda++;
		}
		
	}

	@Override
	public float getX() {
		return this.qualFoiOLado == Lado.DIREITO ? xsPraDireita[quantidadePraDireita] : 0;
	}

	@Override
	public float getY() {
		return this.qualFoiOLado == Lado.DIREITO ? ysPraDireita[quantidadePraDireita] : 0;
	}

	@Override
	public Posicao getPosicao() {
		return this.qualFoiOLado == Lado.DIREITO ? posicoesPraDireita[quantidadePraDireita] : null;
	}

	@Override
	public boolean direcaoFisicaInvertida() {
		return direcoesInvertidas[quantidadePraDireita];
	}

}
