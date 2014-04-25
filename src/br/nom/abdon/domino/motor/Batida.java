/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.motor;

import br.nom.abdon.domino.Vitoria;

/**
 *
 * @author bruno
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
