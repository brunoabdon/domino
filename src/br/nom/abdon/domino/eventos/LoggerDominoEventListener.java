package br.nom.abdon.domino.eventos;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.EventoDomino.Tipo;

public class LoggerDominoEventListener implements DominoEventListener {

	private String nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4;
	
	
	@Override
	public void eventoAconteceu(EventoDomino eventoDomino) {
		Tipo tipoDoEvento = eventoDomino.getTipo();
		switch (tipoDoEvento) {
			case JOGO_COMECOU: comecouJogo(eventoDomino); break;	
			case PARTIDA_COMECOU: comecouPartida(eventoDomino); break;
			case JOGADOR_JOGOU: jogardorJogou(eventoDomino); break;
			case JOGADOR_TOCOU: jogadorTocou(eventoDomino); break;
			case PARTIDA_ACABOU: acabouPartida(eventoDomino); break;
			case JOGO_ACABOU: jogoAcabou(eventoDomino); break;
		}
	}
	
	
	private void comecouPartida(EventoDomino eventoDomino) {
		System.out.println("Comecando partida.");
		imprimePlacar(eventoDomino.getPontosDupla1(), eventoDomino.getPontosDupla2());

	}

	
	private void jogardorJogou(EventoDomino eventoDomino) {
		
		String nomeDoJogador = eventoDomino.getQuemFoi();
	    Pedra pedra = eventoDomino.getPedra();
		Lado lado = eventoDomino.getLado();
		
		String str = nomeDoJogador +  ":\t" + pedra;
		if(lado != null){
			 str += " (" + lado + ")";
		}
		System.out.println(str);

	}

	
	private void jogadorTocou(EventoDomino eventoDomino){
		String nomeDoJogador = eventoDomino.getQuemFoi();
		System.out.println(nomeDoJogador + ":\t\"toc, toc.\"");

	}

	
	private void comecouJogo(EventoDomino eventoDomino){
		System.out.println("Comecou o jogo");
		this.nomeDoJogador1 = eventoDomino.getNomeDoJogador1();
		this.nomeDoJogador2 = eventoDomino.getNomeDoJogador2();
		this.nomeDoJogador3 = eventoDomino.getNomeDoJogador3();
		this.nomeDoJogador4 = eventoDomino.getNomeDoJogador4();
		imprimePlacar(0,0);
		
	}

	private void imprimePlacar(int placarDupla1, int placarDupla2) {
		System.out.println(nomeDoJogador1 + " e " + nomeDoJogador3 
				           + " " + placarDupla1 + " x " + placarDupla2 + " " 
				           + nomeDoJogador2 + " e " + nomeDoJogador4);
		
	}

	
	private void acabouPartida(EventoDomino eventoDomino) {
		String nomeDoJogador = eventoDomino.getQuemFoi();
		Vitoria tipoDeVitoria = eventoDomino.getTipoDeVitoria();
		
		System.out.println(nomeDoJogador + ":\tbateu.");
		if(tipoDeVitoria != Vitoria.BATIDA_SIMPLES){
			System.out.println("("+ tipoDeVitoria+ ")");
		}
		
	}

	
	private void jogoAcabou(EventoDomino eventoDomino) {
		int placarDupla1 = eventoDomino.getPontosDupla1();
		int placarDupla2 = eventoDomino.getPontosDupla2();
		
		System.out.println("Acabou!");
		imprimePlacar(placarDupla1,placarDupla2);
		
		int min = placarDupla1 < placarDupla2 ? placarDupla1 : placarDupla2;
		
		if(min == 0){
			System.out.println("   =======================================   ");
			System.out.println("   =======================================   ");
			System.out.println("   ===========    BUXUDINHA!!!      ======   ");
			System.out.println("   =======================================   ");
			System.out.println("   ===========    BUXUDINHA!!!      ======   ");
			System.out.println("   =======================================   ");
			System.out.println("   ===========    BUXUDINHA!!!      ======   ");
			System.out.println("   =======================================   ");
			System.out.println("   =======================================   ");
		} else if (min == 1){
			System.out.println("   =======================================   ");
			System.out.println("   ===========    INCHADINHA!      ======   ");
			System.out.println("   =======================================   ");
		}
		
	}

}
