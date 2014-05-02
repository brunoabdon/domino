/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.Collection;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import br.nom.abdon.domino.Pedra;

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
        Pane root = new Pane();

        Rectangle mesa = new Rectangle();//LARGURA_DA_MESA,ALTURA_DA_MESA);
        mesa.setId("mesa");
        
        final double PROPORCAO_ALTURA_LARGURA_MESA = 0.6;
        final double PROPORCAO_MESA_JANELA = 0.95;
        
        // here we bind rectangle size to pane size 
        mesa.widthProperty().bind(Bindings.min(root.widthProperty().multiply(PROPORCAO_MESA_JANELA),root.heightProperty().multiply(PROPORCAO_MESA_JANELA).divide(PROPORCAO_ALTURA_LARGURA_MESA)));
        mesa.heightProperty().bind(mesa.widthProperty().multiply(PROPORCAO_ALTURA_LARGURA_MESA));

        mesa.xProperty().bind(root.widthProperty().divide(2).subtract(mesa.widthProperty().divide(2)));
        mesa.yProperty().bind(root.heightProperty().divide(2).subtract(mesa.heightProperty().divide(2)));

//        mesa.widthProperty().addListener(node -> {System.out.println(mesa.getWidth() + " x " +mesa.getHeight() + "|" + root.getScaleX() + " x " +root.getScaleY());});
//        mesa.widthProperty().addListener(node -> {System.out.println(mesa.widthProperty().get() + " x " + mesa.heightProperty().get());});

        PedraFx pedra1 = new PedraFx(Pedra.QUINA_SENA);
        pedra1.heightProperty().bind(mesa.widthProperty().divide(10));

        pedra1.layoutXProperty().bind(mesa.xProperty().add(mesa.widthProperty().divide(2)));
        pedra1.layoutYProperty().bind(mesa.yProperty().add(mesa.heightProperty().divide(3)));

        Collection<Node> nos = root.getChildren();
        nos.add(mesa);
        nos.add(pedra1);

        
        Scene scene = new Scene(root, 800, 600);
        

        
        
        primaryStage.setScene(scene);

        
     Path path = new Path();
     path.getElements().add (new MoveTo(pedra1.getTranslateX(),pedra1.getTranslateY()));
     path.getElements().add (new LineTo(100,200));

        PathTransition pathTransition = new PathTransition();
     
     pathTransition.setDelay(Duration.millis(600));
     pathTransition.setDuration(Duration.millis(600));
     pathTransition.setNode(pedra1);
     pathTransition.setPath(path);
     pathTransition.setOrientation(OrientationType.NONE);
//     pathTransition.setCycleCount(6);
//     pathTransition.setAutoReverse(false);

