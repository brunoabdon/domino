package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;

class PedraBebaException extends BugDeJogadorException {

	private final Pedra pedraBeba;
	
	public PedraBebaException(Jogador jogadorSacana, Pedra pedraBeba) {
		super("Jogou pedra beba!", jogadorSacana);
		this.pedraBeba = pedraBeba;
	}

	public PedraBebaException(Pedra pedra) {
		this(null,pedra);
	}

	public Pedra getPedraBeba() {
		return pedraBeba;
	}

}
