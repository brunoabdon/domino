package br.nom.abdon.domino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Partida {

	private Dupla dupla1, dupla2;
	
	private Mesa mesa;
	private Pedra[] dorme = new Pedra[4];

	private Collection<Pedra>[] maos;  
	
	public Partida(Dupla dupla1, Dupla dupla2) {
		super();
		this.dupla1 = dupla1;
		this.dupla2 = dupla2;
	}

	protected ResultadoPartida jogar(Dupla duplaQueComeca){
		
		Jogador jogadorDaVez = null;
		Pedra pedra = null;
		Lado lado = null;
		
		boolean alguemBateu = false, trancou = false;
		
		embaralhaEdistribui();
		int vez = defineDeQuemEAVezDeComecar(duplaQueComeca);
		while(!alguemBateu && trancou){
			
			jogadorDaVez = pegaJogadorDaVez(vez);
			Collection<Pedra> maoDoJogadorDaVez = this.maos[vez];
			
			Jogada jogada = jogadorDaVez.joga();
			//se livrando logo do objeto Jogada, que veio do jogador.
			lado = jogada.getLado();
			pedra = jogada.getPedra();

			validaJogada(jogadorDaVez,maoDoJogadorDaVez,pedra,lado);
			
			maoDoJogadorDaVez.remove(pedra);

			this.mesa.coloca(pedra, lado);
			
			alguemBateu = maoDoJogadorDaVez.isEmpty();
			if(!alguemBateu) {
				trancou = verificaTrancada();
			}
			
			vez = avanca(vez);
		}
		
		ResultadoPartida resultadoPartida;
		
		if(trancou){
			resultadoPartida = contaPontos();
		} else {
			Vitoria tipoDaBatida = veOTipoDaBatida(pedra);
			resultadoPartida = new ResultadoPartida(tipoDaBatida,jogadorDaVez); 
		}
		
		return resultadoPartida;

	}

	private Vitoria veOTipoDaBatida(Pedra pedra) {

		Vitoria tipoDaBatida;
		boolean carroca = pedra.isCarroca();
		boolean laELo = mesa.getNumeroEsquerda() == mesa.getNumeroDireita();
		
		if(carroca && laELo){
			tipoDaBatida = Vitoria.CRUZADA;
		} else if(laELo){
			tipoDaBatida = Vitoria.LA_E_LO;
		} else if (carroca) {
			tipoDaBatida = Vitoria.CARROCA;
		} else {
			tipoDaBatida = Vitoria.BATIDA_SIMPLES;
		}
		return tipoDaBatida;
	}
	
	private ResultadoPartida contaPontos() {
		
		ResultadoPartida resultado = null;
		
		int menorAteAgora = 1000; 
		int idxJogadorComMenos=-1;
		
		for (int i = 0; i < maos.length; i++) {
			int totalJogador = 0;
		
			for (Pedra pedra : maos[i]) {
				totalJogador += pedra.getNumeroDePontos();
			}
			if(totalJogador < menorAteAgora){
				menorAteAgora = totalJogador;
				idxJogadorComMenos = i; 
			} else if(totalJogador == menorAteAgora && (i-idxJogadorComMenos != 2)){
				//fudeu, empatou duas pessoas de duplas diferentes
				resultado = ResultadoPartida.EMPATE;
				break;
			}
		}
		
		if(resultado == null){
			Jogador jogadorComMenosPontosNaMao = pegaJogadorDaVez(idxJogadorComMenos);
			resultado = new ResultadoPartida(Vitoria.CONTAGEM_DE_PONTOS, jogadorComMenosPontosNaMao);
		}
		
		return resultado;
	}

	private boolean verificaTrancada() {
		/* 
		 * da pra melhorar pra caralho isso aqui.. ver o numero
		 * minimo de pedras que tem que ter sido jogado pra poder 
		 * ter chegado a trancar... ir contando quantas vezes cada
		 * numero foi jogado (somando isso com o numero de vezes que
		 * ele aparece no dorme, pode excluir uma trancada...) 
		 */
		
		boolean taTrancado = true; //ateh que se prove o contrario
		
		for (int i = 0; i < maos.length; i++) {
			for (Pedra pedra : maos[i]) {
				if(pedra.temNumero(mesa.getNumeroEsquerda()) || pedra.temNumero(mesa.getNumeroDireita())){
					taTrancado = false; //provou-se
					break;
				}
			}
		}
		
		return taTrancado;
	}

	private void validaJogada(Jogador jogadorQueJogou, Collection<Pedra> maoDoJogadorQueJogou, Pedra pedra, Lado lado) {
		if(pedra == null || lado == null){
			throw new BugDeJogadorException("Retornou sem dizer direito qual era a pedra e o lado de jogar", jogadorQueJogou);
		}
		
		if(!maoDoJogadorQueJogou.contains(pedra)){
			throw new BugDeJogadorException("Jogando pedra que nao tinha! ", jogadorQueJogou, pedra);
		}
		
	}

	private void embaralhaEdistribui() {
		List<Pedra> pedras = Arrays.asList(Pedra.values());
		Collections.shuffle(pedras);
		
        this.maos = new Collection[4];
        for (int i = 0; i < 4 ; i++) {
        	ArrayList<Pedra> mao = new ArrayList<Pedra>(6); //definir qual a melhor Collection usar
        	for(int j = 0; j < 6; j++){
        		mao.add(pedras.get((i*6) + j));
        	}
			this.maos[1] = mao; 
		}
        
		this.dorme = new Pedra[4];
		for (int i = 24; i < 28 ; i++) {
			dorme[i] = pedras.get(i);
		}
	}

	private int defineDeQuemEAVezDeComecar(Dupla duplaQueComeca) {
		int vez;
		if(duplaQueComeca == null){
			//primeira rodada. comeca quem tiver carroca
			vez = veQuemTemAMaiorCarroca();
		} else {
			int quemDaDuplaComeca = duplaQueComeca.quemComeca();
			//assert(duplaQueComeca.contem(quemDaDuplaComeca));
			vez = duplaQueComeca == dupla1 ? quemDaDuplaComeca * 2 : ((quemDaDuplaComeca * 2) + 1);    
		}
		return vez;
		
	}

	private int veQuemTemAMaiorCarroca() {
		int vez = -1;
		
		for (int i = 6; i >= 2; i--) {
			Pedra carroca = Pedra.carrocas[i];
			for (int j = 0; j < 4; j++) {
				if(this.maos[i].contains(carroca)){
					vez = i;
					break;
				}
			}
		}
		return vez;
	}

	private Jogador pegaJogadorDaVez(int vez) {
		Dupla dupla = (vez%2)==0?dupla1:dupla2;
		return vez<2?dupla.getJogador1():dupla.getJogador2();
	}

	private int avanca(int vez){
		return (vez+1)%4;
	}
	
}
