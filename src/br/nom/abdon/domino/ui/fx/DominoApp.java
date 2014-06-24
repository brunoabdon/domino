/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        
        cenarioDeJogo.adicionaPedras();
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
