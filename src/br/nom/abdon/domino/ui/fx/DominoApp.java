/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.Collection;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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

        final double PROPORCAO_ALTURA_LARGURA_MESA = 0.6;
        final double PROPORCAO_MESA_JANELA = 0.95;

        Rectangle mesa = UtilsFx.retanguloProporcaoFixa(
                LARGURA_DA_MESA, 
                0.6);
        mesa.setId("mesa");
        
        
        final ReadOnlyDoubleProperty bndLarguraJanela = root.widthProperty();
        final ReadOnlyDoubleProperty bndAlturaJanela = root.heightProperty();
        final DoubleProperty bndLarguraDaMesa = mesa.widthProperty();
                
        // here we bind rectangle size to pane size 
        bndLarguraDaMesa.bind(Bindings.min(bndLarguraJanela.multiply(PROPORCAO_MESA_JANELA),bndAlturaJanela.multiply(PROPORCAO_MESA_JANELA/PROPORCAO_ALTURA_LARGURA_MESA)));
        
//        mesa.heightProperty().bind(bndLarguraDaMesa.multiply(PROPORCAO_ALTURA_LARGURA_MESA));

        centraliza(root,mesa);

        PedraFx pedra1 = new PedraFx(Pedra.QUINA_SENA);
        final DoubleBinding bindingUmQuintoLarguraDaMesa = mesa.widthProperty().divide(20);
        pedra1.widthProperty().bind(bindingUmQuintoLarguraDaMesa);
        UtilsFx.posiciona(mesa, pedra1, pedra1.widthProperty(), pedra1.heightProperty(), 50,50);
        
//        pedra1.layoutXProperty().bind(mesa.layoutXProperty().add(bindingUmQuintoLarguraDaMesa.divide(3)));
//        pedra1.layoutYProperty().bind(mesa.layoutYProperty().add(bindingUmQuintoLarguraDaMesa.divide(3)));

        Collection<Node> nos = root.getChildren();
        nos.add(mesa);
        nos.add(pedra1);
        pedra1.setRotate(90);

        pedra1.setOnMousePressed(
                (event) -> {
                    
                    System.out.println("clicou");
                    PedraFx pedra = new PedraFx(Pedra.DUQUE_SENA);
                    pedra.widthProperty().bind(bindingUmQuintoLarguraDaMesa);
                    nos.add(pedra);
                    pedra.layoutXProperty().bind(pedra1.layoutXProperty().add(pedra1.layoutBoundsProperty().get().getWidth()));
                    pedra.layoutYProperty().bind(pedra1.layoutYProperty());
                    
                    
                }
        );
        
        
        double x = 100;
        double y = 80;
        
        botatexto(nos,x, y+=20,"Mesa W: ",mesa.widthProperty());
        botatexto(nos,x, y+=20,"Mesa H: ",mesa.heightProperty());

        botatexto(nos,x, y+=25,"Mesa ScaleX: ",mesa.scaleXProperty());
        botatexto(nos,x, y+=20,"Mesa ScaleY: ",mesa.scaleYProperty());

        botatexto(nos,x, y+=25,"Mesa X: ",mesa.xProperty());
        botatexto(nos,x, y+=20,"Mesa Y: ",mesa.yProperty());

        botatexto(nos,x, y+=25,"Mesa LayoutX: ",mesa.layoutXProperty());
        botatexto(nos,x, y+=20,"Mesa LayoutY: ",mesa.layoutYProperty());

        botatexto(nos,x, y+=25,"Mesa TransX: ",mesa.translateXProperty());
        botatexto(nos,x, y+=20,"Mesa TransY: ",mesa.translateYProperty());

        botatexto(nos,x+=200, y=100,"root W: ",root.widthProperty());
        botatexto(nos,x,y+=20,"root H: ",root.heightProperty());

        botatexto(nos,x,y+=25,"root LayoutX: ",root.layoutXProperty());
        botatexto(nos,x,y+=20,"root LayoutY: ",root.layoutYProperty());

        botatexto(nos,x,y+=25,"root ScaleX: ",root.scaleXProperty());
        botatexto(nos,x,y+=20,"root ScaleY: ",root.scaleYProperty());

        botatexto(nos,x,y+=25,"root TransX: ",root.translateXProperty());
        botatexto(nos,x,y+=20,"root TransY: ",root.translateYProperty());

        botatexto(nos,x+=200,y=100,"pedra W: ",pedra1.widthProperty());
        botatexto(nos,x,y+=20,"pedra H: ",pedra1.heightProperty());

        botatexto(nos,x,y+=25,"pedra LayoutX: ",pedra1.layoutXProperty());
        botatexto(nos,x,y+=20,"pedra LayoutY: ",pedra1.layoutYProperty());

        botatexto(nos,x,y+=25,"pedra ScaleX: ",pedra1.scaleXProperty());
        botatexto(nos,x,y+=20,"pedra ScaleY: ",pedra1.scaleYProperty());

        botatexto(nos,x,y+=25,"pedra TransX: ",pedra1.translateXProperty());
        botatexto(nos,x,y+=20,"pedra TransY: ",pedra1.translateYProperty());
        
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        
        final String css = DominoApp.class.getResource("domino.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.show();
    }

    
    
    private void centraliza(Region externo, Rectangle interno) {
        centraliza(
                externo.widthProperty(),
                externo.heightProperty(),
                interno.widthProperty(),
                interno.heightProperty(),
                interno.layoutXProperty(),
                interno.layoutYProperty()
        );
    }

    
    private void centraliza(Region externo, Region interno) {
        centraliza(
                externo.widthProperty(),
                externo.heightProperty(),
                interno.widthProperty(),
                interno.heightProperty(),
                interno.layoutXProperty(),
                interno.layoutYProperty()
        );
    }
    
    private void centraliza(
            DoubleExpression expWidthExt, DoubleExpression expHeightExt, 
            DoubleExpression expWidthInt, DoubleExpression expHeightInt,
            DoubleProperty bndLayoutXInt, DoubleProperty bndLayoutYInt){
        bndLayoutXInt.bind(metade(expWidthExt).subtract(metade(expWidthInt)));
        bndLayoutYInt.bind(metade(expHeightExt).subtract(metade(expHeightInt)));
    }
    
    
    private DoubleExpression metade(DoubleExpression exp){
        return exp.divide(2);
    }
    

    private void botatexto(Collection<Node> nos, double x, double y, String desc, Object val) {
        Text textMesaW = new Text(x,y,"");
        textMesaW.textProperty().bind(Bindings.concat(desc,val));
        nos.add(textMesaW);
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

    
}
