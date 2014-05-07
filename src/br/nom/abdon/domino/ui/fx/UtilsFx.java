/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;


import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
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

        DoubleExpression incrementoX = (referencia.widthProperty().subtract(nodeWidhtProp)).multiply(xPercent/100d);
        DoubleExpression incrementoY = (referencia.heightProperty().subtract(nodeHeightProp)).multiply(yPercent/100d);
        
        node.layoutXProperty().bind(referencia.layoutXProperty().add(incrementoX));
        node.layoutYProperty().bind(referencia.layoutYProperty().add(incrementoY));

    }
}
