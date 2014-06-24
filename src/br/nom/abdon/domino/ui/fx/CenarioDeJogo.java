package br.nom.abdon.domino.ui.fx;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
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

    private static final double PROPORCAO_ALTURA_LARGURA_MESA = 0.7;
    private static final double PROPORCAO_MESA_REGIAO = 0.75;
    private static final double PROPORCAO_MESA_PEDRA = 20;

    
    private final static Random r = new Random();

    private final ObservableDoubleValue bndUmPorCentoLarguraDaMesa;
    private final ObservableDoubleValue bndUmPorCentoAlturaDaMesa;

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

        pedras = PedraFx.produzJogoCompleto(bndLarguraDasPedras);
        
        final SimpleDoubleProperty zero = new SimpleDoubleProperty(0d);
//        pedras.values().parallelStream().forEach((pedraFx) -> {
//            Line linhax = UtilsFx.bindedLine(
//                    pedraFx.xMeioProperty(),
//                    zero,
//                    pedraFx.xMeioProperty(),
//                    mesa.layoutYProperty().add(mesa.heightProperty()));
//            Text tx = UtilsFx.attachText("x", pedraFx.xMeioProperty(),pedraFx.widthProperty(),linhax );
//            
//
//            Line linhay = UtilsFx.bindedLine(
//                    zero, 
//                    pedraFx.yMeioProperty(),
//                    mesa.layoutXProperty().add(mesa.widthProperty()),
//                    pedraFx.yMeioProperty());
//            this.getChildren().add(linhay);
//            
//            
//            this.getChildren().addAll(linhax,tx);
//        });
        
        List<Jogada> jogo = new LinkedList<>();
        jogo.add(new Jogada(Pedra.TERNO_QUADRA));
        jogo.add(new Jogada(Pedra.CARROCA_DE_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_TERNO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.LIMPO_PIO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_PIO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.PIO_DUQUE,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.DUQUE_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.PIO_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.PIO_TERNO,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.DUQUE_TERNO,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.QUADRA_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_DUQUE,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_QUADRA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.DUQUE_QUADRA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.LIMPO_QUINA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.QUINA_SENA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.TERNO_SENA,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.CARROCA_DE_TERNO,Lado.ESQUERDO));
        jogo.add(new Jogada(Pedra.QUADRA_SENA,Lado.DIREITO));
        jogo.add(new Jogada(Pedra.TERNO_QUINA,Lado.ESQUERDO));
        
        final Iterator<Jogada> iterator = jogo.iterator();
        mesa.setOnMouseClicked(
                e -> {
                    Jogada jogada = iterator.next();
                    jogaPedra(jogada.getPedra(), jogada.getLado());
                }
        );
    }
    
    public void adicionaPedras(){

        final ObservableList<Node> children = this.getChildren();

        pedras.values().stream().forEach(
          pedra -> {
              posicionaNaMesa(
                  pedra, 
                  coordenadaAleatoria(), 
                  coordenadaAleatoria(),
                  direcaoAleatoria());
              
              children.add(pedra);
          });
    }
    
    private static double coordenadaAleatoria(){
        return r.nextDouble()*100d;
    }
    
    private static Direcao direcaoAleatoria(){
        int i = r.nextInt(4);
        
        return i == 0 ? Direcao.PRA_BAIXO
             : i == 1 ? Direcao.PRA_CIMA
             : i == 2 ? Direcao.PRA_ESQUERDA
             : Direcao.PRA_DIREITA;
    }
    
    private void posicionaNaMesa(
            final PedraFx pedraFx, 
            final double percentX, 
            final double percentY,
            final Direcao direcao) {
        
        final ObservableDoubleValue xNaMesa = 
                Bindings.multiply(bndUmPorCentoLarguraDaMesa, percentX);

        final ObservableDoubleValue yNaMesa = 
                Bindings.multiply(bndUmPorCentoAlturaDaMesa, percentY);

        final ObservableDoubleValue xNaTela = 
                mesa.layoutXProperty().add(xNaMesa);
        final ObservableDoubleValue yNaTela = 
                mesa.layoutYProperty().add(yNaMesa);

        pedraFx.posiciona(direcao,xNaTela,yNaTela);
    }
    
    public void jogaPedra(Pedra pedra, Lado lado){    
        
        PedraFx pedraFx = pedras.get(pedra);

        if(mesaTaVazia()){
            Chicote[] chicotes = 
                Chicote.inicia(pedraFx, 
                        this.mesa.heightProperty(),
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