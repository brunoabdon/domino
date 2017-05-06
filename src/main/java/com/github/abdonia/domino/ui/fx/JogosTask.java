package com.github.abdonia.domino.ui.fx;

import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.app.DominoConfig;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.exemplos.JogadorMamao;
import com.github.abdonia.domino.exemplos.JogadorQueNaoGostaDeCarroca;
import com.github.abdonia.domino.motor.Jogo;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;

/**
 *
 * @author bruno
 */
public class JogosTask extends Task<Void> implements DominoEventListener{

    public final IntegerProperty vitoriasDupla1, empates, vitoriasDupla2;
    
    public JogosTask() {
        this.vitoriasDupla1 = new SimpleIntegerProperty(0);
        this.empates = new SimpleIntegerProperty(0);
        this.vitoriasDupla2 = new SimpleIntegerProperty(0);
    }
    
    
    
    
    @Override
    protected Void call() throws Exception {
        final DominoConfig config = new DominoConfig();
        config.setNomeEClasse("Bruno", JogadorMamao.class.getName(), 1, 1);
        config.setNomeEClasse("Ronaldo", JogadorMamao.class.getName(), 2, 1);
        config.setNomeEClasse("Igor", JogadorMamao.class.getName(), 1, 2);
        config.setNomeEClasse("Eudes", JogadorMamao.class.getName(), 2, 2);
        config.addEventListener(getClass().getName());
        
        
        for (long i = 0; i < 90000000; i++) {
            final Jogo jogo = new Jogo(config);
            jogo.jogar();
            if(i%100==0) Thread.sleep(10);
        }
        
        return null;
    }
    
    @Override
    public void partidaEmpatou() {
        incrementaPlacar(this.empates);
    }

    @Override
    public void jogadorBateu(final int quemFoi, final Vitoria tipoDeVitoria) {
        final IntegerProperty placar = 
            (quemFoi == 1 || quemFoi == 3)
            ? vitoriasDupla1
            : vitoriasDupla2;
        
        incrementaPlacar(placar);
    }
 
 
    public void incrementaPlacar(final IntegerProperty prop) {
        Platform.runLater(() ->  prop.set(prop.get()+1));
    }
    
}
