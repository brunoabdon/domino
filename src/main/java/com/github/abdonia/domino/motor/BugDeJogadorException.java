/*
 * Copyright (C) 2016 Bruno Abdon
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

import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 * Exceção que indica um comportamente excepcional (errado) por parte de um
 * {@linkplain com.github.abdonia.domino.Jogador jogador}.
 *
 * <p>É esperado que implementações de {@code Jogador}
 * conheçam e sigam as regras do jogo. Quando um jogador tenta fazer alguma
 * coisa fora das regras (como jogar uma {@link Pedra pedra} que não
 * caiba na {@link com.github.abdonia.domino.Mesa mesa}, ou {@link
 * com.github.abdonia.domino.Jogada#TOQUE tocar} tendo pedras pra jogar) não faz
 * mais sentido continuar o jogo, e ele é interrompido com esta exceção.</p>
 *
 * <p>A exceção guarda as informações sobre qual foi o jogador que cometeu o
 * erro e qual foi a violação cometida. A partir delas, o sistema irá
 * {@link com.github.abdonia.domino.eventos emitir eventos} que podem ser úteis
 * para logs ou interfaces de usuário.</p>
 *
 */
class BugDeJogadorException extends Exception {

    private static final long serialVersionUID = 2807255638816204700L;

    /**
     * Identifica a falha cometida pelo jogador.
     */
    enum Falha {
        /**
         * O jogador {@link com.github.abdonia.domino.Jogada jogou uma pedra em
         * um lado} da {@link com.github.abdonia.domino.Mesa mesa}, mas a {@link
         * Pedra} não {@link
         * Pedra#temNumero(com.github.abdonia.domino.Numero) tinha o número}
         * deste lado da mesa.
         */
        PEDRA_INVALIDA,
        /**
         * O {@link com.github.abdonia.domino.Jogador jogador} {@link
         * com.github.abdonia.domino.Jogada#TOQUE tocou} mesmo tendo uma {@link
         * Pedra pedra} que poderia ter {@link com.github.abdonia.domino.Jogada
         * jogado}. Isso pode ser um roubo comum na vida real, mas o sistema é
         * onisciente e não vai deixar isso acontecer aqui.
         */
        TOCOU_TENDO,
        /**
         * O {@link com.github.abdonia.domino.Jogador jogador} que tinha a maior
         * {@link Pedra#isCarroca() carroça} (provavelmente o {@link
         * Pedra#CARROCA_DE_SENA Dozão}) na mão na primeira rodada da primeira
         * {@link Partida partida} começou o {@link Jogo jogo} {@link
         * com.github.abdonia.domino.Jogada jogando} outra {@link Pedra pedra}.
         * Esse cara não sabe o básico de jogar, não faz sentido continuar o
         * {@link Jogo jogo}.
         */
        JA_COMECOU_ERRANDO,
        /**
         * O {@link com.github.abdonia.domino.Jogador jogador}
         * {@link com.github.abdonia.domino.Jogada jogou} uma {@link Pedra
         * pedra} que não tinha na mão. Isso é bug ou roubo.
         */
        TIROU_PEDRA_DO_BOLSO,
        /**
         * O {@link com.github.abdonia.domino.Jogador jogador} retornou {@code
         * null} quando {@link com.github.abdonia.domino.Jogador#joga()
         * perguntado qual seria sua jogada}. Mesmo no caso de não ter uma
         * {@link Pedra pedra} pra jogar, o jogador não deve retornar {@code
         * null}, e sim {@link com.github.abdonia.domino.Jogada#TOQUE tocar}
         * explicitamente.
         */
        NAO_JOGOU_NEM_TOCOU,
        /**
         * O {@link com.github.abdonia.domino.Jogador jogador} retornou {@code
         * null} quando foi
         * {@link com.github.abdonia.domino.Jogador#getVontadeDeComecar()
         * perguntado sobre se queria começar} a partida. Isso é um bug do
         * jogador e não faz mais sentido continuar um {@link Jogo jogo} com
         * ele.
         */
        NAO_SABE_SE_COMECE;
    }

    private final Falha falha;
    private final JogadorWrapper jogadorBuguento;
    private final Pedra pedra;
    private final Numero numero;

    /**
     * Cria uma exceção com a falha e a referência ao jogador que fez o
     * comportamento bugado.
     *
     * @param falha A falha do jogador.
     * @param jogadorBuguento O jogador contendo o bug.
     */
    BugDeJogadorException(
            final Falha falha,
            final JogadorWrapper jogadorBuguento) {
        this(falha,jogadorBuguento,null,null);
    }

    /**
     * Cria uma exceção com sua mensagem, a referência ao jogador que fez o
     * comportamento bugado e a uma {@link Pedra pedra} ligada ao bug.
     *
     * @param falha A falha do jogador.
     * @param jogadorBuguento O jogador contendo o bug.
     * @param pedra A pedra relevante ao bug.
     */
    BugDeJogadorException(
            final Falha falha,
            final JogadorWrapper jogadorBuguento,
            final Pedra pedra) {
        this(falha,jogadorBuguento,pedra,null);
    }

    /**
     * Cria uma exceção com sua mensagem, a referência ao jogador que fez o
     * comportamento bugado e a uma {@link Pedra pedra} ligada ao bug.
     *
     * @param falha A falha do jogador.
     * @param jogadorBuguento O {@link JogadorWrapper} encapsulando o {@link
     * com.github.abdonia.domino.Jogador jogador} contendo o bug.
     * @param pedra A {@link Pedra} relevante ao bug.
     * @param numero O {@link Numero} relevante ao bug.
     */
    BugDeJogadorException(
            final Falha falha,
            final JogadorWrapper jogadorBuguento,
            final Pedra pedra,
            final Numero numero) {

        super(falha.toString());
        this.falha = falha;
        this.jogadorBuguento = jogadorBuguento;
        this.pedra = pedra;
        this.numero = numero;
    }

    JogadorWrapper getJogadorBuguento() {
        return jogadorBuguento;
    }

    Pedra getPedra() {
        return pedra;
    }

    Falha getFalha(){
        return this.falha;
    }

    Numero getNumero() {
        return this.numero;
    }
}
