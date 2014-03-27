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
		Direcao direcao = vaga.getDirecao();
		
		final float[] coordenadaPraDesenhar = calculaCoordenadaPraDesenhar(vaga); 
		
		this.x = coordenadaPraDesenhar[0];
		this.y = coordenadaPraDesenhar[1];
		
		this.direcao = direcao;

	}

	private float[] calculaCoordenadaPraDesenhar(Vaga vaga){
		
		//acha a coordenada do pontinho do meio do quadrado
		final float[] coordenadaDoQuadrado = calculaCoordenadaQuadrado(vaga.getLocalizacao());
		
		final float xQuadrado = coordenadaDoQuadrado[0];
		final float yQuadrado = coordenadaDoQuadrado[1];
		
		final float[] coordenadaPraDesenhar = coordenadaDoQuadrado;  //hehe. reaproveitando

		//acha a coordenada do ponto no alto à esquerda do quadrado
		coordenadaPraDesenhar[0] = xQuadrado - ladoDeUmQuadrado/2;
		coordenadaPraDesenhar[1] = yQuadrado - ladoDeUmQuadrado/2;

		return coordenadaPraDesenhar;
	}

	private float[] calculaCoordenadaQuadrado(Localizacao localizacao) {
		return null;
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
