package br.nom.abdon.domino.ui.fx;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import br.nom.abdon.domino.eventos.DominoEventListener;
import br.nom.abdon.domino.exemplos.JogadorMamao;
import br.nom.abdon.domino.exemplos.JogadorQueNaoGostaDeCarroca;
import br.nom.abdon.domino.motor.JogadorWrapper;
import br.nom.abdon.domino.motor.Jogo;
import br.nom.abdon.domino.Vitoria;

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
        final JogadorWrapper j1 = new JogadorWrapper(new JogadorMamao(), "Bruno");
        final JogadorWrapper j2 = new JogadorWrapper(new JogadorQueNaoGostaDeCarroca(), "Ronaldo");
        final JogadorWrapper j3 = new JogadorWrapper(new JogadorQueNaoGostaDeCarroca(), "Igor");
        final JogadorWrapper j4 = new JogadorWrapper(new JogadorQueNaoGostaDeCarroca(), "Eudes");

        for (long i = 0; i < 90000000; i++) {
            final Jogo jogo = new Jogo(j1, j2, j3, j4);
            jogo.addEventListener(this);
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
