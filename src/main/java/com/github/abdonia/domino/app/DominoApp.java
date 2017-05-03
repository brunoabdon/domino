package com.github.abdonia.domino.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.abdonia.domino.log.RawLogger;
import com.github.abdonia.domino.log.LoggerDominoEventListener;
import com.github.abdonia.domino.motor.JogadorWrapper;
import com.github.abdonia.domino.motor.Jogo;

public class DominoApp {

    private static final String CONFIG_XML = "domino-config.xml";
    private static final String DEFAULT_CONFIG_XML = "/domino-config-default.xml";
    private static final String MSG_BUNDLE = "DominoAppMsg";

    
    public static void main(final String[] args) {
        
        
        try {
            final DominoXmlConfigLoader cfgLoader = loadConfig();

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
            //jogo.addEventListener(new RawLogger());
            jogo.jogar();

        } catch (DominoAppException e) {
            log(Level.WARNING, "Pipoco: ", e);
        }
            
    }

    private static DominoXmlConfigLoader loadConfig() 
            throws DominoAppException {

        final DominoXmlConfigLoader cfgLoader;
        
        final InputStream is;
        final File configFile = new File(CONFIG_XML);

        try {
            if(configFile.exists()){
                is = new FileInputStream(configFile);
            } else {
                final ResourceBundle msgBundle = 
                        ResourceBundle.getBundle(MSG_BUNDLE);
                
                final String msg = 
                    MessageFormat.format(
                        msgBundle.getString("msg.defaultconfig"),
                        configFile.getAbsolutePath());
                
                System.out.println(msg);
                System.console().readLine();

                is = DominoApp.class.getResourceAsStream(DEFAULT_CONFIG_XML);
                assert is != null;
            }
            
            cfgLoader = new DominoXmlConfigLoader();
            cfgLoader.carregaConfiguracoes(is);
        } catch (FileNotFoundException ex) {
            throw new DominoAppException(ex, "Erro ao ler arquivo");
        }
        
        return cfgLoader;
    }

    
    private static void log(final Level l, final String msg, final Exception e){
        System.err.printf("%s: %s\n",msg, e.getMessage());
        Logger.getLogger(DominoApp.class.getName()).log(l, msg, e);
    }
    
}
