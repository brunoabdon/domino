package com.github.abdonia.domino.app;

class JogadorConfig {

    private String nome;
    private String classe;
    
    public JogadorConfig() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Jogador [" + nome + "," + classe + "]";
    }
}
