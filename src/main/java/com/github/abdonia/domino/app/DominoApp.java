package com.github.abdonia.domino.app;

import com.github.abdonia.domino.log.RawLogger;
import com.github.abdonia.domino.log.LoggerDominoEventListener;
import com.github.abdonia.domino.motor.JogadorWrapper;
import com.github.abdonia.domino.motor.Jogo;

public class DominoApp {

    public static void main(final String[] args) {

        try {
            final DominoXmlConfigLoader cfgLoader = new DominoXmlConfigLoader();	
            cfgLoader.carregaConfiguracoes();

            final JogadorWrapper jogador1dupla1 = cfgLoader.getJogador1Dupla1(); 
            final JogadorWrapper jogador1dupla2 = cfgLoader.getJogador1Dupla2(); 
            final JogadorWrapper jogador2dupla1 = cfgLoader.getJogador2Dupla1(); 
            final JogadorWrapper jogador2dupla2 = cfgLoader.getJogador2Dupla2(); 

            final LoggerDominoEventListener loggerDominoEventListener = 
                new LoggerDominoEventListener();
            final Jogo jogo = 
                new Jogo(
                    jogador1dupla1, 
                    jogador1dupla2, 
                    jogador2dupla1, 
                    jogador2dupla2);

            jogo.addEventListener(loggerDominoEventListener);
            jogo.addEventListener(new RawLogger());
            jogo.jogar();

        } catch (DominoAppException e) {
            System.err.println("Pipoco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
