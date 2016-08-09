package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Vitoria;

/**
 * Um {@link ResultadoPartida} indicando que um determinado {@link Jogador} 
 * bateu.
 * 
 */
class Batida extends ResultadoPartida{

    private final Vitoria tipoDeVitoria;

    public Batida(final Vitoria tipoDeVitoria, final JogadorWrapper vencedor) {
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
