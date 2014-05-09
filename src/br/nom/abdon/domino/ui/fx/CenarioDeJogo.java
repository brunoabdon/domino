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
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Pedra;

/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private final static double PROPORCAO_ALTURA_LARGURA_MESA = 0.6;
    private final static double PROPORCAO_MESA_REGIAO = 0.95;

    private Rectangle mesa;
    private Map<Pedra,PedraFx> pedras;

    private final DoubleBinding bndLarguraDasPedras;
    private final DoubleBinding bndAlturaDasPedras;

    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
    private final DoubleBinding bndUmPorCentoAlturaDaMesa;

    
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
        
        this.bndLarguraDasPedras = bndLarguraDaMesa.divide(25);
        this.bndAlturaDasPedras = this.bndLarguraDasPedras.multiply(2);
        
        this.bndUmPorCentoLarguraDaMesa = bndLarguraDaMesa.divide(100d);
        this.bndUmPorCentoAlturaDaMesa = mesa.heightProperty().divide(100d);     
        
        bndLarguraDaMesa.bind(
                Bindings.min(
                        bndLarguraJanela.multiply(proporcao),
                        bndAlturaJanela.multiply(proporcao/PROPORCAO_ALTURA_LARGURA_MESA)));

        UtilsFx.centraliza(parent,mesa);
        
        super.getChildren().add(mesa);
        adicionaPedras();

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
            this.posicionaPedraNaMesa(pedraFx, x , y, d);
        }
        
    }

    private void colocaNaMesa(PedraFx pedraFx){
        pedraFx.widthProperty().bind(this.bndLarguraDasPedras);
        super.getChildren().add(pedraFx);
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
