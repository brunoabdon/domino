package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Jogador;

class Dupla {
    private int pontos;

    private final JogadorWrapper jogador1;
    private final JogadorWrapper jogador2;

    Dupla(final JogadorWrapper jogador1, final JogadorWrapper jogador2) {
        if(jogador1 == null || jogador2 == null) 
            throw new IllegalArgumentException("Só pode dupla de dois");

        this.jogador1 = jogador1;
        this.jogador2 = jogador2;

        this.pontos = 0;
    }
    public JogadorWrapper getJogador1() {
            return jogador1;
    }
    public JogadorWrapper getJogador2() {
            return jogador2;
    }

    public int getPontos() {
            return pontos;
    }

    void adicionaPontos(final int pontos) {
            this.pontos += pontos;
    }

    boolean contem(final JogadorWrapper jogador){
        return this.jogador1 == jogador || this.jogador2 == jogador;
    }

    /**
     * Retorna um número negativo se o Jogador1 for começar, positivo se o 
     * Jogador 2 for começar, ou zero caso empatem na vontade.
     * 
     * @return O que eu acabei de dizer;
     * @throws BugDeJogadorException Se algum jogador se enrolar até pra dizer 
     * se quer começar ou não.
     */
    int quemComeca() throws BugDeJogadorException {
            int vontadeDo1 = jogador1.vontadeDeComecar();
            validaVontade(vontadeDo1,jogador1);

            int vontadeDo2 = jogador2.vontadeDeComecar();
            validaVontade(vontadeDo2,jogador2);
            return vontadeDo1 - vontadeDo2; 
    }

    private void validaVontade(final int vontade, final Jogador jogador) 
            throws BugDeJogadorException {
        if(vontade < 0 || vontade > 10){
            throw new BugDeJogadorException(
                    "Vontade é de zero a dez só, meu velho. Não inventa.",
                    jogador);
        }
    }
    
    boolean venceu() {
        return this.getPontos() >= 6;
    }

    @Override
    public String toString() {
            return this.jogador1 + " e "  + this.jogador2 + ", " + this.pontos;
    }
}