package br.nom.abdon.domino.app;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.motor.Dupla;
import br.nom.abdon.domino.motor.Jogo;

public class DominoApp {

	public static void main(String[] args) {
		
		try {
			DominoXmlConfigLoader configLoader = new DominoXmlConfigLoader();	
			configLoader.carregaConfiguracoes();
			
			Jogador jogador1dupla1 = carrega(configLoader.getClasseJogador1Dupla1()); 
			Jogador jogador1dupla2 = carrega(configLoader.getClasseJogador1Dupla1()); 
			Jogador jogador2dupla1 = carrega(configLoader.getClasseJogador2Dupla1()); 
			Jogador jogador2dupla2 = carrega(configLoader.getClasseJogador2Dupla2()); 
			
			String nomeJogador1Dupla1 = configLoader.getNomeJogador1Dupla1();
			String nomeJogador2Dupla1 = configLoader.getNomeJogador2Dupla1();
			
			String nomeJogador1Dupla2 = configLoader.getNomeJogador1Dupla2();
			String nomeJogador2Dupla2 = configLoader.getNomeJogador2Dupla2();
			
			Dupla dupla1 = new Dupla(jogador1dupla1, nomeJogador1Dupla1, jogador2dupla1, nomeJogador2Dupla1);
			Dupla dupla2 = new Dupla(jogador1dupla2, nomeJogador1Dupla2, jogador2dupla2, nomeJogador2Dupla2);
			
			Jogo jogo = new Jogo(dupla1,dupla2);
			jogo.jogar();
			
		} catch (DominoAppException e) {
			System.err.println("Pipoco: " + e.getMessage());
			e.printStackTrace();
		}
		
		

	}

	private static Jogador carrega(Class<? extends Jogador> classeJogador) throws DominoAppException {
		Jogador jogador;
		try {
			jogador = classeJogador.newInstance();
		} catch (InstantiationException e) {
			throw new DominoAppException("Jogador nem consegui ser inicializado: " + classeJogador);
		} catch (IllegalAccessException e) {
			throw new DominoAppException("Jogador escodeu ateh o construtor: " + classeJogador);
		}
		return jogador;
	}

}
