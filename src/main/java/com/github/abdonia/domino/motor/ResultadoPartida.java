package com.github.abdonia.domino.motor;

class ResultadoPartida {
	
    private final JogadorWrapper jogadorRelevante;

    public static final ResultadoPartida EMPATE = new ResultadoPartida();

    private ResultadoPartida() {
        this(null);
    }

    protected ResultadoPartida(final JogadorWrapper jogadorRelevante) {
        this.jogadorRelevante = jogadorRelevante;
    }

    JogadorWrapper getJogadorRelevante() {
        return jogadorRelevante;
    }
}