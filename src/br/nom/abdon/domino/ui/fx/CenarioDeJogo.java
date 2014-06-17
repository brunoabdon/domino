package br.nom.abdon.domino.ui.fx;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

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

//    private final DoubleBinding bndUmPorCentoLarguraDaMesa;
//    private final DoubleBinding bndUmPorCentoAlturaDaMesa;

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
        
        final DoubleProperty bndLarguraDaMesa = mesa.widthProperty();
        
        final DoubleExpression bndLarguraDasPedras = bndLarguraDaMesa.divide(PROPORCAO_MESA_PEDRA);
        
//        this.bndUmPorCentoLarguraDaMesa = bndLarguraDaMesa.divide(100d);
//        this.bndUmPorCentoAlturaDaMesa = mesa.heightProperty().divide(100d);     

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

        pedras = PedraFx.produzJogoCompleto(bndLarguraDasPedras);
        this.getChildren().addAll(pedras.values());
        
        List<Jogada> jogo = new LinkedList<>();
        jogo.add(new Jogada(Pedra.CARROCA_DE_DUQUE));           //meio
        jogo.add(new Jogada(Pedra.DUQUE_TERNO,Lado.ESQUERDO));   //normal, esquero, nao inverte
        jogo.add(new Jogada(Pedra.PIO_TERNO,Lado.ESQUERDO));   //normal, esquerdo, inverte
        jogo.add(new Jogada(Pedra.DUQUE_QUINA,Lado.DIREITO));   //normal, direito, nao inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_PIO,Lado.ESQUERDO)); //carroca,  esquerdo
        jogo.add(new Jogada(Pedra.QUADRA_QUINA,Lado.DIREITO));   //normal, direito, inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_QUADRA,Lado.DIREITO)); //carroca,  direito
        jogo.add(new Jogada(Pedra.PIO_SENA,Lado.ESQUERDO)); //na carroca, esquerdo, inverte
        jogo.add(new Jogada(Pedra.CARROCA_DE_SENA,Lado.ESQUERDO)); //carroca, esquerdo (repetido)
        jogo.add(new Jogada(Pedra.LIMPO_SENA,Lado.ESQUERDO)); //na carroca, esquerdo, invente
        jogo.add(new Jogada(Pedra.QUADRA_SENA,Lado.DIREITO)); //na carroca, direito, inverte
        jogo.add(new Jogada(Pedra.QUINA_SENA,Lado.DIREITO)); //na carroca, direito, inverte
        jogo.add(new Jogada(Pedra.TERNO_QUINA,Lado.DIREITO)); //na carroca, direito, inverte
        jogo.add(new Jogada(Pedra.LIMPO_TERNO,Lado.DIREITO)); //na carroca, direito, inverte


        final Iterator<Jogada> iterator = jogo.iterator();
        
        
        mesa.setOnMouseClicked(
                e -> {
                    Jogada jogada = iterator.next();
                    jogaPedra(jogada.getPedra(), jogada.getLado());
                }
        );
    }
    
//    private void posicionaNaMesa(
//            final Node node, 
//            final double percentX, 
//            final double percentY,
//            Direcao d) {
//        
//        node.layoutXProperty().unbind();
//        node.layoutYProperty().unbind();
//        
//        node.setRotate(d.getGraus());
//        
//        DoubleExpression xNaMesa = bndUmPorCentoLarguraDaMesa.multiply(percentX);
//        DoubleExpression yNaMesa = bndUmPorCentoAlturaDaMesa.multiply(percentY);
//        
//        final DoubleBinding xNaTela = mesa.layoutXProperty().add(xNaMesa);
//        final DoubleBinding yNaTela = mesa.layoutYProperty().add(yNaMesa);
//        
//        node.layoutXProperty().bind(xNaTela);
//        node.layoutYProperty().bind(yNaTela);
//    }
    
    
    public void jogaPedra(Pedra pedra, Lado lado){    
        
        PedraFx pedraFx = pedras.get(pedra);

        if(mesaTaVazia()){

            Chicote[] chicotes = 
                Chicote.inicia(pedraFx, 
                        this.mesa.widthProperty(), 
                        this.mesa.layoutXProperty(),
                        this.mesa.layoutYProperty());
                        

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