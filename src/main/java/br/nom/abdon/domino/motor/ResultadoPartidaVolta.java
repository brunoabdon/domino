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
