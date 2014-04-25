/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.motor;

/**
 *
 * @author bruno
 */
class ResultadoPartidaVolta extends ResultadoPartida{

    ResultadoPartidaVolta(JogadorWrapper jogadorComCincoCarroca) {
        super(jogadorComCincoCarroca);
    }
    
    public JogadorWrapper getJogadorComCincoCarroca() {
        return super.getJogadorRelevante();
    }
    
}
