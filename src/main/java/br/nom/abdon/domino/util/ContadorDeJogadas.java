package br.nom.abdon.domino.util;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.eventos.DominoEventListener;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bruno
 */
public class ContadorDeJogadas implements DominoEventListener{

    private int toques;
    private final EnumMap<Pedra,Integer> jogadas;
    private int totalJogadas;

    private boolean ehPrimeiraPartida;
    
    public ContadorDeJogadas() {
        this.jogadas = new EnumMap<>(Pedra.class);
    }

    @Override
    public void partidaComecou(int placarDupla1, int placarDupla2, boolean ehDobrada) {
        this.ehPrimeiraPartida = placarDupla1 == 0 && placarDupla2 == 0;
    }
    
    
    @Override
    public void jogadorTocou(int jogador) {
        this.toques++;
    }

    @Override
    public void jogadorJogou(int jogador, Lado lado, Pedra pedra) {
        if(!ehPrimeiraPartida){
            this.jogadas.merge(pedra,1,Integer::sum);
            totalJogadas++;
        }
    }

    public void imprimeResultado() {
        System.out.printf("Toques: %10d\n",toques);
        
        SortedSet<String> list = new TreeSet<>(Comparator.reverseOrder());
        
        for (Pedra pedra : Pedra.values()) {
            final Integer numJogadas = jogadas.get(pedra);
            final float percent = (numJogadas/(float)totalJogadas)*100f;
            
            list.add(String.format("% 5d (%f%%) [%2d] [%1s]- %s", 
                numJogadas, 
                percent,
                pedra.getNumeroDePontos(),
                pedra.isCarroca()?"*":"",
                pedra));
        }
        
        list.stream().forEach(System.out::println);
    }
    
    

    
    
    
}
