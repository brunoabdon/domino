package br.nom.abdon.domino;

/**
 * Um jogador da partida. Interface que deve ser implementada pelas classes que
 * forem jogar.
 * 
 * @author bruno
 */
public interface Jogador {

    /**
     * O jogador toma uma das quatro posições pra jogar no inicio do Jogo.
     * <b>Importante:</b> Os jogadores são identificados pelos números de 1 a 4
     * (e não de 0 a 3, como nerds esperariam) no sentido anti-horário (fazendo
     * então que as duplas sejam <i>1 e 3</i> contra <i>2 e 4</i>).
     * 
     * Esta numeração é consistente com a usada em {@link 
     * Mesa#quantasPedrasOJogadoresTem(int)}.
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
     * O jogador recebe sua mão 6 {@link Pedra}s no início de cada partida.
     * 
     * @param pedras um array contendo 6 {@link Pedra}s.
     */
    public void recebeMao(Pedra[] pedras);

    /**
     * Está na vez deste jogador jogar. Deve retornar uma {@link Jogada} dizendo
     * qual peca quer jogar e de que {@link Lado} da mesa ela deve ser jogada.
     * 
     * Obviamente, o jogador deve ter {@link #recebeMao(Pedra[]) recebido esssa
     * pedra} nessa partida e não ter jogado ela ainda.
     * 
     * É responsabilidade do jogador saber que, se for a primeira rodada da
     * primeira partida, ele deve comecar com o {@link Pedra#CARROCA_DE_SENA
     * dozão}, ou {@link Pedra carroça de quina}, etc. (se o sistema disser que
     * este jogador deve ser o primeiro a jogar, então é certo que este jogador
     * é quem tem a maior carroça).

     * Para tocar, deve retornar o singleton {@link Jogada#TOQUE}. Retornar
     * <code>null</code> ou um pedra-beba cancela o jogo imediatamente.
     *  
     * @return A {@link Jogada} que o jogador decidiu fazer.
     */
    public Jogada joga();

    /**
     * Usado na primeira rodada de uma partida quando a dupla desse {@link
     * Jogador} ganhou a partida anterior. Um dos dois jogadores da dupla deve
     * fazer a primeira jogada. Cada jogador deve dizer, através deste metodo, 
     * com uma nota de 0 a 10, o "<i>quanto ele quer ser o jogador a fazer a 
     * primeira jogada</i>". O que der a maior nota, começa. Em caso de empate,
     * um dois dois vai ser escolhido aleatoriamente.
     * 
     * @return Um inteiro de zero a dez.
     */
    public int vontadeDeComecar();
}