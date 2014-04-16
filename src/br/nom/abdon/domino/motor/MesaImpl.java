package br.nom.abdon.domino.motor;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.motor.util.IteratorReadOnly;

class MesaImpl implements br.nom.abdon.domino.Mesa{

	private final Deque<Pedra> listaDePedras;
	private Numero numeroEsquerda, numeroDireita;
        private final Collection<Pedra>[] maos;

	protected MesaImpl() {
            this.listaDePedras = new ArrayDeque<>(28-4);
            this.maos = new Collection[4];
	}

        Collection<Pedra>[] getMaos() {
            return maos;
        }

        @Override
        public boolean taVazia(){
		return this.listaDePedras.isEmpty();
	}
	
	private boolean podeJogar(final Pedra pedra, final Lado lado){
		//if(this.taVazia()) throw new IllegalStateException("comecando com pedra jah na mesa?");
		
		Numero cabeca = lado == Lado.ESQUERDO ? numeroEsquerda : numeroDireita; 
		
		return pedra.temNumero(cabeca);
				
	}
        @Override
        public int quantasPedrasOJogadoresTem(int qualJogador) {
            if(qualJogador < 1 || qualJogador >4 ) 
                throw new IllegalArgumentException("Dominó se joga com 4.");
            System.out.println(qualJogador);
            return this.maos[qualJogador-1].size();
        }

	/**
	 * Coloca uma {@link Pedra} num dado {@link Lado} da {@link Mesa}, 
         * lenvantando uma exceção se nao puder colocar.
	 * 
	 * @param pedra A pedra que é pra colocar
	 * @param lado Onde botar ela.
	 * @throws PedraBebaException Se não puder colocar essa pedra nesse lugar.
	 */
	protected void coloca(final Pedra pedra, Lado lado) throws PedraBebaException{

		if(taVazia()){
			this.listaDePedras.addFirst(pedra);
			this.numeroEsquerda = pedra.getPrimeiroNumero();
			this.numeroDireita = pedra.getSegundoNumero();
		} else {
			
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

	}
	
	private Numero novaCabeca(final Numero cabecaAtual, final Pedra pedraQueFoiJogada){
		Numero primeiroNumeroDaPedra = pedraQueFoiJogada.getPrimeiroNumero();
		
                return primeiroNumeroDaPedra == cabecaAtual 
                        ? pedraQueFoiJogada.getSegundoNumero() 
                        : primeiroNumeroDaPedra;
	}

        @Override
	public Numero getNumeroEsquerda() {
		return numeroEsquerda;
	}
        @Override
	public Numero getNumeroDireita() {
		return numeroDireita;
	}

	@Override
	public Iterator<Pedra> iterator() {
		return iteratorEsquedaPraDireita();
	}

        @Override
	public Iterator<Pedra> iteratorEsquedaPraDireita() {
		return new IteratorReadOnly<>(listaDePedras.iterator());
	}

        @Override
	public Iterator<Pedra> iteratorDireitaPraEsquerda() {
		return new IteratorReadOnly<>(listaDePedras.descendingIterator());
	}
	
        @Override
	public int quantasPecas(){
		return this.listaDePedras.size();
	}
	
        @Override
	public Pedra[] toArray(){
		return this.listaDePedras.toArray(new Pedra[this.listaDePedras.size()]);
	}

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            listaDePedras.stream().forEach((pedra) -> {
                sb.append(pedra);
            });
            return sb.toString();

        }
}
