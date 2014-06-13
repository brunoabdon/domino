package br.nom.abdon.domino.motor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Pedra;

public final class JogadorWrapper implements Jogador {

    private final String nome;
    private int cadeira;
    private Collection<Pedra> mao;

    private final Jogador wrapped;

    public JogadorWrapper(Jogador wrapped, String nome) {

        if(nome == null) throw new IllegalArgumentException("João SemNome não joga.");
        if(wrapped == null) throw new IllegalArgumentException("bug");

        this.wrapped = wrapped;
        this.nome = nome;
    }

    @Override
    public void recebeMao(Pedra[] pedras) {
        this.mao = new ArrayList<>(Arrays.asList(pedras));
        wrapped.recebeMao(pedras);
    }

    @Override
    public Jogada joga() {
        return wrapped.joga();
    }

    @Override
    public int vontadeDeComecar() {
        return wrapped.vontadeDeComecar();
    }

    @Override
    public void sentaNaMesa(Mesa mesa, int cadeiraQueSentou) {
        this.cadeira = cadeiraQueSentou;
        wrapped.sentaNaMesa(mesa, cadeiraQueSentou);
    }

    String getNome() {
        return nome;
    }

    int getCadeira() {
        return cadeira;
    }
    
    Collection<Pedra> getMao(){
        return this.mao;
    }
    
    int getNumeroDePontosNaMao(){
        return this.mao.stream().mapToInt(p -> p.getNumeroDePontos()).sum();
    }

    public Jogador getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return this.getNome() + " [" + wrapped.getClass() + "]";
    }
}
