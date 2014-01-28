package br.nom.abdon.domino.motor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.motor.util.IteratorReadOnly;

class MesaImpl implements br.nom.abdon.domino.Mesa{

	private Deque<Pedra> listaDePedras;
	private Numero numeroEsquerda, numeroDireita;

	protected MesaImpl() {
		this.listaDePedras = new ArrayDeque<Pedra>(28-4);
	}
	
	public boolean taVazia(){
		return this.listaDePedras.isEmpty();
	}
	
	private boolean podeJogar(Pedra pedra, Lado lado){
		if(this.taVazia()) return true;
		
		Numero cabeca = lado == Lado.ESQUERDO ? numeroEsquerda : numeroDireita; 
		
		return pedra.temNumero(cabeca);
				
	}
	
	/**
	 * Coloca uma {@link Pedra} num dado {@link Lado} da {@link Mesa}, lenvantando uma exceção 
	 * se nao puder colocar essa porra.
	 * 
	 * @param pedra A pedra que é pra colocar
	 * @param lado Onde botar ela.
	 * @throws PedraBebaException Se não puder colocar essa pedra nesse lugar.
	 */
	protected void coloca(Pedra pedra, Lado lado) throws PedraBebaException{

		if(lado == null && (numeroEsquerda == numeroDireita)){
			lado = Lado.ESQUERDO;
		}
		
		if(!podeJogar(pedra, lado)){
			throw new PedraBebaException(pedra);
		}
		
		if(lado == Lado.ESQUERDO){
			listaDePedras.addFirst(pedra);
			numeroEsquerda = novaCabeca(numeroEsquerda, pedra);
		} else {
			listaDePedras.addLast(pedra);
			numeroDireita = novaCabeca(numeroDireita, pedra);
		}
	}
	
	private Numero novaCabeca(Numero cabecaAtual, Pedra pedraQueFoiJogada){
		Numero primeiroNumeroDaPedra = pedraQueFoiJogada.getPrimeiroNumero();
		return primeiroNumeroDaPedra == cabecaAtual ? pedraQueFoiJogada.getSegundoNumero() : primeiroNumeroDaPedra;
	}

	public Numero getNumeroEsquerda() {
		return numeroEsquerda;
	}
	public Numero getNumeroDireita() {
		return numeroDireita;
	}

	@Override
	public Iterator<Pedra> iterator() {
		return iteratorEsquedaPraDireita();
	}

	public Iterator<Pedra> iteratorEsquedaPraDireita() {
		return new IteratorReadOnly<Pedra>(listaDePedras.iterator());
	}

	public Iterator<Pedra> iteratorDireitaPraEsquerda() {
		return new IteratorReadOnly<Pedra>(listaDePedras.descendingIterator());
	}
	
	public int quantasPecas(){
		return this.listaDePedras.size();
	}
	
	public Pedra[] toArray(){
		return this.listaDePedras.toArray(new Pedra[this.listaDePedras.size()]);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (Pedra pedra : listaDePedras) {
			sb.append(pedra);
		}
		return sb.toString();
		
	}
}
