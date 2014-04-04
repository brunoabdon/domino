/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import br.nom.abdon.domino.Pedra;
import java.util.Collection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/**
 *
 * @author bruno
 */
public class DominoApp extends Application {
    
    	public static final float LARGURA_DA_MESA = 1260;
	public static final float ALTURA_DA_MESA = LARGURA_DA_MESA*0.6f;

	public static final float ALTURA_DA_PEDRA = LARGURA_DA_MESA/12;
	public static final float LARGURA_DA_PEDRA = ALTURA_DA_PEDRA/2f;

	private static final float afastacaoDaLinha = LARGURA_DA_PEDRA*0.1f;
	private static final float tamanhoDaLinhaDoMeio = LARGURA_DA_PEDRA - afastacaoDaLinha - afastacaoDaLinha;
    
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Domino");
        
        

        
//        drawShapes(gc);
        
        Node desenhoPioTerno = criaDesenhoDePedra(Pedra.PIO_TERNO);
        desenhoPioTerno.setTranslateX(20);
        desenhoPioTerno.setTranslateY(20);
        
        Rectangle mesa = new Rectangle(LARGURA_DA_MESA,ALTURA_DA_MESA);
        mesa.setId("mesa");
        
        
        
        
        Group root = new Group();
        Collection<Node> nos = root.getChildren();
        nos.add(mesa);
        nos.add(desenhoPioTerno);

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        
        final String css = DominoApp.class.getResource("domino.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        
        primaryStage.show();

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

    private void drawShapes(GraphicsContext gc) {
        
        
    }

    private Node criaDesenhoDePedra(Pedra pedra) {
        
        Rectangle retanguloPedra = new Rectangle(LARGURA_DA_PEDRA, ALTURA_DA_PEDRA);
        retanguloPedra.getStyleClass().add("pedra");
        
        Line linhaDoMeio = new Line(0,0,LARGURA_DA_PEDRA-2*afastacaoDaLinha,0);
        linhaDoMeio.translateXProperty().set(afastacaoDaLinha);
        linhaDoMeio.translateYProperty().set(ALTURA_DA_PEDRA/2);
        linhaDoMeio.getStyleClass().add("linhaDePedra");
        
        Group desenhoPedra = new Group(retanguloPedra,linhaDoMeio);
        desenhoPedra.setId(pedra.name());
        
        return desenhoPedra;
        
    }
    
}
