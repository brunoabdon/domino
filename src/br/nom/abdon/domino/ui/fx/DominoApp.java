/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import java.util.Collection;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author bruno
 */
public class DominoApp extends Application {
    
    	public static final float LARGURA_DA_MESA = 1260;
	public static final float ALTURA_DA_MESA = LARGURA_DA_MESA*0.6f;

	public static final float ALTURA_DA_PEDRA = LARGURA_DA_MESA/10;
	public static final float LARGURA_DA_PEDRA = ALTURA_DA_PEDRA/2f;

	private static final float afastacaoDaLinha = LARGURA_DA_PEDRA*0.1f;

      	private static final float raioDosPontinhos = LARGURA_DA_PEDRA/9f;
	private static final float afastacaoDosPontinhos = LARGURA_DA_PEDRA*0.2f;

	private static final float pontinhosDaEsquerdaX = afastacaoDosPontinhos;
	private static final float pontinhosDaDireitaX = LARGURA_DA_PEDRA - afastacaoDosPontinhos;

	private static final float pontinhoDoMeioX = (LARGURA_DA_PEDRA/2);
	private static final float pontinhoDoMeioY = (ALTURA_DA_PEDRA)/4f;

	
	private static final float pontinhosDaPrimeiraLinhaY = afastacaoDosPontinhos;
	private static final float pontinhosDaSegundaLinhaY = pontinhoDoMeioY; 
	private static final float pontinhosDaTerceiraLinhaY =  (ALTURA_DA_PEDRA/2) - afastacaoDosPontinhos;

        
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Domino");
        
        

        
//        drawShapes(gc);
        
        Node pedra1 = fazPedra(Pedra.QUINA_SENA);
        pedra1.setTranslateX(20);
        pedra1.setTranslateY(20);
        
        Rectangle mesa = new Rectangle(LARGURA_DA_MESA,ALTURA_DA_MESA);
        mesa.setId("mesa");
        
        
        
        
        Group root = new Group();
        Collection<Node> nos = root.getChildren();
        nos.add(mesa);
        nos.add(pedra1);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        
        final String css = DominoApp.class.getResource("domino.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.show();
        final Duration duracao = Duration.millis(1500);
        final Duration metadeDaDuraco = duracao.divide(2);
        
        TranslateTransition translation = new TranslateTransition(duracao);
        translation.setToX(pedra1.getTranslateX() + 400);
        
        RotateTransition rotation = new RotateTransition(duracao);
        rotation.setByAngle(90);

        ScaleTransition encolhe = new ScaleTransition(metadeDaDuraco);
        encolhe.setByX(-0.8);

        ScaleTransition cresce = new ScaleTransition(metadeDaDuraco);
        cresce.setByX(0.8);
        
        SequentialTransition vira = new SequentialTransition(encolhe,cresce);
        final Group g = (Group)pedra1;
        
        ParallelTransition transicao = new ParallelTransition(pedra1,vira);
//        transicao.setOnFinished((ActionEvent t) -> {
//            ((Shape)g.getChildren().get(2)).setFill(Color.TRANSPARENT);
//        });
        
        
        transicao.play();
        

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

    private Node fazPedra(Pedra pedra) {
        
        Group desenhoPedraEmBranco = fazPedraEmBranco();
        Group pontinhos = fazPontinhos(pedra);

//        Rectangle rec = new Rectangle(LARGURA_DA_PEDRA, ALTURA_DA_PEDRA);
        
        Group desenhoPedra = new Group(desenhoPedraEmBranco,pontinhos);
        desenhoPedra.setId(pedra.name());
        desenhoPedra.getStyleClass().add("pedra");
        
        return desenhoPedra;
        
    }

    private Group fazPedraEmBranco() {
        Rectangle retanguloPedra = new Rectangle(LARGURA_DA_PEDRA, ALTURA_DA_PEDRA);
        Line linhaDoMeio = new Line(0,0,LARGURA_DA_PEDRA-2*afastacaoDaLinha,0);
        linhaDoMeio.translateXProperty().set(afastacaoDaLinha);
        linhaDoMeio.translateYProperty().set(ALTURA_DA_PEDRA/2);
        linhaDoMeio.getStyleClass().add("linhaDePedra");
        Group desenhoPedra = new Group(retanguloPedra,linhaDoMeio);
        return desenhoPedra;
    }

    private Group fazPontinhos(Pedra pedra) {
        Group pontinhosPrimeiroNumero = fazPontinhos(pedra.getPrimeiroNumero());
        Group pontinhosSegundoNumero = fazPontinhos(pedra.getSegundoNumero());
        pontinhosSegundoNumero.setTranslateY(ALTURA_DA_PEDRA/2);
        
        Group pontinhosDaPEdra = new Group(pontinhosPrimeiroNumero,pontinhosSegundoNumero);
        return pontinhosDaPEdra;
        
    }

    
    private Group fazPontinhos(final Numero numero) {
        final Group grupoDePontinhos = new Group();
        final ObservableList<Node> pontinhos = grupoDePontinhos.getChildren();
        
        final int numeroDePontos = numero.getNumeroDePontos();
        
        if((numeroDePontos % 2)!= 0){
            pontinhos.add(fazPontinho(pontinhoDoMeioX, pontinhoDoMeioY));
        }
                
        if(numeroDePontos >= 2){
            pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaPrimeiraLinhaY));
            pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaTerceiraLinhaY));

            if(numeroDePontos >= 4){
                pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaTerceiraLinhaY));
                pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaPrimeiraLinhaY));
            
                if(numeroDePontos == 6){
                    pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaSegundaLinhaY));
                    pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaSegundaLinhaY));
                }
            
            }
        }
        
        return grupoDePontinhos;
        
    }

    
    private Shape fazPontinho(float x, float y) {
        Circle pontinho = fazPontinho();
        pontinho.setTranslateX(x);
        pontinho.setTranslateY(y);
        return pontinho;
    }

    private Circle fazPontinho() {
        Circle pontinho = new Circle(raioDosPontinhos);
        pontinho.getStyleClass().add("pontinho");
        return pontinho;
    }
    
}
