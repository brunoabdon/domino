/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author bruno
 */
public class DominoApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Domino");
        Pane root = new Pane();
        
        final CenarioDeJogo cenarioDeJogo = new CenarioDeJogo();

        root.getChildren().add(cenarioDeJogo);

        Scene scene = new Scene(root, 800, 600);
        setCss(scene,"domino.css");
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        List<Jogada> jogo = new LinkedList<>();
        jogo.add(new Jogada(Pedra.TERNO_QUADRA));
        jogo.add(new Jogada(Pedra.CARROCA_DE_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_TERNO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.LIMPO_PIO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_PIO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.PIO_DUQUE,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.DUQUE_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.PIO_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.PIO_TERNO,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.DUQUE_TERNO,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.QUADRA_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_DUQUE,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_QUADRA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.DUQUE_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.QUINA_SENA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.TERNO_SENA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_TERNO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.QUADRA_SENA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.TERNO_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_SENA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.PIO_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.LIMPO_SENA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_LIMPO,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.PIO_SENA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.LIMPO_DUQUE,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.DUQUE_SENA,Lado.DIREITO));
        
        final Iterator<Jogada> iterator = jogo.iterator();
        cenarioDeJogo.setOnMouseClicked(
            e -> {
                cenarioDeJogo.adicionaPedras();
                cenarioDeJogo.sentaJogadores("Bruno", "Ronaldo", "Igor", "Eudes");
                cenarioDeJogo.setOnMouseClicked(
                    e2 -> {
                        
                        final List<Pedra> pedras = Arrays.asList(Pedra.values());
                        Collections.shuffle(pedras);

                        //distribui as maos dos 4 jogadores
                        for (int i = 0, idx = 0; i < 4; i++) {
                            final Collection<Pedra> mao = pedras.subList(idx, idx+=6); //imutavel
                            cenarioDeJogo.entregaPedras(i, mao);
                        }

                        
                        cenarioDeJogo.setOnMouseClicked(
                            e3 -> {
                                if(iterator.hasNext()){
                                    Jogada jogada = iterator.next();
                                    cenarioDeJogo.jogaPedra(jogada.getPedra(), jogada.getLado());
                                } 
                            }
                        );
                    }
                );
            }
        );

        
        
    }

    private void setCss(Scene scene, String resource) {
        final String css = DominoApp.class.getResource(resource).toExternalForm();
        scene.getStylesheets().add(css);
    }

    private MenuBar fazMenu(){
        MenuBar menuBar = new MenuBar();
 
        // --- Menu File
        Menu menuFile = new Menu("File");
 
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
 
        // --- Menu View
        Menu menuView = new Menu("View");
 
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
 
 
        return menuBar;
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    
}
