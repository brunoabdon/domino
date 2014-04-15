package br.nom.abdon.domino.app;

import br.nom.abdon.domino.log.LoggerDominoEventListener;
import br.nom.abdon.domino.motor.Dupla;
import br.nom.abdon.domino.motor.JogadorWrapper;
import br.nom.abdon.domino.motor.Jogo;

public class DominoApp {

	public static void main(String[] args) {
		
		try {
			DominoXmlConfigLoader configLoader = new DominoXmlConfigLoader();	
			configLoader.carregaConfiguracoes();
			
			JogadorWrapper jogador1dupla1 = configLoader.getJogador1Dupla1(); 
			JogadorWrapper jogador1dupla2 = configLoader.getJogador1Dupla2(); 
			JogadorWrapper jogador2dupla1 = configLoader.getJogador2Dupla1(); 
			JogadorWrapper jogador2dupla2 = configLoader.getJogador2Dupla2(); 
			
			Dupla dupla1 = new Dupla(jogador1dupla1, jogador2dupla1);
			Dupla dupla2 = new Dupla(jogador1dupla2, jogador2dupla2);
			
			Jogo jogo = new Jogo(dupla1,dupla2);
                        jogo.addEventListener(new LoggerDominoEventListener());
                        jogo.jogar();
			
			
		} catch (DominoAppException e) {
			System.err.println("Pipoco: " + e.getMessage());
			e.printStackTrace();
		}
		
		

	}
}
