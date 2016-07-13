package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Vitoria;

/**
 * Um {@link ResultadoPartida} indicando que um determinado {@link Jogador} 
 * bateu.
 * 
 */
class Batida extends ResultadoPartida{

    private final Vitoria tipoDeVitoria;

    public Batida(Vitoria tipoDeVitoria, JogadorWrapper vencedor) {
        super(vencedor);
        this.tipoDeVitoria = tipoDeVitoria;
    }
    
    public JogadorWrapper getVencedor() {
            return super.getJogadorRelevante();
    }

    public Vitoria getTipoDeVitoria() {
            return tipoDeVitoria;
    }

}
