package br.nom.abdon.domino.ui;

import br.nom.abdon.domino.Lado;

public class CalculadorDeCoordenadasDePedrasImpl implements CalculadorDeCoordenadaDePedras {
	
	private static enum Direcao {ESQUERDA, SUBINDO, DIREITA, DESCENDO};
	
	private float larguraDaArea, alturaDaArea, larguraDasPedras, alturaDasPedras;
	
	class CalculadorLateral {

		private final Lado lado;
		private Direcao direcao;
		private float limiteHorizontal, limiteVertical;
		
		private float x, y;
		private boolean invertido;
		
		public CalculadorLateral(Lado lado) {
			this.lado = lado;

			if(lado == Lado.ESQUERDO){
				this.direcao = Direcao.ESQUERDA;
				this.limiteHorizontal = (larguraDaArea / 2) - (alturaDasPedras / 2);
			} else {
				this.direcao = Direcao.DIREITA;
				this.limiteHorizontal = (larguraDaArea / 2) + (alturaDasPedras / 2);
			}
		}
		
		
		public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca) {
			if(!cabe()){
				fazCurva();
			}
			
			switch (this.direcao) {
				case ESQUERDA:
					this.x = limiteHorizontal;
					this.y = limiteVertical;
					this.invertido = this.lado == Lado.DIREITO;
					
					this.limiteHorizontal+= ehCarroca?larguraDasPedras:alturaDasPedras;
					
					break;
				
				case SUBINDO: 
			default:
				break;
			}
			
		}


		private void fazCurva() {
			// TODO Auto-generated method stub
			
		}


		private boolean cabe() {
			// TODO Auto-generated method stub
			return false;
		}
		
		

	}

	private CalculadorLateral calculadorEsquerda, calculadorDireita;
	

	@Override
	public void init(float larguraDaArea, float alturaDaArea,
			float comprimentoDasPedras) {
		
		this.calculadorEsquerda = new CalculadorLateral(Lado.ESQUERDO);
		this.calculadorDireita = new CalculadorLateral(Lado.DIREITO);
		
		this.larguraDaArea = larguraDaArea;
		this.alturaDaArea = alturaDaArea;
		this.larguraDasPedras = comprimentoDasPedras/2;
		this.alturaDasPedras = comprimentoDasPedras;

	}

	@Override
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Posicao getPosicao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean direcaoFisicaInvertida() {
		// TODO Auto-generated method stub
		return false;
	}

}
