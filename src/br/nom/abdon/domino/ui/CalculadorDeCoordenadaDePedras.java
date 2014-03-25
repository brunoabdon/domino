package br.nom.abdon.domino.ui;

import br.nom.abdon.domino.Lado;

public interface CalculadorDeCoordenadaDePedras {

	public void init(float larguraDaArea, float alturaDaArea, float comprimentoDasPedras);
	
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca);
	
	public float getX();
	public float getY();
	public Posicao getPosicao();
	public boolean direcaoFisicaInvertida();

	
}
