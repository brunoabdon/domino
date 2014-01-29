package br.nom.abdon.domino.ui;

import br.nom.abdon.domino.Lado;

public class CalculadorDeCordenadaSimples implements CalculadorDeCordenadaDePedras {

	private enum Estrategia {SEGUIR_RETO, VIRAR_ESQUERDA};
	
	private float larguraDaArea, alturaDaArea;
	private float larguraDasPedras, alturaDasPedras;
	
//	private Caminho caminhoCabecaDireita, caminhoCabecaEsquerda;
//	private float coordenadaLimiteCabecaDireita, coordenadaLimiteCabecaEsquerda; 
	
	
	private float x,y;
	private Posicao posicao;
	
	
	@Override
	public void init(float larguraDaArea, float alturaDaArea, float larguraDasPedras, float alturaDasPedras) {
		this.larguraDaArea = larguraDaArea;
		this.alturaDaArea = alturaDaArea;
		this.larguraDasPedras = larguraDasPedras;
		this.alturaDasPedras = alturaDasPedras;
	}

	
	
	@Override
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca) {
		
		
//		if(cabe(lado)){ //segue reto
//			this.x = this.x - (ehCarroca ? larguraDasPedras : alturaDasPedras);
//			if(lado == Lado.DIREITO){
//				this.y += alturaDasPedras;
//			} else if(ehCarroca)
////				th
//				
////			this.y = lado == Lado.DIREITO ? () : (ehCarroca ? : );
//		} else { //dobra a esquerda
//			
//		}
//		
//		
//		if(es)
//		
//		
//		
//		if(caminhoCabecaDireita == null){
//			//primeiraPeca. bota em peh no meio!
//			this.x = (larguraDaArea - alturaDasPedras) / 2;
//			this.y = (alturaDaArea - larguraDasPedras) / 2;
//			this.posicao = Posicao.EM_PE;
//			this.direcaoFisicaInvertida = false;
//			
//			this.caminhoCabecaDireita = Caminho.DIREITA;
//			this.caminhoCabecaEsquerda = Caminho.ESQUERDA;
//
//		} else {
//			if(lado == Lado.DIREITO){
//				if(caminhoCabecaDireita == Caminho.DIREITA){
//					if(cabe(Caminho.DIREITA)){
//						if(ehCarroca){
//							
//						}
//						this.x += larguraDasPedras; 
//						//this.y = this.y =
//						this.posicao = Posicao.EM_PE;
//						this.direcaoFisicaInvertida = false;
//						
//						this.caminhoCabecaDireita = Caminho.DIREITA;
//						this.caminhoCabecaEsquerda = Caminho.ESQUERDA;
//					}
//				}
//			}
//		}

	}

	private boolean cabe(Lado lado) {
		return true;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public Posicao getPosicao() {
		return posicao;
	}

	@Override
	public boolean direcaoFisicaInvertida() {
		return false;
	}

}
