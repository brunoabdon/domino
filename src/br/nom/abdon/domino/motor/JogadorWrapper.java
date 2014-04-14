package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Pedra;

public final class JogadorWrapper implements Jogador {

	private final String nome;
	private final Jogador wrapped;
	private Dupla dupla;
	
	public JogadorWrapper(Jogador wrapped, String nome) {
		
		if(nome == null) throw new IllegalArgumentException("João SemNome não joga");
		if(wrapped == null) throw new IllegalArgumentException("bug");
		
		this.wrapped = wrapped;
		this.nome = nome;
	}
	
	@Override
	public void recebeMao(Pedra[] pedras) {
		wrapped.recebeMao(pedras);

	}

	@Override
	public Jogada joga(Mesa mesa) {
		return wrapped.joga(mesa);
	}

	@Override
	public int vontadeDeComecar() {
		return wrapped.vontadeDeComecar();
	}

	public Dupla getDupla() {
		return dupla;
	}

	public void setDupla(Dupla dupla) {
		this.dupla = dupla;
	}

	public String getNome() {
		return nome;
	}

	public Jogador getWrapped() {
		return wrapped;
	}
	
	@Override
	public String toString() {
		return this.getNome() + " [" + wrapped.getClass() + "]";
	}

}
