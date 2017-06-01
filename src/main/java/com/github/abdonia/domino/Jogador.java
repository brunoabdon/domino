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
package com.github.abdonia.domino;

/**
 * Um jogador da partida, ou seja, a IA que decide como jogar.
 * 
 * <hr>
 * <h2>Métodos básicos</h2>
 * <p><b>{@link #sentaNaMesa(Mesa, int) jogador.sentaNaMesa(...)}</b>: Num jogo 
 * de dominó, os quatro Jogadores vão primeiro {@link #sentaNaMesa(Mesa, int) 
 * sentar na mesa} (em duplas) e então jogar várias partidas, até que uma das 
 * duplas acumule seis pontos e o jogo termine.</p>
 * 
 * <p><b>{@link #recebeMao(Pedra, Pedra, Pedra, Pedra, Pedra, Pedra) 
 * jogador.recebeMao(...)}</b>: Cada partida começa com cada Jogador {@link 
 * #recebeMao(Pedra, Pedra, Pedra, Pedra, Pedra, Pedra) recebendo seis pedras na 
 * mão} e com a {@link Mesa#taVazia() mesa vazia}. Quatro pedras ficam no 
 * <em>dorme</em> e estão fora dessa partida. Os jogadores vão {@link #joga() 
 * jogando} em turnos até que um deles vence ao colocar sua última pedra na 
 * {@link Mesa mesa}<a href="#fim">*</a>.</p>
 * 
 * <p><b>{@link #vontadeDeComecar() jogador.vontadeDeComecar()}</b>: Com a 
 * exceção da primeira partida, em que quem começa é o jogador que tiver o 
 * {@link Pedra#CARROCA_DE_SENA Dozão}<a href="#dozao">**</a>, nas demais partidas, quem começa é um 
 * dos jogadores da dupla que venceu a partida anterior. A dupla precisa decidir
 * quais dos dois membros vai começar, mas deve fazer isso sem trocar muita 
 * informação. Pra isso, cada jogador deve saber responde "{@link 
 * #vontadeDeComecar() o quanto ele gostaria de começar a jogar}" olhando apenas
 * pra suas pedras iniciais, sem se comunicar com seu parceiro de dupla.</p>
 *
 * <p><b>{@link #joga() jogador.joga()}</b>: Na sua vez de {@link #joga() jogar},
 * o jogador deve analisar o estado da {@link Mesa mesa} e decidir qual será sua 
 * {@link Jogada}: qual {@link Pedra pedra} de sua mão e em que {@link Lado 
 * lado} da mesa. Caso veja que nãotem nenhuma pedra que se encaixe na mesa, a 
 * jogada vai ser {@link Jogada#TOQUE um toque}. Caso seja a primeira jogada do 
 * primeira partida<a href="#ini">***</a>, cabe ao jogador entender que deve jogar a maior {@link 
 * Pedra#isCarroca() carroça} (normalmente o 
 * {@link Pedra#CARROCA_DE_SENA Dozão}<a href="#dozao">**</a>).</p> 
 * <hr>
 * <h2 id="eventos">Usando mais informações sobre o jogo</h2>
 * <p>Jogadores que implementarem {@link 
 * com.github.abdonia.domino.eventos.DominoEventListener} vão ser avisados dos 
 * eventos que acontecem no jogo (alguem tocou, tal jogador iniciou a partida,
 * etc. ...)</p>
 * <p>Enquanto os quatro métodos da interface {@code Jogador} permitem a 
 * implementação de um jogador completamente funcional (ver 
 * {@link com.github.abdonia.domino.exemplos.JogadorSimplorio}, por exemplo) 
 * estratégias de jogo um pouco mais complexas vão exigir que o jogador saiba 
 * mais sobre o que se passa no jogo.</p>
 * <hr>
 * 
 * <p><a name="fim">(*)</a> Típicamente, uma partida acaba com um dos jogadores
 * colocando sua última pedra na mesa. É uma "batida". Mas uma partida também 
 * pode terminar de outras quatro maneiras diferentes:</p>
 * <ol><li><em>Vitória por contagem de pontos na mão</em>: nenhum dos quatro 
 * jogadores tem uma pedra que encaixe na mesa e a partida trava. São somados, 
 * pra cada jogador, {@link Pedra#getNumeroDePontos() os pontos de suas pedras} 
 * e vence o jogador que tiver menos.</li>
 * <li><em>Empate por contagem de pontos</em>: A partida trava, jogadores de 
 * duplas diferentes empatam com o número mínimo de pontos na mão.</li>
 * <li><em>Partida abortada por cinco carroças na mão</em>: Se um dos jogadores
 * {@link #recebeMao(Pedra, Pedra, Pedra, Pedra, Pedra, Pedra) receber} cinco
 * {@link Pedra#isCarroca() carroças} entre as seis pedras inciais de sua mão,
 * a partida e cancelada e uma outra começa.</li>
 * <li><em>Vitória por seis carroças na mão</em>: Se um dos jogadores
 * {@link #recebeMao(Pedra, Pedra, Pedra, Pedra, Pedra, Pedra) receber} seis
 * {@link Pedra#isCarroca() carroças} já como as seis pedras inciais de sua mão,
 * a partida termina imediatamente com vitória pra a dupla desse jogador. Isso é
 * raríssimo de acontecer.</li>
 * </ol>
 * 
 * <p><a name="dozao">(**)</a> Pode acontecer do {@link Pedra#CARROCA_DE_SENA 
 * Dozão} estar no dorme,e então o jogo deve começar com a {@link 
 * Pedra#CARROCA_DE_QUINA carroça de quina}. Como o dorme tem quatro {@link 
 * Pedra pedras}, é possivel até que estejam nele as carroças de {@link 
 * Numero#SENA sena}, {@link Numero#QUINA quina}, {@link Numero#QUADRA quadra} e
 * {@link Numero#TERNO terno}, obrigando o jogo a ser inciado pelo jogador que 
 * tiver a {@link Pedra#CARROCA_DE_DUQUE carroça de duque}.</p>
 * 
 * <p><a name="ini">(***)</a> É possível perceber que é a primeira jogada da 
 * partida se a {@link Mesa#taVazia() mesa estiver vazia}. E ,se o método {@link 
 * #vontadeDeComecar()} não foi chamado, então esta é a primeira partida do 
 * jogo. É possível também saber quantas partidas foram jogadas 
 * <a href="#eventos">ouvindo eventos</a>.
 * </p>
 * 
 * @author Bruno Abdon
 */
