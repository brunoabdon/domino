package br.nom.abdon.domino.log;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.DominoEventListener;

public class LoggerDominoEventListener implements DominoEventListener {

	private String nomeDoJogador1, nomeDoJogador2, nomeDoJogador3, nomeDoJogador4;
	
	
	@Override 
        public void partidaComecou(int pontosDupla1, int pontosDupla2, boolean ehDobrada) {
		System.out.println("Comecando partida.");
		imprimePlacar(pontosDupla1,pontosDupla2);

	}

	
	@Override 
        public void jogadorJogou(String nomeDoJogador, Lado lado, Pedra pedra) {
		
		String str = nomeDoJogador +  ":\t" + pedra;
		if(lado != null){
			 str += " (" + lado + ")";
		}
		System.out.println(str);

	}

	
	@Override
        public void jogadorTocou(String nomeDoJogador){
		System.out.println(nomeDoJogador + ":\t\"toc, toc.\"");

	}

	
        @Override
        public void jogoComecou(String nomeDoJogador1, String nomeDoJogador2, String nomeDoJogador3, String nomeDoJogador4){
		System.out.println("Comecou o jogo");
		this.nomeDoJogador1 = nomeDoJogador1;
		this.nomeDoJogador2 = nomeDoJogador2;
		this.nomeDoJogador3 = nomeDoJogador3;
		this.nomeDoJogador4 = nomeDoJogador4;
		imprimePlacar(0,0);
		
	}

	private void imprimePlacar(int placarDupla1, int placarDupla2) {
		System.out.println(nomeDoJogador1 + " e " + nomeDoJogador3 
				           + " " + placarDupla1 + " x " + placarDupla2 + " " 
				           + nomeDoJogador2 + " e " + nomeDoJogador4);
		
	}

	
        @Override
        public void jogadorBateu(String nomeDoJogador, Vitoria tipoDeVitoria) {

            System.out.println(nomeDoJogador + ":\tbateu.");
            if (tipoDeVitoria != Vitoria.BATIDA_SIMPLES) {
                System.out.println("(" + tipoDeVitoria + ")");
            }
        }


       	@Override
        public void partidaEmpatou(){
            System.out.println("Empatou. A proxima vale dobrada.");   
        }

	@Override
        public void jogoAcabou(int placarDupla1,int placarDupla2) {
		
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
