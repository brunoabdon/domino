package br.nom.abdon.domino;

import java.util.ArrayDeque;
import java.util.Deque;

public class Mesa {

	private Deque<Pedra> listaDePedras = new ArrayDeque<Pedra>(28-4);
	
	private Numero numeroEsquerda, numeroDireita;

	public boolean taVazia(){
		return this.listaDePedras.isEmpty();
	}
	
	private boolean podeJogar(Pedra pedra, Lado lado){
		if(this.taVazia()) return true;
		
		Numero cabeca = lado == Lado.ESQUERDO ? numeroEsquerda : numeroDireita; 
		
		return pedra.temNumero(cabeca);
				
	}
	
	/**
	 * Coloca uma pedra num dado lado da mesa, lenvantando uma excecao se nao poder
	 * colocar essa porra.
	 * 
	 * @param pedra
	 * @param lado
	 */
	public void coloca(Pedra pedra, Lado lado){
		
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
	

	
}
