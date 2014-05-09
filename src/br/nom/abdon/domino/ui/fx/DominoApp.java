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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import br.nom.abdon.domino.Pedra;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;

/**
 *
 * @author bruno
 */
public class DominoApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Domino");
        Pane root = new Pane();

        final double PROPORCAO_ALTURA_LARGURA_MESA = 0.6;
        final double PROPORCAO_MESA_JANELA = 0.95;

        Rectangle mesa = UtilsFx.retanguloProporcaoFixa(0.6);
        mesa.setId("mesa");
        
        
        final ReadOnlyDoubleProperty bndLarguraJanela = root.widthProperty();
        final ReadOnlyDoubleProperty bndAlturaJanela = root.heightProperty();
        final DoubleProperty bndLarguraDaMesa = mesa.widthProperty();
                
        // here we bind rectangle size to pane size 
        bndLarguraDaMesa.bind(
                Bindings.min(
                        bndLarguraJanela.multiply(PROPORCAO_MESA_JANELA),
                        bndAlturaJanela.multiply(PROPORCAO_MESA_JANELA/PROPORCAO_ALTURA_LARGURA_MESA)));

        centraliza(root,mesa);

        root.getChildren().add(mesa);

        PedraFx pedra1 = new PedraFx(Pedra.QUINA_SENA);
        colocaPedra(root, mesa, pedra1, 50, 50);
        
        PedraFx pedra2 = new PedraFx(Pedra.CARROCA_DE_PIO);
        colocaPedra(root, mesa, pedra2, 40, 70);

        imprimeDebugInfo(root, mesa, pedra1, pedra2);
        
        Scene scene = new Scene(root, 800, 600);
        setCss(scene,"domino.css");
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        animation(pedra1, pedra2);
        
        
    }

    private void colocaPedra(Pane root, Rectangle mesa, PedraFx pedra, int xPerc, int yPerc) {
        final DoubleBinding bindingUmQuintoLarguraDaMesa = mesa.widthProperty().divide(25);
        pedra.widthProperty().bind(bindingUmQuintoLarguraDaMesa);

        DoubleExpression incrementoX = (mesa.widthProperty().subtract(pedra.widthProperty())).multiply(xPerc/100d);
        DoubleExpression incrementoY = (mesa.heightProperty().subtract(pedra.heightProperty())).multiply(yPerc/100d);
        
        pedra.layoutXProperty().bind(mesa.layoutXProperty().add(incrementoX));
        pedra.layoutYProperty().bind(mesa.layoutYProperty().add(incrementoY));
        
        root.getChildren().add(pedra);
    }

    private void setCss(Scene scene, String resource) {
        final String css = DominoApp.class.getResource(resource).toExternalForm();
        scene.getStylesheets().add(css);
    }

    private void imprimeDebugInfo(Pane root, Rectangle mesa, PedraFx pedra1, PedraFx pedra2) {
        
        Collection<Node> nos = root.getChildren();
        
        double x = 100;
        double y = 80;
        
//        botatexto(nos,x, y+=20,"Mesa W: ",mesa.widthProperty());
//        botatexto(nos,x, y+=20,"Mesa H: ",mesa.heightProperty());
//
//        botatexto(nos,x, y+=25,"Mesa ScaleX: ",mesa.scaleXProperty());
//        botatexto(nos,x, y+=20,"Mesa ScaleY: ",mesa.scaleYProperty());
//
//        botatexto(nos,x, y+=25,"Mesa X: ",mesa.xProperty());
//        botatexto(nos,x, y+=20,"Mesa Y: ",mesa.yProperty());
//
//        botatexto(nos,x, y+=25,"Mesa LayoutX: ",mesa.layoutXProperty());
//        botatexto(nos,x, y+=20,"Mesa LayoutY: ",mesa.layoutYProperty());
//
//        botatexto(nos,x, y+=25,"Mesa TransX: ",mesa.translateXProperty());
//        botatexto(nos,x, y+=20,"Mesa TransY: ",mesa.translateYProperty());

//        botatexto(nos,x,y+=55,"pedra BoundPar: ",pedra1.boundsInParentProperty());
//        botatexto(nos,x,y+=20,"pedra BoundLoc: ",pedra1.boundsInLocalProperty());

//        botatexto(nos,x+=200, y=100,"root W: ",root.widthProperty());
//        botatexto(nos,x,y+=20,"root H: ",root.heightProperty());

//        botatexto(nos,x,y+=25,"root LayoutX: ",root.layoutXProperty());
//        botatexto(nos,x,y+=20,"root LayoutY: ",root.layoutYProperty());
//
//        botatexto(nos,x,y+=25,"root ScaleX: ",root.scaleXProperty());
//        botatexto(nos,x,y+=20,"root ScaleY: ",root.scaleYProperty());
//
//        botatexto(nos,x,y+=25,"root TransX: ",root.translateXProperty());
//        botatexto(nos,x,y+=20,"root TransY: ",root.translateYProperty());

//        botatexto(nos,x+=200,y=100,"pedra W: ",pedra1.widthProperty());
//        botatexto(nos,x,y+=20,"pedra H: ",pedra1.heightProperty());

        botatexto(nos,x,y+=25,"pedra LayoutX: ",pedra1.layoutXProperty());
        botatexto(nos,x,y+=20,"pedra LayoutY: ",pedra1.layoutYProperty());

//        botatexto(nos,x,y+=25,"pedra ScaleX: ",pedra1.scaleXProperty());
//        botatexto(nos,x,y+=20,"pedra ScaleY: ",pedra1.scaleYProperty());

        botatexto(nos,x,y+=25,"pedra TransX: ",pedra1.translateXProperty());
        botatexto(nos,x,y+=20,"pedra TransY: ",pedra1.translateYProperty());
        
//        botatexto(nos,x,y+=20,"pedra BoundParMaxX: ",pedra1.getBoundsInParent().getMaxX());
//        botatexto(nos,x,y+=20,"pedra BoundParMinY: ",pedra1.getBoundsInParent().getMinY());
//        botatexto(nos,x,y+=20,"pedra BoundParMaxY: ",pedra1.getBoundsInParent().getMaxY());
//        
//        botatexto(nos,x,y+=25,"pedra BoundLocMinX: ",pedra1.getBoundsInLocal().getMinX());
//        botatexto(nos,x,y+=20,"pedra BoundLocMaxX: ",pedra1.getBoundsInLocal().getMaxX());
//        botatexto(nos,x,y+=20,"pedra BoundLocMinY: ",pedra1.getBoundsInLocal().getMinY());
//        botatexto(nos,x,y+=20,"pedra BoundLocMaxY: ",pedra1.getBoundsInLocal().getMaxY());
        
//        botatexto(nos,x,y+=55,"pedra BoundPar: ",pedra2.boundsInParentProperty());
//        botatexto(nos,x,y+=20,"pedra BoundLoc: ",pedra2.boundsInLocalProperty());

//        botatexto(nos,x+=200,y=100,"pedra W: ",pedra2.widthProperty());
//        botatexto(nos,x,y+=20,"pedra H: ",pedra2.heightProperty());

        botatexto(nos,x,y+=25,"pedra LayoutX: ",pedra2.layoutXProperty());
        botatexto(nos,x,y+=20,"pedra LayoutY: ",pedra2.layoutYProperty());

        botatexto(nos,x,y+=25,"pedra TransX: ",pedra2.translateXProperty());
        botatexto(nos,x,y+=20,"pedra TransY: ",pedra2.translateYProperty());

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
    
    
    private void animation(PedraFx pedra1, PedraFx pedra2){

        TranslateTransition translation = new TranslateTransition(Duration.millis(6000),pedra1);
        translation.setByX(350);
        
        
        pedra1.layoutXProperty().unbind();
        
        KeyValue kv = new KeyValue(pedra1.layoutXProperty(), pedra2.getLayoutX());
        KeyFrame kf = new KeyFrame(Duration.seconds(4), kv);
        
        Timeline timeline = new Timeline(kf);
        timeline.play();
//        translation.setDelay(Duration.seconds(1));
//        translation.play();

        
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
