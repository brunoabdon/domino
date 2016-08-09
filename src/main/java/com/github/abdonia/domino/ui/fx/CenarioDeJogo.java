package com.github.abdonia.domino.ui.fx;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Random;
import java.util.function.Function;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import static com.github.abdonia.domino.ui.fx.UtilsFx.adicionaDistancia;


/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private static final double PROPORCAO_ALTURA_LARGURA_MESA = 0.7;
    private static final double PROPORCAO_MESA_REGIAO = 0.75;

    private static final Random r = new Random();

    private final Rectangle mesa;

    private Chicote chicoteEsquerda;
    private Chicote chicoteDireita;
    
    private final EnumMap<Pedra,PedraFx> pedras;
    
    private final Label labelJogador1, labelJogador2; 
    private final Label labelJogador3, labelJogador4; 
    
    private final ReferenciaDimensoes referenciaDimensoes;

    private final Function<ObservableDoubleValue,ObservableDoubleValue>
        adicionaEspacinho;
            
    private final Function<ObservableDoubleValue,ObservableDoubleValue>
        ignoraEspacinho = (val) -> { return val; };
            
    public CenarioDeJogo() {
        this(PROPORCAO_MESA_REGIAO);
    }
    
    public CenarioDeJogo(double proporcao) {
        super();
        
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");

        this.referenciaDimensoes = 
            new ReferenciaDimensoes(
                this.mesa.heightProperty(),
                this.mesa.widthProperty(),
                this.mesa.layoutXProperty(),
                this.mesa.layoutYProperty());
        
        this.adicionaEspacinho = 
            (val) -> { 
                return adicionaDistancia
                    .apply(val,referenciaDimensoes.distanciaEntrePedrasNaMao);
        };

        final double proporcaoAltura = proporcao/PROPORCAO_ALTURA_LARGURA_MESA;
        
        this.parentProperty().addListener((ChangeListener<Parent>)
            (parentProp, oldparent, newParent) -> {
                Region parent = (Region) newParent;
                final ObservableDoubleValue bndLarguraJanela = parent.widthProperty();
                final ObservableDoubleValue bndAlturaJanela = parent.heightProperty();
                
                mesa.widthProperty().bind(
                    Bindings.min(
                        Bindings.multiply(bndLarguraJanela,proporcao),
                        Bindings.multiply(bndAlturaJanela, proporcaoAltura)));
                    
                UtilsFx.centraliza(parent,mesa);
            }
        );
        
        super.getChildren().add(mesa);

        pedras = PedraFx.produzJogoCompleto(referenciaDimensoes.larguraPedra);
        
        this.labelJogador1 = makeLabel(referenciaDimensoes.xMeioDaMesa, referenciaDimensoes.yMaoBaixo);
        this.labelJogador2 = makeLabel(referenciaDimensoes.xMaoEsq,referenciaDimensoes.yInicialMaoPar);
        this.labelJogador3 = makeLabel(referenciaDimensoes.xMeioDaMesa,referenciaDimensoes.yMaoCima);
        this.labelJogador4 = makeLabel(referenciaDimensoes.xMaoDir,referenciaDimensoes.yInicialMaoPar);
    } 
   
    private Label makeLabel(ObservableDoubleValue x, ObservableDoubleValue y){
        Label label = new Label();
        label.setFont(Font.font("Sans-Serif", 10));

        posicionaNaMesa(label, x, y);
        this.getChildren().add(label);

        return label;
    }
    
    public void adicionaPedras(){
        
        final ObservableList<Node> children = this.getChildren();
        pedras.values().stream().forEach(
            pedra -> {
                posicionaNaMesa(pedra, 
                    coordenadaAleatoria(), 
                    coordenadaAleatoria(),
                    direcaoAleatoria());
                children.add(pedra);
            }
        );
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
            final Node node, 
            final double percentX, 
            final double percentY) {
        
        final ObservableDoubleValue xNaTela = xAbs(percentX);
        final ObservableDoubleValue yNaTela = yAbs(percentY);
        
        UtilsFx.rebind(node.layoutXProperty(), xNaTela);
        UtilsFx.rebind(node.layoutYProperty(), yNaTela);

    }

    private void posicionaNaMesa(
            final Label label, 
            final double percentX, 
            final double percentY) {
        
        final ObservableDoubleValue xNaTela = xAbs(percentX);
        final ObservableDoubleValue yNaTela = yAbs(percentY);

        posicionaNaMesa(label, xNaTela, yNaTela);
        
    }

    private void posicionaNaMesa(
            final Label label, 
            final ObservableDoubleValue xNaTela, 
            final ObservableDoubleValue yNaTela) {

        ObservableDoubleValue x = label.widthProperty().divide(-2).add(xNaTela);
        ObservableDoubleValue y = label.heightProperty().divide(-2).add(yNaTela);

        UtilsFx.rebind(label.layoutXProperty(), x);
        UtilsFx.rebind(label.layoutYProperty(), y);
    }
    
    private void posicionaNaMesa(
            final PedraFx pedraFx, 
            final double percentX, 
            final double percentY,
            final Direcao direcao) {
        
        final ObservableDoubleValue xNaTela = xAbs(percentX);
        final ObservableDoubleValue yNaTela = yAbs(percentY);

        pedraFx.posiciona(direcao,xNaTela,yNaTela);
    }
    
    private ObservableDoubleValue xAbs(double percent){
        return abs(mesa.layoutXProperty(),
                referenciaDimensoes.umPorCentoDaLarguraDaMesa,
                percent);
    }
    private ObservableDoubleValue yAbs(double percent){
        return abs(mesa.layoutYProperty(),
                referenciaDimensoes.umPorCentoDaAlturaDaMesa,
                percent);
    }
    
    private ObservableDoubleValue abs(
        ObservableDoubleValue referenceOffset,
        ObservableDoubleValue referencePerc,
        double percent){

        return
            DoubleExpression.doubleExpression(referencePerc)
                .multiply(percent)
                .add(referenceOffset);
    }

    
    public void jogaPedra(Pedra pedra, Lado lado){    
        
        PedraFx pedraFx = pedras.get(pedra);
        if(mesaTaVazia()){
            Chicote[] chicotes = Chicote.inicia(pedraFx, referenciaDimensoes);
                        
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
    
    public void sentaJogadores(
            String nomeJogador1,String nomeJogador2, 
            String nomeJogador3, String nomeJogador4){
        
        labelJogador1.setText(nomeJogador1);
        labelJogador2.setText(nomeJogador2);
        labelJogador3.setText(nomeJogador3);
        labelJogador4.setText(nomeJogador4);
    }
    
    public void entregaPedras(int cadeiraDoJogador, Collection<Pedra> mao){
        
        boolean ehPar = cadeiraDoJogador % 2 == 0;
        
        final Direcao direcaoPedras; 
        ObservableDoubleValue x, y;
        
        final Function<ObservableDoubleValue, ObservableDoubleValue> opX, opY;
        
        if(ehPar){
            y = referenciaDimensoes.yInicialMaoPar;
            x = cadeiraDoJogador == 2 
                ? referenciaDimensoes.xMaoDir 
                : referenciaDimensoes.xMaoEsq;
            
            opX = ignoraEspacinho;
            opY = adicionaEspacinho;
            
            direcaoPedras = Direcao.PRA_DIREITA;
        } else {
            x = referenciaDimensoes.xInicialMaoImpar;
            y = cadeiraDoJogador == 1 
                ? referenciaDimensoes.yMaoBaixo 
                : referenciaDimensoes.yMaoCima;

            opX = adicionaEspacinho;
            opY = ignoraEspacinho;
            
            direcaoPedras = Direcao.PRA_BAIXO;
        }
        
        for (Pedra pedra : mao) {
            final PedraFx pedraFx = pedras.get(pedra);
            pedraFx.posiciona(direcaoPedras, x, y);
            x = opX.apply(x);
            y = opY.apply(y);
        }
    }
    
    public void arrumaODorme(final Collection<Pedra> pedras){
        
    }
    
}