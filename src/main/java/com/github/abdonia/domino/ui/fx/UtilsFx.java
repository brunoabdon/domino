/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.abdonia.domino.ui.fx;


import java.util.Collection;
import java.util.function.BiFunction;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


/**
 *
 * @author bruno
 */
public class UtilsFx {

    public static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        adicionaDistancia = 
            (prop,distancia) -> {
                return DoubleExpression.doubleExpression(prop).add(distancia);
        };
    
    public static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        subtraiDistancia = 
            (prop,distancia) -> {
                return DoubleExpression.doubleExpression(prop).subtract(distancia);
        };
    
    public static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        ignoraDistancia = (prop,distancia) -> {
            return prop;
        };

    public static final Rectangle retanguloProporcaoFixa(double proporcaoAlturaLargura){
        return retanguloProporcaoFixa(1,proporcaoAlturaLargura);
    }

    public static final Rectangle retanguloProporcaoFixa(double largura, double proporcaoAlturaLargura){
        Rectangle rectangle = new Rectangle(largura,largura*proporcaoAlturaLargura);
        rectangle.heightProperty().bind(rectangle.widthProperty().multiply(proporcaoAlturaLargura));
        return rectangle;
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
    

    public static void apontaProAlvo(
            Node node, 
            Rectangle parent,
            Collection<Node> children){

        UtilsFx.apontaProAlvo(
            node,
            parent.layoutXProperty(), 
            parent.layoutYProperty(), 
            parent.widthProperty(),
            parent.heightProperty(), 
            children);
    }

    
    public static void apontaProAlvo(
            Node node, 
            DoubleExpression parentLayoutX,
            DoubleExpression parentLayoutY,
            DoubleExpression parentWidth,
            DoubleExpression parentHeight,
            Collection<Node> children){
        
        Line linhah = bindedLine(
            parentLayoutX,
            node.layoutYProperty(),
            parentLayoutX.add(parentWidth),
            node.layoutYProperty());

        Line linhav = bindedLine(
            node.layoutXProperty(),
            parentLayoutY,
            node.layoutXProperty(),
            parentLayoutY.add(parentHeight));
                
        final DoubleExpression textGap = parentWidth.divide(40);
        
        Text lxmsg = attachText("Layout x",node.layoutXProperty(),textGap,linhav);
        Text lymsg = attachText("Layout y",node.layoutYProperty(),textGap,linhah);
        
        children.add(linhah);
        children.add(linhav);
        children.add(lxmsg);
        children.add(lymsg);        
        
    }

    public static Line bindedLine(
        ObservableDoubleValue startX, ObservableDoubleValue startY, 
        ObservableDoubleValue endX, ObservableDoubleValue endY){
        
        Line linha = new Line(0,0,0,0);
        
        linha.startXProperty().bind(startX);
        linha.endXProperty().bind(endX);
        
        linha.startYProperty().bind(startY);
        linha.endYProperty().bind(endY);
        
        return linha;
        
    }
    
    public static Text attachText(
            String label, 
            DoubleExpression value, 
            DoubleExpression textGap,
            Line linha){
        Text lmsg = new Text();
        lmsg.textProperty().bind(Bindings.concat(label,": ",value));
        lmsg.layoutXProperty().bind(linha.startXProperty().add(textGap));
        lmsg.layoutYProperty().bind(linha.startYProperty().add(textGap));
        return lmsg;

    }
 
    public static <X> void rebind(Property<X> prop, ObservableValue<X> value){
        prop.unbind();
        prop.bind(value);
    }
}
