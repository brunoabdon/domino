package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;

/**
 * Interface para receber eventos publicos do que acontece durante o jogo.
 * 
 * {@link Jogador}es que implementarem esta interface serao automaticamente 
 * registrados para serem avisados dos eventos.
 * 
 * @author bruno
 *
 */
public interface DominoEventListener {

    /**
     * O jogo comecou. O placar está zero a zero (um jogo é a 
     * seqëncia de várias partidas).
     *   
     * @param nomeDoJogador1 nome do jogador 1 (dupla 1)
     * @param nomeDoJogador2 nome do jogador 2 (dupla 2)
     * @param nomeDoJogador3 nome do jogador 3 (dupla 1)
     * @param nomeDoJogador4 nome do jogador 4 (dupla 2)
     */
    public default void jogoComecou(
            String nomeDoJogador1, 
            String nomeDoJogador2, 
            String nomeDoJogador3, 
            String nomeDoJogador4){

    }

    /**
     * Mais uma partida começou (um jogo tem várias partidas).
     *  
     * @param placarDupla1 Quantos pontos a dupla 1 tem.
     * @param placarDupla2 Quantos pontos a dupla 2 tem
     * @param ehDobrada diz se os pontos dessa partida valeram em dobro, por causa
     * de um empate na partida anterior (por ser o caso de ser uma seqüência de empates)
     */
    public default void partidaComecou(
            int placarDupla1, 
            int placarDupla2, 
            boolean ehDobrada){
    }

    /**
     * Um determinado {@link Jogador} {@link Jogada jogou} uma {@link Pedra} (e 
     * nao {@link Jogada#TOQUE tocou}). (Se ele tiver batido, além desse evento, 
     * também ocorrerá {@link #jogadorBateu(String, Vitoria)}
     * 
     * @param nomeDoJogador quem jogo
     * @param lado onde jogou (pode ter sido nulo)
     * @param pedra o que jogou
     */
    public default void jogadorJogou(
            String nomeDoJogador, 
            Lado lado, 
            Pedra pedra){

    }

    /**
     * Um {@link Jogador} {@link Jogada#TOQUE tocou})
     * @param nomeDoJogador quem foi
     */
    public default void jogadorTocou(String nomeDoJogador){

    }

    /**
     * Um {@link Jogador} bateu e a partida acabou. O jogo ainda pode continuar.
     * @param quemFoi Quem bateu 
     * @param tipoDeVitoria Como foi a batida.
     */
    public default void jogadorBateu(String quemFoi, Vitoria tipoDeVitoria){

    }

    /**
     * A partida acabou empatada. O jogo vai continuar.
     */
    public default void partidaEmpatou(){

    }

    /**
     * Uma das duplas fez 6 pontos (ou mais) e o jogo acabou.
     * 
     * @param placarDupla1 quantos pontos tinha a dupla 1
     * @param placarDupla2 quantos pontos tinha a dupla 2
     */
    public default void jogoAcabou(int placarDupla1, int placarDupla2){

    }
	
}
