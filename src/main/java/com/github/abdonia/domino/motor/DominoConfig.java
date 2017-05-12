/*
 * Copyright (C) 2017 Bruno Abdon
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

import java.util.ArrayList;
import java.util.List;


/**
 * As configurações de um {@link com.github.abdonia.domino.motor.Jogo Jogo de 
 * dominó}, ou seja, quais jogadores vão participar (quais são seus nomes e suas
 * classes) e quais os 
 * {@link com.github.abdonia.domino.eventos.DominoEventListener listeners} que
 * devem ser registrados no jogo.
 * 
 * @author Bruno Abdon
 */
public class DominoConfig {

    private final String[] nomes = new String[4];
    private final String[] classes  = new String[4];

    private String nomeRandomizadora;

    private List<String> eventListeners = new ArrayList<>();

    public String getNomeJogador1Dupla1() {
        return this.nomes[0];
    }

    public void setNomeJogador1Dupla1(final String nomeJogador1Dupla1) {
        this.nomes[0] = nomeJogador1Dupla1;
    }

    public String getNomeJogador2Dupla1() {
        return this.nomes[1];
    }

    public void setNomeJogador2Dupla1(final String nomeJogador2Dupla1) {
        this.nomes[1] = nomeJogador2Dupla1;
    }

    public String getNomeJogador1Dupla2() {
        return this.nomes[2];
    }

    public void setNomeJogador1Dupla2(final String nomeJogador1Dupla2) {
        this.nomes[2] = nomeJogador1Dupla2;
    }

    public String getNomeJogador2Dupla2() {
        return this.nomes[3];
    }

    public void setNomeJogador2Dupla2(final String nomeJogador2Dupla2) {
        this.nomes[3] = nomeJogador2Dupla2;
    }

    public String getClasseJogador1Dupla1() {
        return classes[0];
    }

    public void setClasseJogador1Dupla1(final String classeJogador1Dupla1) {
        this.classes[0] = classeJogador1Dupla1;
    }

    public String getClasseJogador2Dupla1() {
        return classes[1];
    }

    public void setClasseJogador2Dupla1(final String classeJogador2Dupla1) {
        this.classes[1] = classeJogador2Dupla1;
    }

    public String getClasseJogador1Dupla2() {
        return classes[2];
    }

    public void setClasseJogador1Dupla2(final String classeJogador1Dupla2) {
        this.classes[2] = classeJogador1Dupla2;
    }

    public String getClasseJogador2Dupla2() {
        return classes[3];
    }

    public void setClasseJogador2Dupla2(final String classeJogador2Dupla2) {
        this.classes[3] = classeJogador2Dupla2;
    }

    /**
     * Seta o nome e a classe de um dos dois jogadores de uma das duas dupplas
     * @param nome O nome do jogador
     * @param classe O nome da classe do jogador
     * @param dupla O número da dupla (1 o 2)
     * @param jogador O número do jogador na dupla (1 ou 2)
     */
    public void setNomeEClasse(
            final String nome, 
            final String classe, 
            final int dupla, 
            final int jogador){
        
        //nao tenho certeza se deveria validar nesse ponto....
        this.validaParametros(nome, classe, dupla, jogador);
        
        final int index = (dupla-1)*2 + (jogador-1);
        
        nomes[index] = nome;
        classes[index] = classe;
    }

    private void validaParametros(
            final String nome, 
            final String classe,
            final int dupla, 
            final int jogador) 
                throws IllegalArgumentException, NullPointerException {
        if(dupla != 1 && dupla != 2){
            throw new IllegalArgumentException("Dupla invalida: " + dupla);
        }

        if(jogador != 1 && jogador != 2){
            throw new IllegalArgumentException("Jogador invalido: " + dupla);
        }
        
        if(nome == null || classe == null){
            throw new NullPointerException(
                    String.format(
                            "Nome ou clase nulos: [%s] [%s] ",
                            nome,
                            classe)
            );
        }
    }

    public List<String> getEventListeners() {
        return this.eventListeners;
    }

    public void setEventListeners(final List<String> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void addEventListener(final String eventListener) {
        this.eventListeners.add(eventListener);
    }
    
    public String getNomeRandomizadora() {
        return nomeRandomizadora;
    }

    public void setNomeRandomizadora(String nomeRandomizadora) {
        this.nomeRandomizadora = nomeRandomizadora;
    }
}
