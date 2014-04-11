package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;

public class EventoDomino {

	public enum Tipo {
		/**
		 * O jogo comecou. O placar está zero a zero (um jogo é a 
		 * seqüência de várias partidas). 
		 * 
		 * O {@link EventoDomino evento} vai conter as seguintes informações 
		 * <ul>  
		 * <li> {@link EventoDomino#getNomeDoJogador1() Nome do jogador 1 (dupla 1)}
		 * <li> {@link EventoDomino#getNomeDoJogador2() Nome do jogador 2 (dupla 2)}
		 * <li> {@link EventoDomino#getNomeDoJogador3() Nome do jogador 3 (dupla 1)}
		 * <li> {@link EventoDomino#getNomeDoJogador4() Nome do jogador 4 (dupla 2)}
		 * </ul>
		 */		
		JOGO_COMECOU,
		
		/**
		 * Mais uma partida começou (um jogo tem várias partidas).
		 * 
		 * No {@link EventoDomino}, estão setados os seguintes campos: 
		 * <ul>  
		 * <li> {@link EventoDomino#getPontosDupla1() Quantos pontos a dupla 1 tem}.
		 * <li> {@link EventoDomino#getPontosDupla2() Quantos pontos a dupla 2 tem}.
		 * <li> {@link EventoDomino#ehDobrada() se os pontos dessa partida valeram em dobro}, por causa
		 * de um empate na partida anterior (pode ser o caso de ser uma seqüência de empates).
		 * </ul>
		 */
		PARTIDA_COMECOU,

		/**
		 * Um determinado {@link Jogador} {@link Jogada jogou} uma {@link Pedra} (e 
		 * nao {@link Jogada#TOQUE tocou}).
		 * 
		 * No {@link EventoDomino}, estão setados os seguintes campos: 
		 * <ul>  
		 * <li> {@link EventoDomino#getQuemFoi() quem jogou}.
		 * <li> {@link EventoDomino#getPedra() a pedra que jogou}.
		 * <li> {@link EventoDomino#getLado() onde jogou}.
		 * </ul>
		 */
		JOGADOR_JOGOU,
		/**
		 * Um {@link Jogador} {@link Jogada#TOQUE tocou})
		 * 
		 * No {@link EventoDomino}, estão setados os seguintes campos: 
		 * <ul>  
		 * <li> {@link EventoDomino#getQuemFoi() Quem tocou}.
		 * </ul>
		 */
		JOGADOR_TOCOU,
		/**
		 * A partida atual acabou, ou por que algum {@link Jogador} bateu ou por que empatou. 
		 * O jogo ainda pode continuar.
		 * 
		 * No {@link EventoDomino}, estão setados os seguintes campos: 
		 * <ul>  
		 * <li> {@link EventoDomino#foiEmpate() se foi ou não empate}. 
		 * <li> {@link EventoDomino#getQuemFoi() Quem bateu}, em caso de não ter sido empate 
		 * (ou <code>null</code>, se for).
		 * <li> {@link EventoDomino#getTipoDeVitoria() Como foi a batida} (ou <code>null</code>, 
		 * se for empate).
		 * </ul>
		 */
		PARTIDA_ACABOU,
		
		/**
		 * Uma das duplas fez 6 pontos (ou mais) e o jogo acabou.
		 * 
		 * No {@link EventoDomino}, estão setados os seguintes campos: 
		 * <ul>  
		 * <li> {@link EventoDomino#getPontosDupla1() Com quantos pontos a dupla 1 terminou}.
		 * <li> {@link EventoDomino#getPontosDupla2() Com quantos pontos a dupla 2 terminou}.
		 * </ul>
		 */
		JOGO_ACABOU};
	
	private final Tipo tipo;
	
	private String quemFoi;
	private int pontosDupla1, pontosDupla2;
	private boolean ehDobrada;
	private Boolean foiEmpate;
	private String nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4;
	private Pedra pedra;
	private Lado lado;
	private Vitoria tipoDeVitoria;
	
	protected EventoDomino(Tipo tipo){
		this.tipo = tipo;
	}

	public EventoDomino(Tipo tipo,String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4) {
		this(tipo);
		this.nomeDoJogador1 = nomeDoJogador1;
		this.nomeDoJogador2 = nomeDoJogador2;
		this.nomeDoJogador3 = nomeDoJogador3;
		this.nomeDoJogador4 = nomeDoJogador4;
	}

	public EventoDomino(Tipo tipo,int pontosDupla1, int pontosDupla2, boolean ehDobrada) {
		this(tipo);
		this.pontosDupla1 = pontosDupla1;
		this.pontosDupla2 = pontosDupla2;
		this.ehDobrada = ehDobrada;
	}

	public EventoDomino(Tipo tipo,String quemFoi, Pedra pedra, Lado lado) {
		this(tipo);
		this.quemFoi = quemFoi;
		this.pedra = pedra;
		this.lado = lado;
	}

	public EventoDomino(Tipo tipo,String quemFoi) {
		this(tipo);
		this.quemFoi = quemFoi;
	}

	public EventoDomino(Tipo tipo,String quemFoi, Vitoria tipoDeVitoria) {
		this(tipo);
		this.quemFoi = quemFoi;
		this.tipoDeVitoria = tipoDeVitoria;
                this.foiEmpate = Boolean.FALSE;
	}

	public EventoDomino(Tipo tipo,int pontosDupla1, int pontosDupla2) {
		this(tipo);
		this.pontosDupla1 = pontosDupla1;
		this.pontosDupla2 = pontosDupla2;
	}

	public EventoDomino(Tipo tipo, boolean foiEmpate) {
		this(tipo);
		this.foiEmpate = foiEmpate ? Boolean.TRUE : Boolean.FALSE;
	}

	public String getQuemFoi() {
		return quemFoi;
	}

	public int getPontosDupla1() {
		return pontosDupla1;
	}

	public int getPontosDupla2() {
		return pontosDupla2;
	}

	public boolean ehDobrada() {
		return ehDobrada;
	}

	public String getNomeDoJogador1() {
		return nomeDoJogador1;
	}

	public String getNomeDoJogador2() {
		return nomeDoJogador2;
	}

	public String getNomeDoJogador3() {
		return nomeDoJogador3;
	}

	public String getNomeDoJogador4() {
		return nomeDoJogador4;
	}

	public Pedra getPedra() {
		return pedra;
	}

	public Lado getLado() {
		return lado;
	}

	public Vitoria getTipoDeVitoria() {
		return tipoDeVitoria;
	}

	/**
	 * O tipo do evento ocorrido. O significado do valor em cada atributo da
	 * classe varia de acordo com o evento ocorrido. 
	 * 
	 * @return o tipo do evento ocorrido
	 * @see Tipo
	 */
	public Tipo getTipo() {
		return tipo;
	}

	public Boolean foiEmpate() {
		return foiEmpate;
	}
}
