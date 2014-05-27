/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;


import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;


/**
 *
 * @author bruno
 */
public class UtilsFx {

    public static final Rectangle retanguloProporcaoFixa(double proporcaoAlturaLargura){
        return retanguloProporcaoFixa(1,proporcaoAlturaLargura);
    }

    public static final Rectangle retanguloProporcaoFixa(double largura, double proporcaoAlturaLargura){
        Rectangle rectangle = new Rectangle(largura,largura*proporcaoAlturaLargura);
        rectangle.heightProperty().bind(rectangle.widthProperty().multiply(proporcaoAlturaLargura));
        return rectangle;
        
    }
    
    public static final void posiciona(
            Rectangle mesa, 
            PedraFx pedra, 
            double xPercent, double yPercent){
        
        posiciona(mesa, pedra, pedra.widthProperty(), pedra.heightProperty(), xPercent,yPercent);
    }

    
    
    public static final void posiciona(
            Rectangle referencia, 
            Node node, 
            ReadOnlyDoubleProperty nodeWidhtProp,
            ReadOnlyDoubleProperty nodeHeightProp,
            double xPercent, double yPercent){


    }
    
    public static void centraliza(Region externo, Rectangle interno) {
        centraliza(
                externo.widthProperty(),
                externo.heightProperty(),
                interno.widthProperty(),
                interno.heightProperty(),
                interno.layoutXProperty(),
                interno.layoutYProperty()
        );
    }

    
    public static void centraliza(Region externo, Region interno) {
        centraliza(
                externo.widthProperty(),
                externo.heightProperty(),
                interno.widthProperty(),
                interno.heightProperty(),
                interno.layoutXProperty(),
                interno.layoutYProperty()
        );
    }
    
    public static void centraliza(
            DoubleExpression expWidthExt, DoubleExpression expHeightExt, 
            DoubleExpression expWidthInt, DoubleExpression expHeightInt,
            DoubleProperty bndLayoutXInt, DoubleProperty bndLayoutYInt){
        bndLayoutXInt.bind(metade(expWidthExt).subtract(metade(expWidthInt)));
        bndLayoutYInt.bind(metade(expHeightExt).subtract(metade(expHeightInt)));
    }
    
    
    private static DoubleExpression metade(DoubleExpression exp){
        return exp.divide(2);
    }


    public static void fillTranslation(Node node, Node destLocation, TranslateTransition translation){

        final DoubleBinding xAbsDest = destLocation.layoutXProperty().add(destLocation.getParent().layoutXProperty());
        final DoubleBinding yAbsDest = destLocation.layoutYProperty().add(destLocation.getParent().layoutYProperty());

        final DoubleBinding xAbsIni = node.layoutXProperty().add(node.getParent().layoutXProperty());
        final DoubleBinding yAbsIni = node.layoutYProperty().add(node.getParent().layoutYProperty());

        final DoubleBinding xTrans = xAbsDest.subtract(xAbsIni);
        final DoubleBinding yTrans = yAbsDest.subtract(yAbsIni);

        translation.setNode(node);
        translation.toXProperty().bind(xTrans);
        translation.toYProperty().bind(yTrans);
    }
    
    
}
