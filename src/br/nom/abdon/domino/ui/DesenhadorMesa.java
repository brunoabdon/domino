package br.nom.abdon.domino.ui;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

//essa classe nao deve usar nada de awt
public class DesenhadorMesa {

	private final DesenhadorObjetos desenhadorObjetos;
	private final CalculadorDeCoordenadaDePedras calculadorDeCordenadaDePedras;
	
	private static final float DISTANCIA_PEDRAS_DA_MAO = DesenhadorObjetos.LARGURA_DA_PEDRA/10f;
	private static final float INCREMENTO_DISTANCIA_PEDRAS_DA_MAO = DesenhadorObjetos.LARGURA_DA_PEDRA + DISTANCIA_PEDRAS_DA_MAO;
	
	//auxiliar. reflete o algoritimo que desenha
	private static final float LARGURA_DE_UMA_MAO = (6 * DesenhadorObjetos.LARGURA_DA_PEDRA) + (5 * DISTANCIA_PEDRAS_DA_MAO); 
	
	private static final float MAO_DE_CIMA_X = (DesenhadorObjetos.LARGURA_DA_MESA - LARGURA_DE_UMA_MAO ) / 2;
	private static final float MAO_DE_CIMA_Y = DISTANCIA_PEDRAS_DA_MAO;
	
	private static final float MAO_DE_BAIXO_X = MAO_DE_CIMA_X;
	private static final float MAO_DE_BAIXO_Y = DesenhadorObjetos.ALTURA_DA_MESA - DesenhadorObjetos.ALTURA_DA_PEDRA - MAO_DE_CIMA_Y;

	private static final float MAO_DA_ESQUERDA_X = DISTANCIA_PEDRAS_DA_MAO;
	private static final float MAO_DA_ESQUERDA_Y = (DesenhadorObjetos.ALTURA_DA_MESA - LARGURA_DE_UMA_MAO) / 2;

	private static final float MAO_DA_DIREITA_X = (DesenhadorObjetos.LARGURA_DA_MESA - DesenhadorObjetos.ALTURA_DA_PEDRA - MAO_DA_ESQUERDA_X);
	private static final float MAO_DA_DIREITA_Y = MAO_DA_ESQUERDA_Y;

	private static final float DISTANCIA_DOS_NOMES_PRA_AS_MAOS = 4*DISTANCIA_PEDRAS_DA_MAO;
	
	private static final float JOGADOR_DE_CIMA_X = MAO_DE_CIMA_X + LARGURA_DE_UMA_MAO + DISTANCIA_DOS_NOMES_PRA_AS_MAOS;
	private static final float JOGADOR_DE_CIMA_Y = MAO_DE_CIMA_Y + DesenhadorObjetos.TAMANHO_DA_LETRA_DO_NOME_DOS_JOGADORES;
	private static final float JOGADOR_DA_DIREITA_X = MAO_DA_DIREITA_X;
	private static final float JOGADOR_DA_DIREITA_Y = MAO_DA_DIREITA_Y + LARGURA_DE_UMA_MAO + DISTANCIA_DOS_NOMES_PRA_AS_MAOS  + DesenhadorObjetos.TAMANHO_DA_LETRA_DO_NOME_DOS_JOGADORES;
	private static final float JOGADOR_DE_BAIXO_X = JOGADOR_DE_CIMA_X;
	private static final float JOGADOR_DE_BAIXO_Y = MAO_DE_BAIXO_Y + DesenhadorObjetos.TAMANHO_DA_LETRA_DO_NOME_DOS_JOGADORES;
	private static final float JOGADOR_DA_ESQUERDA_X = MAO_DA_ESQUERDA_X;
	private static final float JOGADOR_DA_ESQUERDA_Y = JOGADOR_DA_DIREITA_Y;

	
	public DesenhadorMesa(DesenhadorObjetos desenhadorObjetos, CalculadorDeCoordenadaDePedras calculadorDeCordenadaDePedras) {
		this.desenhadorObjetos = desenhadorObjetos;
		this.calculadorDeCordenadaDePedras = calculadorDeCordenadaDePedras;
	}
	
	public void desenhaMesaInicial(String nomeJogador1,String nomeJogador2,String nomeJogador3,String nomeJogador4){
		
		desenhadorObjetos.desenhaMesa();
		
		escreveNomeDosJogadores(nomeJogador1,nomeJogador2,nomeJogador3,nomeJogador4);
		
		desenhaMao(MAO_DE_CIMA_X, MAO_DE_CIMA_Y, Direcao.PRA_DIREITA);
		desenhaMao(MAO_DE_BAIXO_X, MAO_DE_BAIXO_Y, Direcao.PRA_DIREITA);
		desenhaMao(MAO_DA_ESQUERDA_X, MAO_DA_ESQUERDA_Y, Direcao.PRA_BAIXO);
		desenhaMao(MAO_DA_DIREITA_X, MAO_DA_DIREITA_Y, Direcao.PRA_BAIXO);
	}

	public void desenhaJogada(String nomeJogador, Pedra pedra, Lado lado, Numero cabecaDoLado){
		
		this.calculadorDeCordenadaDePedras.calculaOndeDesenharAPedra(lado, pedra.isCarroca());
		 
		this.desenhadorObjetos.desenhaPedra(
				pedra, 
				calculadorDeCordenadaDePedras.getDirecao(),
				calculadorDeCordenadaDePedras.getX(),
				calculadorDeCordenadaDePedras.getY());
	}
	
	private void escreveNomeDosJogadores(String nomeJogador1, String nomeJogador2, String nomeJogador3, String nomeJogador4) {
		desenhadorObjetos.escreveNomeJogador(nomeJogador1, JOGADOR_DE_CIMA_X, JOGADOR_DE_CIMA_Y);
		desenhadorObjetos.escreveNomeJogador(nomeJogador2, JOGADOR_DA_DIREITA_X, JOGADOR_DA_DIREITA_Y);
		desenhadorObjetos.escreveNomeJogador(nomeJogador3, JOGADOR_DE_BAIXO_X, JOGADOR_DE_BAIXO_Y);
		desenhadorObjetos.escreveNomeJogador(nomeJogador4, JOGADOR_DA_ESQUERDA_X, JOGADOR_DA_ESQUERDA_Y);
		
	}

	private void desenhaMao(float x, float y, Direcao direcao){
		if(direcao.ehVertical()){
			for (int i = 0; i < 6; i++) {
				desenhadorObjetos.desenhaPedraEmborcada(direcao, x, y);
				y+=INCREMENTO_DISTANCIA_PEDRAS_DA_MAO;
			}
		} else {
			for (int i = 0; i < 6; i++) {
				desenhadorObjetos.desenhaPedraEmborcada(direcao, x, y);
				x+=INCREMENTO_DISTANCIA_PEDRAS_DA_MAO;
			}
		}
		
	}
	
}
