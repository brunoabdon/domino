/*
 * Copyright (C) 2016 Bruno Abdon <brunoabdon+github@gmail.com>
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

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Pedra;
import java.util.List;

/**
 * Gerador de eventos aleatórios durante a partida, responsável por embaralhar
 * as pedras e por definir quem na dupla deve começar uma  partida em caso de 
 * empate na {@link Jogador#vontadeDeComecar() vontade demonstrada para começar 
 * a jogar}.
 * 
 * @author Bruno Abdon <brunoabdon+github@gmail.com>
 */
public interface RandomGoddess {

    /**
     * Embaralha uma lista de {@link Pedra}s, mudando (ou não) a posição de cada
     * pedra na lista de uma maneira aleatória ou pseudo-aleatória. A lista de
     * 28 pedras será distribuida entre os jogadores {@link 
     * Jogador#sentaNaMesa(com.github.abdonia.domino.Mesa, int) 
     * sentados na mesa}, onde o jogador na candeira <pre>x</pre> receberá as 
     * pedras do índices <pre>x*6</pre> até <pre>x*6 + 5</pre>. As pedras nos
     * últimos quatro indices vão para o dorme.
     * 
     * @param pedras a lista de {@link Pedra}s a ser embaralhada.
     */
    public void embaralha(final List<Pedra> pedras);
    
    /**
     * Diz, (pseudo-)aleatóriamente se o primeiro jogador da dupla (e não o 
     * segundo) é quem deve jogar a  primeira pedra na partida (em caso de 
     * empate na {@link Jogador#vontadeDeComecar() vontade demonstrada para 
     * começar a jogar}).
     * 
     * @return <code>true</code> se o primeiro jogador da dupla deve ser quem 
     * começa, ou <code>false</code> se é o segundo quem deve.
     */
    public boolean jogador1Comeca();
    
}
