/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private final static double PROPORCAO_ALTURA_LARGURA_MESA = 1;
    private final static double PROPORCAO_MESA_REGIAO = 0.95;
    public static final int PROPORCAO_MESA_PEDRA = 20;
    
    private Rectangle mesa;
    private EnumMap<Pedra,PedraFx> pedras;

    private final DoubleBinding bndLarguraDasPedras;
    private final DoubleBinding bndAlturaDasPedras;

    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoAlturaDaMesa;

    private PedraFx placeHolder;
    private final GridPane panePedras;

    private final HBox paneMao1, paneMao3;
    private final VBox paneMao2, paneMao4;
    
    private final Pane[] maosPanes;
    
    
    private TelaQuadriculada telaQuadriculada;
    
    public CenarioDeJogo() {
        this(PROPORCAO_MESA_REGIAO);
    }

    
    public CenarioDeJogo(double proporcao) {
        super();
        
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");
        
        final DoubleProperty bndLarguraDaMesa = mesa.widthProperty();
        
        this.bndLarguraDasPedras = bndLarguraDaMesa.divide(PROPORCAO_MESA_PEDRA);
        this.bndAlturaDasPedras = this.bndLarguraDasPedras.multiply(2);
        
        this.bndUmPorCentoLarguraDaMesa = bndLarguraDaMesa.divide(100d);
        this.bndUmPorCentoAlturaDaMesa = mesa.heightProperty().divide(100d);     

        this.parentProperty().addListener((ChangeListener<Parent>)
            (parentProp, oldparent, newParent) -> {
                Region parent = (Region) newParent;
                final ReadOnlyDoubleProperty bndLarguraJanela = parent.widthProperty();
                final ReadOnlyDoubleProperty bndAlturaJanela = parent.heightProperty();

                bndLarguraDaMesa.bind(
                        Bindings.min(
                                bndLarguraJanela.multiply(proporcao),
                                bndAlturaJanela.multiply(proporcao/PROPORCAO_ALTURA_LARGURA_MESA)));

                UtilsFx.centraliza(parent,mesa);
            } 
        );
        
        
        super.getChildren().add(mesa);
        
        
        this.paneMao1 = new HBox();
        this.paneMao2 = new VBox();
        this.paneMao4 = new VBox();
        this.paneMao3 = new HBox();

        final DoubleBinding bndEspacinho = bndLarguraDasPedras.divide(10);
        
        this.paneMao1.spacingProperty().bind(bndEspacinho);
        this.paneMao2.spacingProperty().bind(bndEspacinho);
        this.paneMao3.spacingProperty().bind(bndEspacinho);
        this.paneMao4.spacingProperty().bind(bndEspacinho);
        
        this.maosPanes = new Pane[]{this.paneMao1,this.paneMao2,this.paneMao3,this.paneMao4};
        
        posicionaNaMesa(this.paneMao1,20,80,Direcao.PRA_BAIXO);
        this.getChildren().add(this.paneMao1);
        
        posicionaNaMesa(this.paneMao2,80,20,Direcao.PRA_DIREITA);
        this.getChildren().add(this.paneMao2);
        
        posicionaNaMesa(this.paneMao3,20,20,Direcao.PRA_BAIXO);
        this.getChildren().add(this.paneMao3);
        
        posicionaNaMesa(this.paneMao4,80,80,Direcao.PRA_DIREITA);
        this.getChildren().add(this.paneMao4);
        
        this.telaQuadriculada = new TelaQuadriculada(
                PROPORCAO_MESA_PEDRA/2,
                PROPORCAO_MESA_PEDRA/2);
        
        this.placeHolder = new PedraFx(Pedra.CARROCA_DE_SENA);
        
        
        this.panePedras = new GridPane();

        panePedras.setGridLinesVisible(true);
        
        final DoubleExpression xNaMesa = bndLarguraDaMesa.divide(20);
        final DoubleExpression yNaMesa = bndLarguraDaMesa.divide(20);
        
        final DoubleBinding xNaTela = mesa.layoutXProperty().add(xNaMesa);
        final DoubleBinding yNaTela = mesa.layoutYProperty().add(yNaMesa);
        
        panePedras.layoutXProperty().bind(xNaTela);
        panePedras.layoutYProperty().bind(yNaTela);
        
        super.getChildren().add(panePedras);
        
        adicionaPedras();
        List<Pedra> pedras = Arrays.asList(Pedra.values());
        Collections.shuffle(pedras);
        final Iterator<Pedra> pedrasIterator = pedras.iterator();
        
        Random r  = new Random(2604);
        
        mesa.setOnMouseClicked(
                e -> {
                    jogaPedra(pedrasIterator.next(), r.nextBoolean() ?  Lado.DIREITO : Lado.ESQUERDO );
//                    jogaPedra(pedrasIterator.next(), Lado.DIREITO );
                }
        );
        
    }
    
    private void entregaPedra(int jogador, PedraFx pedra){
        this.maosPanes[jogador].getChildren().add(pedra);
    }
    
    public final void adicionaPedras(){

        this.pedras = new EnumMap(Pedra.class);
        
        List<Pedra> pedras = Arrays.asList(Pedra.values());
        Collections.shuffle(pedras);
        int i = 0;
        int p = 0;
        while (p<=27) {
            final Pedra pedra = pedras.get(p);
            PedraFx pedraFx = new PedraFx(pedra);
            System.out.println("mao " + i + " -> " + pedra);
            maosPanes[i].getChildren().add(pedraFx);
            i=++i%4;
            p++;
            
            this.pedras.put(pedra, pedraFx);
            pedraFx.widthProperty().bind(this.bndLarguraDasPedras);
        }
        
        
//        Random rand = new Random();
//        for (Pedra pedra : Pedra.values()) {
//            PedraFx pedraFx = new PedraFx(pedra);
//            int x = rand.nextInt(80);
//            int y = rand.nextInt(80);
//            
////            int dir = rand.nextInt(4);
////            Direcao d = dir == 1? 
////                Direcao.PRA_BAIXO
////                : dir == 2? 
////                    Direcao.PRA_CIMA
////                    : dir == 3?
////                        Direcao.PRA_DIREITA
////                        : Direcao.PRA_ESQUERDA;
//            
//            pedras.put(pedra, pedraFx);
//            this.colocaNaMesa(pedraFx);
//            this.posicionaPedraNaMesa(pedraFx, x , y, Direcao.PRA_BAIXO);
//        }
        
    }

    public void jogaPedra(Pedra pedra, Lado lado){
        Vaga vaga = telaQuadriculada.getVaga(lado);
        System.out.println(vaga);
        coloca(this.placeHolder,vaga);
        
        PedraFx pedraFx = pedras.get(pedra);
                
        TranslateTransition transl = new TranslateTransition(Duration.millis(600));
        transl.setOnFinished(
                (ActionEvent e) -> {
                        final TranslateTransition t = (TranslateTransition)e.getSource();
                        Node node = t.getNode();
                    
                        panePedras.getChildren().remove(placeHolder);
                        this.getChildren().remove(node);
                        node.setTranslateX(0);
                        node.setTranslateY(0);
                        coloca(node, vaga);
                }
        );
        
        if(!pedra.isCarroca()){
            pedraFx.setDirecao(vaga.getDirecao().inverver());
        }
        
        UtilsFx.fillTranslation(pedraFx, placeHolder, transl);
        transl.play();
        
        
    }

    private void coloca(Node node, Vaga vaga) {
        node.layoutXProperty().unbind();
        node.layoutYProperty().unbind();
        GridPane.setConstraints(node, vaga.getX(), vaga.getY()); 
        panePedras.getChildren().add(node);
    }

    private void posicionaNaMesa(
            final Node node, 
            final double percentX, 
            final double percentY,
            Direcao d) {
        
        node.layoutXProperty().unbind();
        node.layoutYProperty().unbind();
        
        node.setRotate(d.getGraus());
        
        DoubleExpression xNaMesa = bndUmPorCentoLarguraDaMesa.multiply(percentX);
        DoubleExpression yNaMesa = bndUmPorCentoAlturaDaMesa.multiply(percentY);
        
        final DoubleBinding xNaTela = mesa.layoutXProperty().add(xNaMesa);
        final DoubleBinding yNaTela = mesa.layoutYProperty().add(yNaMesa);
        
        node.layoutXProperty().bind(xNaTela);
        node.layoutYProperty().bind(yNaTela);
    }

}
