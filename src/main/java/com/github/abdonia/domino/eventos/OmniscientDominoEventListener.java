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
package com.github.abdonia.domino.eventos;

import java.util.Collection;

import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 * Interface para receber eventos com informações sigilosas que acontecem 
 * durante o jogo. Deve ser implementado por UIs, Loggers, etc. mas não por
 * jogadores.
 * 
 * @author bruno
 */
public interface OmniscientDominoEventListener extends DominoEventListener {

    /**
     * Avisa que, no início de uma partida, um derterminado 
     * {@link com.github.abdonia.domino.Jogador jogador}recebeu dadas 
     * {@link Pedra}s.
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     * @param pedras Uma coleção <em>não modificável</em> das 6 pedras que o 
     * jogador recebeu.
     */
    public default void jogadorRecebeuPedras(
            final int quemFoi, final Collection<Pedra> pedras){
    }
    
    /**
     * As pedras foram distribuidas, e as quatro que sobraram foram pro dorme.
     * 
     * @param pedras As pedras que foram pro dorme.
     */
    public default void dormeDefinido(final Collection<Pedra> pedras){
        
    }
    
    /**
     * O {@link com.github.abdonia.domino.Jogador} jogou uma {@link Pedra} que 
     * não cabia na {@link com.github.abdonia.domino.Mesa}, o que é um erro 
     * grave (bug da implementação do jogador), e faz o {@link 
     * com.github.abdonia.domino.motor.Jogo} ser abortado.
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     * @param pedra a {@link Pedra} que tentou jogar.
     * @param numero o {@link Numero} que a pedra deveria ter pra caber na mesa.
     */
    public default void jogadorJogouPedraInvalida(
        final int quemFoi, final Pedra pedra, final Numero numero){
    }

    /**
     * Foi {@link com.github.abdonia.domino.Jogador#vontadeDeComecar() 
     * perguntado} a {@link com.github.abdonia.domino.Jogador um dos 
     * jogadores } da dupla que venceu a partida anterior se ele queria ser o 
     * primeiro a jogar, e ele respondeu "<em>batata</em>". A resposta deve ser
     * sempre um número de 0 a 10, como descrito na documentação. Isso é um erro 
     * grave (bug da implementação do jogador), e faz o {@link 
     * com.github.abdonia.domino.motor.Jogo} ser abortado.
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     */
    public default void jogadorErrouVontadeDeComeçar(int quemFoi){
    }

    /**
     * O jogo vem por meio deste método informar que o jogador que estava 
     * sentado na cadeira cujo número está indicado como parâmetro já não está
     * mais entre nós. Ele teve um crash súbito, mas foi em paz. Em respeito ao
     * luto, e dada a impossibilidade de difunto jogar, o jogo vai ser
     * interrompido.
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     */
    public default void jogadorFaleceu(final int quemFoi){
    } 
    
    /**
     * O {@link com.github.abdonia.domino.Jogador} retornou 
     * <code>null</code> quando {@link com.github.abdonia.domino.Jogador#joga()
     * perguntado qual seria sua jogada}. Mesmo no caso de
     * não ter uma {@link Pedra pedra} pra jogar, o jogador não deve 
     * retornar <code>null</code>, e sim {@link 
     * com.github.abdonia.domino.Jogada#TOQUE tocar} explicitamente.
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     */
    public default void jogadorJogouPedraNenhuma(final int quemFoi){
    }
    
    /**
     * O {@link com.github.abdonia.domino.Jogador jogador} era quem 
     * tinha a maior {@link Pedra#isCarroca() carroça} (provavelmente o {@link 
     * Pedra#CARROCA_DE_SENA Dozão} na mão na primeira rodada da primeira 
     * partida, mas começou o jogo {@link com.github.abdonia.domino.Jogada 
     * jogando} outra {@link Pedra pedra}.
     * <p>Esse cara não sabe o basico de jogar, não faz sentido continuar o 
     * jogo.</p>
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     */
    public default void jogadorComecouErrando(final int quemFoi){
        
    }
    
    /**
     * O {@link com.github.abdonia.domino.Jogador jogador} {@link 
     * com.github.abdonia.domino.Jogada#TOQUE tocou} quando tinha {@link 
     * Pedra pedras} na pão que poderiam ser {@link 
     * com.github.abdonia.domino.Jogada jogadas}. Isso é roubo (ou bug).
     * 
     * <p>A pesar desse tipo de roubo ser comum na vida real, onde a pessoa faz
     * e ninguém percebe, o sistem é onisciente e não vai deixar isso rolar 
     * aqui.</p>
     * 
     * @param quemFoi O jogador em questão (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     */
    public default void jogadorTocouTendoPedraPraJogar(final int quemFoi){
        
    }

    /**
     * O {@link com.github.abdonia.domino.Jogador} jogou uma {@link Pedra} que 
     * ele não tinha na mão. Isso é roubo (ou bug) e faz o jogo ser abortado.
     * 
     * @param quemFoi O jogador em espertinho (identificado pelo 
     * {@link com.github.abdonia.domino.Jogador#sentaNaMesa(
     * com.github.abdonia.domino.Mesa, int) número da cadeira}).
     * 
     * @param pedra a {@link Pedra} que ele o jogador tirou do bolso pra jogar.
     */
    public default void jogadorJogouPedraQueNãoTinha(
            final int quemFoi, final Pedra pedra){
        
    }
}
