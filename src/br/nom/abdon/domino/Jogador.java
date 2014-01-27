package br.nom.abdon.domino;

public interface Jogador {

	public void recebeMao(Pedra[] pedras);
	
	public Jogada joga(Mesa mesa);

	public int vontadeDeComecar();

}
