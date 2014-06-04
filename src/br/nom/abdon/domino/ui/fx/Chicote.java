/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import br.nom.abdon.domino.Numero;

/**
 *
 * @author bruno
 */
class Chicote {

    private Numero numeroExposto;
    private PedraFx pedra;
    private Direcao direcao;

    
    public Chicote(PedraFx pedra, Direcao direcao, Numero numeroExposto) {
        this.pedra = pedra;
        this.direcao = direcao;
        this.numeroExposto = numeroExposto;
    }

    public PedraFx getPedra() {
        return pedra;
    }

    public void setPedra(PedraFx pedra) {
        this.pedra = pedra;
    }

    public Direcao getDirecao() {
        return direcao;
    }

    public void setDirecao(Direcao direcao) {
        this.direcao = direcao;
    }
    
    public Numero getNumeroExposto() {
        return numeroExposto;
    }

    public void setNumeroExposto(Numero numeroExposto) {
        this.numeroExposto = numeroExposto;
    }


}
