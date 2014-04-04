package br.nom.abdon.domino.ui.j2d;

import br.nom.abdon.domino.Lado;

public interface CalculadorDeCoordenadaDePedras {

	public void init(float larguraDaArea, float alturaDaArea, float comprimentoDasPedras);
	
	public void calculaOndeDesenharAPedra(Lado lado, boolean ehCarroca);
	
	public float getX();
	public float getY();
	public Direcao getDirecao();

	
}
