package br.nom.abdon.domino.util;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.eventos.DominoEventListener;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;

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
    public void partidaComecou(
            final int placarDupla1, 
            final int placarDupla2, 
            final boolean ehDobrada) {
        this.ehPrimeiraPartida = placarDupla1 == 0 && placarDupla2 == 0;
    }
     
    @Override
    public void jogadorTocou(final int jogador) {
        this.toques++;
    }

    @Override
    public void jogadorJogou(
            final int jogador, 
            final Lado lado, 
            final Pedra pedra) {
        if(!ehPrimeiraPartida){
            this.jogadas.merge(pedra,1,Integer::sum);
            totalJogadas++;
        }
    }

    public void imprimeResultado() {
        System.out.printf("Toques: %10d\n",toques);
        
        final SortedSet<String> list = new TreeSet<>(Comparator.reverseOrder());
        
        for (final Pedra pedra : Pedra.values()) {
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