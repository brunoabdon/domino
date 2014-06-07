/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import java.util.Arrays;
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
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;


/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private static final double PROPORCAO_ALTURA_LARGURA_MESA = 1;
    private static final double PROPORCAO_MESA_REGIAO = 0.95;
    private static final double PROPORCAO_MESA_PEDRA = 20;

    private final DoubleBinding bndLarguraDasPedras;
    private final DoubleProperty bndLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoAlturaDaMesa;
    private final DoubleExpression xMeioDaTela;
    private final DoubleExpression yMeioDaTela;

    private final Rectangle mesa;
    
    private final EnumMap<Pedra,PedraFx> pedras;
    private Chicote chicoteEsquerda;
    private Chicote chicoteDireita;
    
    public CenarioDeJogo() {
        this(PROPORCAO_MESA_REGIAO);
    }

    
    public CenarioDeJogo(double proporcao) {
        super();
        
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");
        
        this.bndLarguraDaMesa = mesa.widthProperty();
        
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

        final DoubleExpression metadeDaMesa = bndLarguraDaMesa.divide(2);

        this.xMeioDaTela = mesa.layoutXProperty().add(metadeDaMesa).subtract(bndLarguraDasPedras.divide(2));
        this.yMeioDaTela = mesa.layoutYProperty().add(metadeDaMesa).subtract(bndLarguraDasPedras);
        
        pedras = new EnumMap<>(Pedra.class);
        Arrays.asList(Pedra.values()).forEach((Pedra p) -> {
            PedraFx pfx = new PedraFx(p);
            pedras.put(p,pfx);
            pfx.widthProperty().bind(this.bndLarguraDasPedras);
            super.getChildren().add(pfx);
        });
        
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