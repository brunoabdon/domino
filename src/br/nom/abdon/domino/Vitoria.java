package br.nom.abdon.domino;

/**
 * Os tipos de batida que dá pra se ganhar uma partida.
 * @author bruno
 */
public enum Vitoria {
    CONTAGEM_DE_PONTOS, BATIDA_SIMPLES, SEIS_CARROCAS_NA_MAO, CARROCA(2), LA_E_LO(3), CRUZADA(4);

    private final int pontos;

    private Vitoria() {
        this(1);
    }

    private Vitoria(int pontos) {
        this.pontos = pontos;
    }

    /**
     * Quantos pontos vale essa vitória.
     * 
     * @return Quantos pontos vale a vitória.
     */
    public int getPontos(){
            return this.pontos;
    }
}
