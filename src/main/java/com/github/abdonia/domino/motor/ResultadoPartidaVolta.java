package com.github.abdonia.domino.motor;

/**
 *
 * @author bruno
 */
class ResultadoPartidaVolta extends ResultadoPartida{

    ResultadoPartidaVolta(final JogadorWrapper jogadorComCincoCarroca) {
        super(jogadorComCincoCarroca);
    }
    
    public JogadorWrapper getJogadorComCincoCarroca() {
        return super.getJogadorRelevante();
    }
}
