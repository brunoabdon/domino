package br.nom.abdon.domino.motor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.eventos.EventoDomino;
import br.nom.abdon.domino.eventos.EventoDomino.Tipo;

class Partida {

	private Dupla dupla1, dupla2;

	private MesaImpl mesa;
	private Pedra[] dorme = new Pedra[4];

	private Collection<Pedra>[] maos;
	
	private DominoEventListener eventListener;
	
	public Partida(Dupla dupla1, Dupla dupla2, DominoEventListener eventListener) {
		super();
		this.mesa = new MesaImpl();
		this.dupla1 = dupla1;
		this.dupla2 = dupla2;
		
		this.eventListener = eventListener;
	}

	protected ResultadoPartida jogar(Dupla duplaQueGanhouApartidaAnterior) throws BugDeJogadorException{
		
		JogadorWrapper jogadorDaVez = null;
		String nomeJogadorDaVez = null;
		
		Pedra pedra = null;
		Lado lado = null;
		
		boolean alguemBateu = false, trancou = false;
		
		embaralhaEdistribui();
		
		boolean ehPrimeiraRodada = duplaQueGanhouApartidaAnterior == null;
		
		int vez;
		if(ehPrimeiraRodada){
			vez = primeiraJogada();
		} else {
			vez = decideDeQuemDosDoisVaiComecar(duplaQueGanhouApartidaAnterior);
		}
		
		while(!(alguemBateu || trancou)){
			
			jogadorDaVez = jogadorDaVez(vez);
			nomeJogadorDaVez = jogadorDaVez.getNome();

			Collection<Pedra> maoDoJogadorDaVez = this.maos[vez];
			
			Jogada jogada = jogadorDaVez.joga(mesa);
			
			if(jogada == Jogada.TOQUE){
				this.eventListener.eventoAconteceu(new EventoDomino(Tipo.JOGADOR_TOCOU,nomeJogadorDaVez));
			} else {
				//se livrando logo do objeto Jogada, que veio do jogador.
				lado = jogada.getLado();
				pedra = jogada.getPedra();

				validaJogada(jogadorDaVez,maoDoJogadorDaVez,pedra,lado);
				
				maoDoJogadorDaVez.remove(pedra);

				this.mesa.coloca(pedra, lado);
				
				this.eventListener.eventoAconteceu(new EventoDomino(Tipo.JOGADOR_JOGOU,nomeJogadorDaVez,pedra,lado));
				
				alguemBateu = maoDoJogadorDaVez.isEmpty();
				if(!alguemBateu) {
					trancou = verificaTrancada();
				}
			}
			
			vez = avanca(vez);
		}
		
		ResultadoPartida resultadoPartida;
		
		EventoDomino eventoPartidaAcabou = null;

		if(trancou){
			resultadoPartida = contaPontos();
			if(resultadoPartida == ResultadoPartida.EMPATE){
				eventoPartidaAcabou = new EventoDomino(Tipo.PARTIDA_ACABOU, true);
			}
		} else {
			Vitoria tipoDaBatida = veOTipoDaBatida(pedra);
			resultadoPartida = new ResultadoPartida(tipoDaBatida,jogadorDaVez);
		}
		
		if(eventoPartidaAcabou == null){
			eventoPartidaAcabou = new EventoDomino(Tipo.PARTIDA_ACABOU, resultadoPartida.getVencedor().getNome(), resultadoPartida.getTipoDeVitoria());
		}

		this.eventListener.eventoAconteceu(eventoPartidaAcabou);
		
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
//				break;
			}
		}
		
		if(resultado == null){
			JogadorWrapper jogadorComMenosPontosNaMao = jogadorDaVez(idxJogadorComMenos);
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
		
		boolean taTrancado = true; //até que se prove o contrario
		
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

	private void validaJogada(Jogador jogadorQueJogou, Collection<Pedra> maoDoJogadorQueJogou, Pedra pedra, Lado lado) throws BugDeJogadorException {
		if(pedra == null){
			throw new BugDeJogadorException("Cade a pedra?", jogadorQueJogou);
		}
		
		if(lado == null && !mesa.taVazia() && mesa.getNumeroEsquerda() != mesa.getNumeroDireita()){
			throw new BugDeJogadorException("De que lado é pra botar essa pedra?", jogadorQueJogou);
		}
		
		
		if(!maoDoJogadorQueJogou.contains(pedra)){
			throw new BugDeJogadorException("Jogando pedra que nao tinha! ", jogadorQueJogou, pedra);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void embaralhaEdistribui() {
		List<Pedra> pedras = Arrays.asList(Pedra.values());
		Collections.shuffle(pedras);
		
        this.maos = new Collection[4];
        for (int i = 0; i < 4 ; i++) {
        	System.out.println("mao " + i);
        	ArrayList<Pedra> mao = new ArrayList<Pedra>(6); //definir qual a melhor Collection usar
        	Pedra[] mao_ = new Pedra[6]; 
        	for(int j = 0; j < 6; j++){
        		Pedra pedra = pedras.get((i*6) + j);
				mao.add(pedra);
				mao_[j] = pedra;
				 System.out.println(pedra);
        	}
        	System.out.println();
			this.maos[i] = mao; 
			jogadorDaVez(i).recebeMao(mao_);
		}
        
		this.dorme = new Pedra[4];
		for (int i = 24; i < 28 ; i++) {
			dorme[i-24] = pedras.get(i);
		}
	}

	private int decideDeQuemDosDoisVaiComecar(Dupla duplaQueComeca) throws BugDeJogadorException {
		int quemDaDuplaComeca = duplaQueComeca.quemComeca();
		return duplaQueComeca == dupla1 ? quemDaDuplaComeca * 2 : ((quemDaDuplaComeca * 2) + 1);    
	}

	private int primeiraJogada() throws BugDeJogadorException{
		
		int vez = -1;
		Jogada primeiraJogada = null; 
		for (int i = 6; i >= 2 && primeiraJogada == null; i--) {
			Pedra carroca = Pedra.carrocas[i];
			for (int j = 0; j < 4; j++) {
				if(this.maos[j].contains(carroca)){
					vez = j;
					JogadorWrapper jogadorQueComeca = jogadorDaVez(vez);
					primeiraJogada = jogadorQueComeca.joga(mesa);

					Pedra pedra = primeiraJogada.getPedra();
					this.eventListener.eventoAconteceu(new EventoDomino(Tipo.JOGADOR_JOGOU,jogadorQueComeca.getNome(),pedra,null));
					
					//agora erre, meu velho
					if(pedra != carroca){
						throw new BugDeJogadorException("Comecou com a pedra errada me velho. Erra pra ser " + carroca,jogadorQueComeca);
					}
					//limpesa
					this.maos[j].remove(pedra);
					this.mesa.coloca(pedra,primeiraJogada.getLado());
					
					break;
				}
			}
		}
		return avanca(vez);
	}

	private JogadorWrapper jogadorDaVez(int vez) {
		Dupla dupla = duplaDaVez(vez);
		return vez<2?dupla.getJogador1():dupla.getJogador2();
	}

	private Dupla duplaDaVez(int vez) {
		return (vez%2)==0?dupla1:dupla2;
	}

	private int avanca(int vez){
		return (vez+1)%4;
	}
}
