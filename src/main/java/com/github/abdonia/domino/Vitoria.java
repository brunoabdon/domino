package com.github.abdonia.domino;

/**
 * Os tipos de batida que dá pra se ganhar uma partida.
 * @author bruno
 */
public enum Vitoria {
    CONTAGEM_DE_PONTOS(1), 
    BATIDA_SIMPLES(1), 
    SEIS_CARROCAS_NA_MAO(1), 
    CARROCA(2), 
    LA_E_LO(3), 
    CRUZADA(4);

    private final int pontos;

    private Vitoria(final int pontos) {
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