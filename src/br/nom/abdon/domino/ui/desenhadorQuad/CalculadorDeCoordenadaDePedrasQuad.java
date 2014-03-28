package br.nom.abdon.domino.ui.desenhadorQuad;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.ui.CalculadorDeCoordenadaDePedras;
import br.nom.abdon.domino.ui.Direcao;

public class CalculadorDeCoordenadaDePedrasQuad implements
		CalculadorDeCoordenadaDePedras {

	private int quadradinhosPorLinha, quadradinhosPorColuna;
	private float ladoDeUmQuadrado;
	private float larguraDaArea, alturaDaArea;
	
	
	private TelaQuadriculada telaQuadriculada;
	private Direcao direcao;
	private float x, y;
	
	@Override
	public void init(float larguraDaArea, float alturaDaArea,
			float comprimentoDasPedras) {
		
		this.larguraDaArea = larguraDaArea;
		this.alturaDaArea = alturaDaArea;
		
		float larguraDasPedras = comprimentoDasPedras/2;
		this.ladoDeUmQuadrado = larguraDasPedras;

		this.quadradinhosPorLinha = (int) (larguraDaArea/ larguraDasPedras);
		this.quadradinhosPorColuna = (int) (alturaDaArea / comprimentoDasPedras);
		
		this.telaQuadriculada = new TelaQuadriculada(quadradinhosPorLinha, quadradinhosPorColuna);
		
	}

	@Override
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca) {
		final Vaga vaga = this.telaQuadriculada.getVaga(lado, ehCarroca);
		
		
		final float[] coordenadaPraDesenhar = calculaCoordenadaPraDesenhar(vaga); 
		
		this.x = coordenadaPraDesenhar[0];
		this.y = coordenadaPraDesenhar[1];
		
		this.direcao = vaga.getDirecao();
		if(ehCarroca){
			
			switch (this.direcao) {
			case PRA_DIREITA: this.direcao = Direcao.PRA_BAIXO; break;
			case PRA_BAIXO: this.direcao = Direcao.PRA_DIREITA; break;
			case PRA_ESQUERDA: this.direcao = Direcao.PRA_BAIXO; break;
//			case PRA_pra: this.direcao = Direcao.PRA_BAIXO; break;
	
			default:
				break;
			}
		}

	}

	private float[] calculaCoordenadaPraDesenhar(Vaga vaga){
		
		//acha a coordenada do pontinho no alto à esquerda do quadrado
		final float[] coordenadaDoQuadrado = calculaCoordenadaQuadrado(vaga.getLocalizacao());
		
		final float xQuadrado = coordenadaDoQuadrado[0];
		final float yQuadrado = coordenadaDoQuadrado[1];

		float xDesenho = xQuadrado;
		float yDesenho = yQuadrado; //a principio

		final Direcao direcao = vaga.getDirecao();
		
		//se for carroca, desloca um pouquinho pra desenhar. se for normal, desenha ai mesmo
		
		if(vaga.ehDeCarroca()){
			if(direcao == Direcao.PRA_DIREITA){
				yDesenho = yQuadrado - ladoDeUmQuadrado/2;
			} else if (direcao == Direcao.PRA_BAIXO){
				xDesenho = xQuadrado - ladoDeUmQuadrado/2;
			} else if(direcao == Direcao.PRA_ESQUERDA){
				xDesenho = xQuadrado + ladoDeUmQuadrado;
			} else { //Direcao.PRA_CIMA 
				xDesenho = xQuadrado - ladoDeUmQuadrado/2;
			}
			
		} else {

			final boolean andandoNoSentidoDecrescenteDasCoordenadas = 
					direcao == Direcao.PRA_ESQUERDA || direcao == Direcao.PRA_CIMA;

			if(andandoNoSentidoDecrescenteDasCoordenadas){
			} else if(direcao == Direcao.PRA_CIMA) {
				yDesenho = yQuadrado - ladoDeUmQuadrado;
			} else { //Direcao.PRA_ESQUERDA
				xDesenho = xQuadrado - ladoDeUmQuadrado;
			}
		}

		final float[] coordenadaPraDesenhar = coordenadaDoQuadrado;  //hehe. reaproveitando
		coordenadaPraDesenhar[0] = xDesenho;
		coordenadaPraDesenhar[1] = yDesenho;
		
		return coordenadaPraDesenhar;
	}

	private float[] calculaCoordenadaQuadrado(Localizacao localizacao) {
		return new float[]{localizacao.getX() * ladoDeUmQuadrado, localizacao.getY()*ladoDeUmQuadrado};
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public Direcao getDirecao() {
		return this.direcao;
	}

}
