/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Pedra;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private final static double PROPORCAO_ALTURA_LARGURA_MESA = 1;
    private final static double PROPORCAO_MESA_REGIAO = 0.95;

    private Rectangle mesa;
    private Map<Pedra,PedraFx> pedras;

    private final DoubleBinding bndLarguraDasPedras;
    private final DoubleBinding bndAlturaDasPedras;

    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoAlturaDaMesa;

    private final GridPane panePedras;
    
    public CenarioDeJogo(Region parent) {
        this(parent,PROPORCAO_MESA_REGIAO);
    }

    
    public CenarioDeJogo(Region parent, double proporcao) {
        super();
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");
        
        final ReadOnlyDoubleProperty bndLarguraJanela = parent.widthProperty();
        final ReadOnlyDoubleProperty bndAlturaJanela = parent.heightProperty();
        final DoubleProperty bndLarguraDaMesa = mesa.widthProperty();
        
        this.bndLarguraDasPedras = bndLarguraDaMesa.divide(20);
        this.bndAlturaDasPedras = this.bndLarguraDasPedras.multiply(2);
        
        this.bndUmPorCentoLarguraDaMesa = bndLarguraDaMesa.divide(100d);
        this.bndUmPorCentoAlturaDaMesa = mesa.heightProperty().divide(100d);     
        
        bndLarguraDaMesa.bind(
                Bindings.min(
                        bndLarguraJanela.multiply(proporcao),
                        bndAlturaJanela.multiply(proporcao/PROPORCAO_ALTURA_LARGURA_MESA)));

        UtilsFx.centraliza(parent,mesa);
        
        super.getChildren().add(mesa);
                
        panePedras = new GridPane();
        panePedras.setLayoutX(100);
        panePedras.setLayoutY(100);

        final DoubleExpression xNaMesa = bndUmPorCentoLarguraDaMesa.multiply(10);
        final DoubleExpression yNaMesa = bndUmPorCentoAlturaDaMesa.multiply(10);
        
        final DoubleBinding xNaTela = mesa.layoutXProperty().add(xNaMesa);
        final DoubleBinding yNaTela = mesa.layoutYProperty().add(yNaMesa);
        
        panePedras.layoutXProperty().bind(xNaTela);
        panePedras.layoutYProperty().bind(yNaTela);

        
        
        super.getChildren().add(panePedras);
        
        
        
//        adicionaPedras();
//        
        PedraFx pedra1 = new PedraFx(Pedra.PIO_SENA);
        pedra1.setDirecao(Direcao.PRA_ESQUERDA);
        
        PedraFx pedra2 = new PedraFx(Pedra.TERNO_SENA);
        pedra2.setDirecao(Direcao.PRA_DIREITA);

        PedraFx pedra3 = new PedraFx(Pedra.CARROCA_DE_TERNO);
        
        pedra1.widthProperty().bind(this.bndLarguraDasPedras);
        pedra2.widthProperty().bind(this.bndLarguraDasPedras);
        pedra3.widthProperty().bind(this.bndLarguraDasPedras);
        
        Group p1 = new Group(pedra1);
        Group p2 = new Group(pedra2);
        Group p3 = new Group(pedra3);
        
        GridPane.setConstraints(p1, 0, 0); 
        GridPane.setConstraints(p2, 1, 0); 
        GridPane.setConstraints(p3, 2, 0); 
        
        this.panePedras.getChildren().add(p1);
        this.panePedras.getChildren().add(p2);
        this.panePedras.getChildren().add(p3);

                
        GridPane paneMao = new GridPane();
        paneMao.setLayoutX(1);
        paneMao.setLayoutY(1);
        
        super.getChildren().add(paneMao);

        DoubleExpression xNaMesa2 = bndUmPorCentoLarguraDaMesa.multiply(40);
        DoubleExpression yNaMesa2 = bndUmPorCentoAlturaDaMesa.multiply(90);
        
        final DoubleBinding xNaTela2 = mesa.layoutXProperty().add(xNaMesa2);
        final DoubleBinding yNaTela2 = mesa.layoutYProperty().add(yNaMesa2);
        
        paneMao.layoutXProperty().bind(xNaTela2);
        paneMao.layoutYProperty().bind(yNaTela2);

        
        final PedraFx pedra4 = new PedraFx(Pedra.TERNO_SENA);
        pedra4.widthProperty().bind(this.bndLarguraDasPedras);
        Group p4 = new Group(pedra4);
        
        GridPane.setConstraints(p4, 0, 0); 
        
        paneMao.getChildren().add(p4);

        DebugInfoChart  debugPanel = new DebugInfoChart();
        debugPanel.setLayoutY(200);
        
//        debugPanel.addDebugInfo("p1.getBoundsInParent()",p1.boundsInParentProperty());
//        debugPanel.addDebugInfo("p2.getBoundsInParent()",p2.boundsInParentProperty());
//        debugPanel.addDebugInfo("p3.getBoundsInParent()",p3.boundsInParentProperty());
//        debugPanel.addDebugInfo("p4.getBoundsInParent()",p4.boundsInParentProperty());
//        debugPanel.addDebugInfo("panePedras lx",panePedras.layoutXProperty());
//        debugPanel.addDebugInfo("panePedras ly",panePedras.layoutYProperty());
//        debugPanel.addDebugInfo("paneMao lx",paneMao.layoutXProperty());
//        debugPanel.addDebugInfo("paneMao ly",paneMao.layoutYProperty());
//        
        super.getChildren().add(debugPanel);

        
        
        
        FadeTransition fout = new FadeTransition(Duration.millis(800), p4);
        fout.setToValue(0);
        fout.setOnFinished(
             e -> {
                    paneMao.getChildren().remove(p4);
                    pedra4.setDirecao(Direcao.PRA_ESQUERDA);
                    GridPane.setConstraints(p4, 3, 0); 
                    panePedras.getChildren().add(p4);
             }
        );
        TranslateTransition transl = new TranslateTransition(Duration.millis(800),p4);
        
        FadeTransition fin = new FadeTransition(Duration.millis(800), p4);
        fin.setToValue(100);
        
        SequentialTransition apagacende = new SequentialTransition(p4,fout,fin);
        
        
        Rectangle coringa = UtilsFx.retanguloProporcaoFixa(2);
        coringa.widthProperty().bind(this.bndLarguraDasPedras);
        GridPane.setConstraints(coringa, 3, 0); 
        panePedras.getChildren().add(coringa);
        
        debugPanel.addDebugInfo("panePedras.layoutXProperty()", panePedras.layoutXProperty());
        debugPanel.addDebugInfo("coringa.layoutBoundsProperty()", coringa.layoutBoundsProperty());
        p4.setOnMouseClicked(
                e -> {
                    
                    transl.byXProperty().bind(panePedras.layoutXProperty().add(coringa.boundsInParentProperty().get().getMinX()).subtract(paneMao.layoutXProperty()));
                    transl.byYProperty().bind(panePedras.layoutYProperty().add(coringa.boundsInParentProperty().get().getMinY()).subtract(paneMao.layoutYProperty()));
//                    transl.setDelay(Duration.millis(400));
                    transl.setOnFinished(
                        xxx -> {
                            panePedras.getChildren().remove(coringa);
                            paneMao.getChildren().remove(p4);
                            p4.setTranslateX(0);
                            p4.setTranslateY(0);
                            GridPane.setConstraints(p4, 3, 0); 
                            panePedras.getChildren().add(p4);
                    });
                    transl.play();
//                  
                    
//                    System.out.println(p4.getBoundsInParent().getMinX());
//                    System.out.println(p4.getBoundsInParent().getMaxX());
//                    paneMao.getChildren().remove(p4);
//                    pedra4.setDirecao(Direcao.PRA_ESQUERDA);
//                    GridPane.setConstraints(p4, 3, 0); 
//                    panePedras.getChildren().add(p4);
//                    System.out.println(p4.getBoundsInParent().getMinX());
//                    System.out.println(p4.getBoundsInParent().getMaxX());
                }
        );
        


        
        
//
//        
//        colocaNaMesa(pedra1);
//        colocaNaMesa(pedra2);
////        colocaNaMesa(pedra3);

        


    }

    public final void adicionaPedras(){

        this.pedras = new HashMap<>();
        
        Random rand = new Random();
        for (Pedra pedra : Pedra.values()) {
            PedraFx pedraFx = new PedraFx(pedra);
            int x = rand.nextInt(80);
            int y = rand.nextInt(80);
            
            int dir = rand.nextInt(4);
            Direcao d = dir == 1? 
                Direcao.PRA_BAIXO
                : dir == 2? 
                    Direcao.PRA_CIMA
                    : dir == 3?
                        Direcao.PRA_DIREITA
                        : Direcao.PRA_ESQUERDA;
            
            pedras.put(pedra, pedraFx);
            this.colocaNaMesa(pedraFx);
//            this.posicionaPedraNaMesa(pedraFx, x , y, d);
        }
        
    }

    private void colocaNaMesa(PedraFx pedraFx){
        pedraFx.widthProperty().bind(this.bndLarguraDasPedras);
        this.panePedras.getChildren().add(pedraFx);
        
//        super.getChildren().add(pedraFx);
    }

    private void posicionaPedraNaMesa(
            final PedraFx pedraFx, 
            final double percentX, 
            final double percentY,
            Direcao d) {
        
        pedraFx.layoutXProperty().unbind();
        pedraFx.layoutYProperty().unbind();
        
        pedraFx.setRotate(d.getGraus());
        
        
        DoubleExpression xNaMesa = bndUmPorCentoLarguraDaMesa.multiply(percentX);
        DoubleExpression yNaMesa = bndUmPorCentoAlturaDaMesa.multiply(percentY);
        
        final DoubleBinding xNaTela = mesa.layoutXProperty().add(xNaMesa);
        final DoubleBinding yNaTela = mesa.layoutYProperty().add(yNaMesa);
        
        pedraFx.layoutXProperty().bind(xNaTela);
        pedraFx.layoutYProperty().bind(yNaTela);
    }
}