public interface Jogador {

    /**
     * O jogador toma uma das quatro posições pra jogar no inicio do Jogo.
     * <b>Importante:</b> Os jogadores são identificados pelos números de 1 a 4
     * (e não de 0 a 3, como nerds esperariam), fazendo então que as duplas 
     * sejam <i>1 e 3</i> contra <i>2 e 4</i>). Esta numeração é consistente com 
     * a usada em {@link Mesa#quantasPedrasOJogadoresTem(int)}.
     * 
     * @param mesa A mesa do jogo do dominó, de onde se poderá descobrir, na 
     * hora de {@link #joga() jogar}, que {@link Pedra}s estão dispostas, qual o
     * {@link Numero} aparece em cada {@link Lado} e quantas pedras cada 
     * {@link Jogador} tem na mão.
     * 
     * @param cadeiraQueSentou O número da cadeira em que o jogador se sentou 
     * (entre 1 e 4).
     */
    public void sentaNaMesa(Mesa mesa, int cadeiraQueSentou);

    /**
     * O jogador recebe sua mão: 6 {@link Pedra pedras} no início de cada partida.
     * 
     * @param pedra1 A primeira {@link Pedra pedra} da mão.
     * @param pedra2 A segunda {@link Pedra pedra} da mão.
     * @param pedra3 A terceira {@link Pedra pedra} da mão.
     * @param pedra4 A quarta {@link Pedra pedra} da mão.
     * @param pedra5 A quinta {@link Pedra pedra} da mão.
     * @param pedra6 A última {@link Pedra pedra} da mão.
     */
    public void recebeMao(
            Pedra pedra1,
            Pedra pedra2,
            Pedra pedra3,
            Pedra pedra4,
            Pedra pedra5,
            Pedra pedra6);

    /**
     * Está na vez deste jogador jogar. Deve retornar uma {@link Jogada} dizendo
     * qual {@link Pedra pedra} quer jogar e de que {@link Lado} da mesa ela 
     * deve ser jogada.
     * 
     * <p>Obviamente, o jogador deve ter {@link #recebeMao(Pedra, Pedra, Pedra, 
     * Pedra, Pedra, Pedra) recebido esssa pedra} nessa partida e 
     * não ter jogado ela ainda.</p>
     * 
     * <p>É responsabilidade do jogador saber que, se for a primeira rodada da
     * primeira partida, ele deve comecar com o {@link Pedra#CARROCA_DE_SENA
     * dozão}, ou {@link Pedra carroça de quina}, etc. (se o sistema disser que
     * este jogador deve ser o primeiro a jogar, então é certo que este jogador
     * é quem tem a maior carroça).</p>

     * <p>Para tocar, deve retornar o singleton {@link Jogada#TOQUE}. Retornar
     * {@code null} ou um pedra-beba cancela o jogo imediatamente.</p>
     *  
     * @return A {@link Jogada} que o jogador decidiu fazer.
     */
    public Jogada joga();

    /**
     * Usado na primeira rodada de uma partida quando a dupla desse {@link
     * Jogador} ganhou a partida anterior. Um dos dois jogadores da dupla deve
     * fazer a primeira {@link Jogada}. Cada jogador deve dizer, através deste 
     * método, "<i>quanto ele quer ser o jogador a fazer a primeira jogada</i>".
     * O que que tiver mais vontade começa. Em caso de empate, um dois dois vai 
     * ser escolhido aleatoriamente.
     * 
     * @return A vontade deste jogador de ser o primeiro a jogar nessa partida.
     */
    public Vontade vontadeDeComecar();
}