package com.github.abdonia.domino.app;

import java.util.List;

class DominoYAMLConfig {

    private List<String> implementacoes;

    private JogadorConfig[] jogadores;
    
    private JogadorConfig[] dupla0, dupla1;
    
    private List<String> listeners;
    
    private String randomGoddess;

    public DominoYAMLConfig() {
    }
    
    public List<String> getImplementacoes() {
        return implementacoes;
    }

    public void setImplementacoes(final List<String> implementacoes) {
        this.implementacoes = implementacoes;
    }

    public List<String> getListeners() {
        return listeners;
    }

    public void setListeners(final List<String> listeners) {
        this.listeners = listeners;
    }

    public String getRandomGoddess() {
        return randomGoddess;
    }

    public void setRandomGoddess(String randomGoddess) {
        this.randomGoddess = randomGoddess;
    }

    @Override
    public String toString() {
        return "DominoYAMLConfig [implementacoes=" + implementacoes
                + ", dupla0=" + dupla0 + ", dupla1=" + dupla1 + ", listeners="
                + listeners + ", randomGoddess=" + randomGoddess + "]";
    }

    public JogadorConfig[] getDupla0() {
        return dupla0;
    }

    public void setDupla0(JogadorConfig[] dupla0) {
        this.dupla0 = dupla0;
    }

    public JogadorConfig[] getDupla1() {
        return dupla1;
    }

    public void setDupla1(JogadorConfig[] dupla1) {
        this.dupla1 = dupla1;
    }

    public JogadorConfig[] getJogadores() {
        return jogadores;
    }

    public void setJogadores(JogadorConfig[] jogadores) {
        this.jogadores = jogadores;
    }
}

