package br.nom.abdon.domino;

/**
 * Um jogador da partida. Interface que deve ser implementada pelas classes que forem jogar.
 * 
 * @author bruno
 *
 */
public interface Jogador {

	/**
	 * O jogador recebe sua mao 6 {@link Pedra}s no inicio de cada pertida.
	 * 
	 * Eh responsabilidade do jogador saber que, se for a primeira rodada da
	 * primeira partida, ele deve comecar com o dozao, ou quadra de quina, etc.
	 * (se o sistema disser que este jogador deve ser o primeiro a jogar, entao 
	 * eh certo que este jogador eh quem tem a maior peca).
	 * 
	 *  Pedra-bebe para o jogo imediatamente.
	 * 
	 * @param pedras 6 {@link Pedra}s
	 */
	public void recebeMao(Pedra[] pedras);
	
	/**
	 * Esta na vez deste jogador jogar. Deve retornar uma {@link Jogada} dizendo qual a
	 * peca que quer jogar e, caso for ambiguo, em que ponta da mesa quer jogar.
	 * 
	 * Obviamente, o jogador deve ter {@link #recebeMao(Pedra[]) recebido esssa pessa}
	 * nessa partida e nao ter jogado ela ainda.
	 * 
	 * Para tocar, eh soh retornar o singleton {@link Jogada#TOQUE}.
	 * 
	 * Pedra-beba cancela o jogo imediatamente.
	 *  
	 * @param mesa A mesa no estado atual.
	 * @return A {@link Jogada} que o jogador decidiu fazer.
	 */
	public Jogada joga(Mesa mesa);
	

	/**
	 * Usado na primeira rodada de uma partida onde a dupla desse jogador ganhou
	 * a partida anterior. Um dos dois da dupla deve iniciar. Cada jogador deve dizer,
	 * atraves deste metodo, com uma nota de 0 a 10, o "quanto" ele quer comecar. O
	 * que der a maior nota, comeca. Em caso de empate, um dois dois vai ser escolhido
	 * aleatoriamente.
	 * 
	 * @return Um inteiro de zero a dez.
	 */
	public int vontadeDeComecar();

}
