package br.nom.abdon.domino;

import java.util.Iterator;

public interface Mesa extends Iterable<Pedra>{

	public Numero getNumeroEsquerda();
	
	public Numero getNumeroDireita();

	@Override
	public Iterator<Pedra> iterator();

	public Iterator<Pedra> iteratorEsquedaPraDireita();

	public Iterator<Pedra> iteratorDireitaPraEsquerda();
	
	public int quantasPecas();
	
	public Pedra[] toArray();
	
	public boolean taVazia();


}
