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
import java.util.LinkedList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Jogada;
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

    private final DoubleProperty bndLarguraDaMesa;
    
    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoAlturaDaMesa;

    private final DoubleExpression xMeioDaTela;
    private final DoubleExpression yMeioDaTela;

    
    private final HBox paneMao1, paneMao3;
    private final VBox paneMao2, paneMao4;
    
    private final Pane[] maosPanes;
    
    private Chicote chicoteEsquerda;
    private Chicote chicoteDireita;
    
    public CenarioDeJogo() {
        this(PROPORCAO_MESA_REGIAO);
    }

    
    public CenarioDeJogo(double proporcao) {
        super();
        
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");
        
        bndLarguraDaMesa = mesa.widthProperty();
        
        this.bndLarguraDasPedras = bndLarguraDaMesa.divide(PROPORCAO_MESA_PEDRA);
        
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
        
        final DoubleExpression metadeDaMesa = bndLarguraDaMesa.divide(2);

        this.xMeioDaTela = mesa.layoutXProperty().add(metadeDaMesa);
        this.yMeioDaTela = mesa.layoutYProperty().add(metadeDaMesa);
        
        pedras = new EnumMap<>(Pedra.class);
        Arrays.asList(Pedra.values()).forEach((Pedra p) -> {
            PedraFx pfx = new PedraFx(p);
            pedras.put(p,pfx);
            pfx.widthProperty().bind(this.bndLarguraDasPedras);
            super.getChildren().add(pfx);
        });
        
//        Random r = new Random();
        //coloca uma pedra no meio
//        PedraFx pedra1 = new PedraFx(r.nextBoolean() ? Pedra.CARROCA_DE_PIO : Pedra.CARROCA_DE_DUQUE);
//        PedraFx pedra1 = pedras.get(Pedra.DUQUE_QUINA);
//        pedra1.posiciona(Direcao.PRA_BAIXO, xMeioDaTela, yMeioDaTela);
//        
//        //coloca uma numa ponta
//        PedraFx pedra2 = pedras.get(Pedra.QUINA_SENA);
//        posicionaNaMesa(pedra2, 90, 50, Direcao.PRA_BAIXO);
        
        
        List<Jogada> jogo = new LinkedList<>();
        jogo.add(new Jogada(Pedra.CARROCA_DE_DUQUE));           //meio
        jogo.add(new Jogada(Pedra.DUQUE_QUINA,Lado.DIREITO));   //normal, direito, nao inverte
        jogo.add(new Jogada(Pedra.QUADRA_QUINA,Lado.DIREITO));   //normal, direito, inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_QUADRA,Lado.DIREITO)); //carroca,  direito
        jogo.add(new Jogada(Pedra.QUADRA_SENA,Lado.DIREITO)); //na carroca, direito, inverte

        jogo.add(new Jogada(Pedra.DUQUE_TERNO,Lado.ESQUERDO));   //normal, esquero, nao inverte
        jogo.add(new Jogada(Pedra.PIO_TERNO,Lado.ESQUERDO));   //normal, esquerdo, inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_PIO,Lado.ESQUERDO)); //carroca,  esquerdo
        jogo.add(new Jogada(Pedra.PIO_SENA,Lado.ESQUERDO)); //na carroca, esquerdo, inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_SENA,Lado.ESQUERDO)); //carroca, esquerdo (repetido)
        jogo.add(new Jogada(Pedra.LIMPO_SENA,Lado.ESQUERDO)); //na carroca, esquerdo, invente

        final Iterator<Jogada> iterator = jogo.iterator();
        
        
        mesa.setOnMouseClicked(
                e -> {
                    Jogada jogada = iterator.next();
                    jogaPedra(jogada.getPedra(), jogada.getLado());
                }
        );
        
        
//        Text msg = new Text();
//        msg.textProperty().bind(Bindings.concat("Rotate: ",pedra2.rotateProperty()));
//        posicionaNaMesa(msg, 10, 30, Direcao.PRA_BAIXO);
//        this.getChildren().add(msg);        
//        Text msg2 = new Text();
//        msg2.textProperty().bind(Bindings.concat("InnerRotate: ",pedra2.innerRotateProperty()));
//        posicionaNaMesa(msg2, 10, 33, Direcao.PRA_BAIXO);
//        this.getChildren().add(msg2);        

//        UtilsFx.apontaProAlvo(pedra1,mesa,this.getChildren());
//        UtilsFx.apontaProAlvo(pedra2,mesa,this.getChildren());

    }
    
    private void entregaPedra(int jogador, PedraFx pedra){
        this.maosPanes[jogador].getChildren().add(pedra);
    }
    
    private final void adicionaPedras(){

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
    
    
    public void jogaPedra(Pedra pedra, Lado lado){    
        
        PedraFx pedraFx = pedras.get(pedra);

        if(mesaTaVazia()){

            Chicote[] chicotes = 
                Chicote.inicia(pedraFx, xMeioDaTela, yMeioDaTela);

            this.chicoteEsquerda = chicotes[0];
            this.chicoteDireita = chicotes[1];
            
        } else {
            
            Chicote chicote = 
                lado == Lado.ESQUERDO ? chicoteEsquerda : chicoteDireita;
            
            chicote.encaixa(pedraFx);
        }
    }

    private boolean mesaTaVazia() {
        return this.chicoteEsquerda == null;
    }

   
    
}