//     pathTransition.play();

        
//        mover(mesa, pedra1, 100, 0);
//        pedra1.relocate(100, 340);

        
        final String css = DominoApp.class.getResource("domino.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.show();
//        final Duration duracao = Duration.millis(600);
//        final Duration metadeDaDuraco = duracao.divide(2);
//        
//        TranslateTransition translation = new TranslateTransition(duracao);
//        translation.setToX(pedra1.getTranslateX() + 100);
//        
//        RotateTransition rotation = new RotateTransition(duracao);
//        rotation.setByAngle(90);
//
//        ScaleTransition encolhe = new ScaleTransition(metadeDaDuraco);
//        encolhe.setByX(-0.8);
//
//        ScaleTransition cresce = new ScaleTransition(metadeDaDuraco);
//        cresce.setByX(0.8);
//        
//        SequentialTransition vira = new SequentialTransition(encolhe,cresce);
//        final Group g = (Group)pedra1;
//        
//        ParallelTransition transicao = new ParallelTransition(pedra1,translation);
//        
//        transicao.setOnFinished(
//                (o) -> {
//                    final double larguraMesa = mesa.getWidth();
//                    final double xPedra = pedra1.getTranslateX();
//
//                    System.out.println("mesax = " + mesa.getX());
////                    System.out.println("largura mesa = " + larguraMesa);
//                    System.out.println("xPedra = " + pedra1.getTranslateX());
//
//                    pedra1.translateXProperty().addListener(
//                            (x) -> {
//                                System.out.println("mudando xPedra = " + pedra1.getTranslateX());
//                                System.out.println("mesax = " + mesa.getX());
//                                System.out.println("mesatx = " + mesa.getTranslateX());
//                                System.out.println("mesaw = " + mesa.getWidth());
//                                                        
//                            }
//                    );
//                    
//                    final double xrelativopedra = xPedra - mesa.getTranslateX();
//
//                    pedra1.translateXProperty().bind(
//                            mesa.xProperty().subtract(mesa.getX()).add(
//                            mesa.widthProperty().multiply(xrelativopedra/mesa.getWidth())));
//
//                }
//                
//                
//        );
        
//        pedra1.translateXProperty().unbind();
//        transicao.play();

    }
    
    
    private void mover(Rectangle mesa, PedraFx pedra, int x, int y){

        TranslateTransition translation = new TranslateTransition(Duration.millis(600),pedra);
        translation.setToX(x);
        translation.setToY(y);
//        translation.setOnFinished(
//          (ActionEvent h) -> {
//              final double xrelativopedra = pedra.getTranslateX();
//              final double yrelativopedra = pedra.getTranslateY();
//
//
//            pedra.translateYProperty().addListener(
//            
//               (l) -> {
//            
//            System.out.println("\nmesay = " + mesa.getY());
//            System.out.println("mesaty = " + mesa.getTranslateY());
//            System.out.println("mesah = " + mesa.getHeight());
//            System.out.println("pty = " + pedra.getTranslateY());
//            System.out.println("pty/mh = " + yrelativopedra / mesa.getHeight());
//               
//               }
//            );
//            
//            
//            
//            
//              pedra.translateXProperty().bind(
//                      mesa.xProperty().subtract(mesa.getX()).add(
//                              mesa.widthProperty().multiply(xrelativopedra / mesa.getWidth())));
//
//              pedra.translateYProperty().bind(
//                      mesa.yProperty().subtract(mesa.getY()).add(
//                              mesa.heightProperty().multiply(yrelativopedra / mesa.getHeight())));
//          
//          }
//        );
//        
//        pedra.translateXProperty().unbind();
//        pedra.translateYProperty().unbind();
        
        translation.play();

        
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

//    private Group fazPedra(Pedra pedra) {
//        
//        Group desenhoPedraEmBranco = fazPedraEmBranco();
//        Group pontinhos = fazPontinhos(pedra);
//
////        Rectangle rec = new Rectangle(LARGURA_DA_PEDRA, ALTURA_DA_PEDRA);
//        
//        Group desenhoPedra = new Group(desenhoPedraEmBranco,pontinhos);
//        desenhoPedra.setId(pedra.name());
//        desenhoPedra.getStyleClass().add("pedra");
//        
//        return desenhoPedra;
//        
//    }

//    private Group fazPedraEmBranco() {
//        Rectangle retanguloPedra = new Rectangle(LARGURA_DA_PEDRA, ALTURA_DA_PEDRA);
//        Line linhaDoMeio = new Line(0,0,LARGURA_DA_PEDRA-2*afastacaoDaLinha,0);
//        linhaDoMeio.translateXProperty().set(afastacaoDaLinha);
//        linhaDoMeio.translateYProperty().set(ALTURA_DA_PEDRA/2);
//        linhaDoMeio.getStyleClass().add("linhaDePedra");
//        Group desenhoPedra = new Group(retanguloPedra,linhaDoMeio);
//        return desenhoPedra;
//    }
//
//    private Group fazPontinhos(Pedra pedra) {
//        Group pontinhosPrimeiroNumero = fazPontinhos(pedra.getPrimeiroNumero());
//        Group pontinhosSegundoNumero = fazPontinhos(pedra.getSegundoNumero());
//        pontinhosSegundoNumero.setTranslateY(ALTURA_DA_PEDRA/2);
//        
//        Group pontinhosDaPEdra = new Group(pontinhosPrimeiroNumero,pontinhosSegundoNumero);
//        return pontinhosDaPEdra;
//        
//    }
//
//    
//    private Group fazPontinhos(final Numero numero) {
//        final Group grupoDePontinhos = new Group();
//        final ObservableList<Node> pontinhos = grupoDePontinhos.getChildren();
//        
//        final int numeroDePontos = numero.getNumeroDePontos();
//        
//        if((numeroDePontos % 2)!= 0){
//            pontinhos.add(fazPontinho(pontinhoDoMeioX, pontinhoDoMeioY));
//        }
//                
//        if(numeroDePontos >= 2){
//            pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaPrimeiraLinhaY));
//            pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaTerceiraLinhaY));
//
//            if(numeroDePontos >= 4){
//                pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaTerceiraLinhaY));
//                pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaPrimeiraLinhaY));
//            
//                if(numeroDePontos == 6){
//                    pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaSegundaLinhaY));
//                    pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaSegundaLinhaY));
//                }
//            
//            }
//        }
//        
//        return grupoDePontinhos;
//        
//    }
//
//    
//    private Shape fazPontinho(float x, float y) {
//        Circle pontinho = fazPontinho();
//        pontinho.setTranslateX(x);
//        pontinho.setTranslateY(y);
//        return pontinho;
//    }
//
//    private Circle fazPontinho() {
//        Circle pontinho = new Circle(raioDosPontinhos);
//        pontinho.getStyleClass().add("pontinho");
//        return pontinho;
//    }
    
}
