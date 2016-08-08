package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Pedra;

class PedraBebaException extends BugDeJogadorException {

    private final Pedra pedraBeba;

    public PedraBebaException(
            final Jogador jogadorSacana, 
            final Pedra pedraBeba) {
        super("Jogou pedra beba!", jogadorSacana);
        this.pedraBeba = pedraBeba;
    }

    public PedraBebaException(final Pedra pedraBeba) {
            this(null,pedraBeba);
    }

    public Pedra getPedraBeba() {
            return pedraBeba;
    }
}
