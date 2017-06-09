/*
 * Copyright (C) 2016 Bruno Abdon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Vontade;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

/**
 * Uma dupla de {@link JogadorWrapper jogadores} num jogo, com seu placar.
 * 
 * @author Bruno Abdon
 */
class Dupla {
    private int pontos;

    private final JogadorWrapper jogador0;
    private final JogadorWrapper jogador1;

    /**
     * Cria uma dupla com dois {@link JogadorWrapper jogadores}.
     * @param jogador0 O primeiro jogador da dupla.
     * @param jogador1 O segundo jogador da dupla.
     */
    Dupla(final JogadorWrapper jogador0, final JogadorWrapper jogador1) {
        this.jogador0 = jogador0;
        this.jogador1 = jogador1;

        this.pontos = 0;
    }
    
    /**
     * Retorna o {@link JogadorWrapper jogador} 0 ou 1, de acordo com o 
     * parâmetro. 
     * <p>O método não valida que o se o parâmetro está entre 0 e 1. Um valor
     * indeterminado e retornado caso outro número seja pedido.
     * 
     * @param jogador O número 0 ou 1.
     * @return O {@link JogadorWrapper jogador} 0 ou 1.
     */
    public JogadorWrapper getJogador(final int jogador){
        return jogador == 0 ? jogador0 : jogador1;
    }
    

    /**
     * Retorna quantos pontos essa dupla tem.
     * @return quantos pontos essa dupla tem.
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Adiciona uma dada quantidade de pontos ao placar da dupla.
     * @param pontos Quntos pontos adicionar.
     */
    void adicionaPontos(final int pontos) {
        this.pontos += pontos;
    }

    /**
     * Diz se um dado {@link JogadorWrapper jogadore} pertence a essa dupla.
     * @param jogador Um jogador.
     * @return {@code true} se e somente se este jogador
     * for um dos 2 jogadores da dupla.
     */
    boolean contem(final JogadorWrapper jogador){
        return this.jogador0 == jogador || this.jogador1 == jogador;
    }

    /**
     * Retorna um número positivo se o <em>jogador0</em> {@link 
     * JogadorWrapper#getVontadeDeComecar()  tiver mais vontade de começar}, 
     * negativo se for o <em>jogador1</em> que tiver, ou zero caso empatem na 
     * vontade.
     * 
     * @return um número positivo se o <em>jogador0</em> tiver mais vontade de
     * começar, negativo se o <em>jogador1</em> tiver, ou Zero caso empatem na 
     * vontade.
     * 
     * @throws BugDeJogadorException Se algum jogador se enrolar até pra dizer 
     * se quer ou não começar (com a {@link BugDeJogadorException#getFalha() 
     * falha} {@link Falha#NAO_SABE_SE_COMECE}).
     */
    int quemTemMaisVontadeDeComecar() throws BugDeJogadorException {
        final Vontade vontadeDo0 = perguntaVontade(jogador0);
        final Vontade vontadeDo1 = perguntaVontade(jogador1);

        return vontadeDo0.compareTo(vontadeDo1); 
    }

    /**
     * Pergunta ao dado {@link JogadorWrapper jogador} {@link 
     * JogadorWrapper#getVontadeDeComecar() qual a vontade dele começar a partida},
     * levantando {@link BugDeJogadorException} caso ele retorne 
     * {@code null}.
     * 
     * @param jogador Um jogador.
     * 
     * @return A {@link JogadorWrapper#getVontadeDeComecar() vontade dele de 
     * começar a partida} (caso ele responda corretamente com um valor não 
     * nulo).
     * @throws BugDeJogadorException caso o jogador retorne {@code null}
     * erroneamente.
     */
    private static Vontade perguntaVontade(
            final JogadorWrapper jogador) 
                throws BugDeJogadorException {

        final Vontade vontade = jogador.getVontadeDeComecar();

        if(vontade == null){
            throw new BugDeJogadorException(
                    Falha.NAO_SABE_SE_COMECE,
                    jogador);
        }
        
        return vontade;
    }
    
    /**
     * Diz se essa dupla já tem pelo menos 6 {@link #getPontos() pontos}.
     * @return 
     */
    boolean venceu() {
        return this.getPontos() >= 6;
    }

    @Override
    public String toString() {
        return this.jogador0 + " e "  + this.jogador1 + ", " + this.pontos;
    }
}