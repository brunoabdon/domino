package com.github.abdonia.domino.exemplos;

import java.util.EnumSet;
import java.util.Set;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;

public class JogadorSeguraCarroca implements Jogador {

    private Integer cadeira;
    
    private Boolean ehPrimeiraRodadaDaPrimeiraPartida;
    
    private Set<Pedra> mao;
    
    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        if(cadeiraQueSentou < 0 || cadeiraQueSentou > 3)
            throw new IllegalArgumentException("É pra sentar fora da mesa?");
        
        if(this.cadeira != null)
            throw new IllegalStateException("Já estou sentado.");

        if(mesa == null) 
            throw new IllegalArgumentException("E cadê a mesa?");
        
        this.cadeira = cadeiraQueSentou;
        this.ehPrimeiraRodadaDaPrimeiraPartida = true;
        
    }

    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {

        if(this.cadeira == null)
            throw new IllegalStateException("Estou em pé!");
        
        if(pedra1 == null || pedra2 == null || pedra3 == null
            || pedra4 == null || pedra5 == null || pedra6 == null)
            throw new IllegalArgumentException("Tá faltando pedra aqui.");
            
        this.mao = 
            EnumSet.of(pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
        
        if(this.mao.size() != 6) {
            throw new IllegalArgumentException("Veio pedra repetida!");
        }
        
    }

    @Override
    public Jogada joga() {
        final Jogada jogada;
        if(ehPrimeiraRodadaDaPrimeiraPartida) {
            jogada = jogaAMaiorCarroca();
        } else {
            jogada = jogaAMenorNaoCarroca();
        }
        
        return jogada;
    }

    private Jogada jogaAMaiorCarroca() {
        final Pedra maiorCarroca =  
            this.mao
                .stream()
                .filter(Pedra::isCarroca)
                .max(Pedra::compareTo)
                .orElseThrow(
                    () -> new IllegalStateException("Nem carroça eu tenho!")
                );
        mao.remove(maiorCarroca);
        return Jogada.de(maiorCarroca, Lado.ESQUERDO);
    }

    private Jogada jogaAMenorNaoCarroca() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vontade getVontadeDeComecar() {
        // TODO Auto-generated method stub
        return null;
    }

}
