package br.nom.abdon.domino.ui.fx;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;


/**
 *
 * @author bruno
 */
public class CenarioDeJogo extends Group{

    private static final double PROPORCAO_ALTURA_LARGURA_MESA = 0.7;
    private static final double PROPORCAO_MESA_REGIAO = 0.75;
    private static final double PROPORCAO_MESA_PEDRA = 16;

    private static final double ESPACO_ENTRE_PREDRAS_NA_MA0 = 1; /* % */
    private static final double ESPACO_ENTRE_PREDRAS_E_PAREDE = 3; /* % */
    
    private static final Random r = new Random();

    private final Rectangle mesa;

    private Chicote chicoteEsquerda;
    private Chicote chicoteDireita;
    
    private final EnumMap<Pedra,PedraFx> pedras;
    
    private final Text labelJogador1, labelJogador2; 
    private final Text labelJogador3, labelJogador4; 
    
    private final ReferenciaDimensoes referenciaDimensoes;

    
    
    public CenarioDeJogo() {
        this(PROPORCAO_MESA_REGIAO);
    }
    
    public CenarioDeJogo(double proporcao) {
        super();
        
        this.mesa = UtilsFx.retanguloProporcaoFixa(PROPORCAO_ALTURA_LARGURA_MESA);
        this.mesa.setId("mesa");

        final DoubleProperty bndLarguraDaMesa = this.mesa.widthProperty();
        
        this.referenciaDimensoes = 
            new ReferenciaDimensoes(
                this.mesa.heightProperty(),
                bndLarguraDaMesa,
                this.mesa.layoutXProperty(),
                this.mesa.layoutYProperty(),
                bndLarguraDaMesa.divide(PROPORCAO_MESA_PEDRA));
        
        
        final double proporcaoAltura = proporcao/PROPORCAO_ALTURA_LARGURA_MESA;
        
        this.parentProperty().addListener((ChangeListener<Parent>)
            (parentProp, oldparent, newParent) -> {
                Region parent = (Region) newParent;
                final ObservableDoubleValue bndLarguraJanela = parent.widthProperty();
                final ObservableDoubleValue bndAlturaJanela = parent.heightProperty();
                
                bndLarguraDaMesa.bind(
                    Bindings.min(
                        Bindings.multiply(bndLarguraJanela,proporcao),
                        Bindings.multiply(bndAlturaJanela, proporcaoAltura)));
                    
                UtilsFx.centraliza(parent,mesa);
            }
        );
        
        super.getChildren().add(mesa);

        pedras = PedraFx.produzJogoCompleto(referenciaDimensoes.larguraPedra);
        
        this.labelJogador1 = makeLabel(50, -10);
        this.labelJogador2 = makeLabel( -10,50);
        this.labelJogador3 = makeLabel(50,110);
        this.labelJogador4 = makeLabel(110,50);
    }
    
    private Text makeLabel(double x, double y){
        Text label = new Text();
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
        final Direcao direcaoPedras = 
            cadeiraDoJogador == 1 || cadeiraDoJogador == 3
            ? Direcao.PRA_BAIXO
            : Direcao.PRA_DIREITA;
        
        final DoubleBinding espacoEntrePedrasNaMao = DoubleExpression.doubleExpression(referenciaDimensoes.larguraPedra).divide(4).multiply(2);
        
        DoubleExpression x = 
            DoubleExpression.doubleExpression(referenciaDimensoes.xMeioDaMesa)
            .subtract(DoubleExpression.doubleExpression(referenciaDimensoes.larguraPedra).multiply(3))
            .subtract(espacoEntrePedrasNaMao);

        final ObservableDoubleValue y = 
            DoubleExpression.doubleExpression(referenciaDimensoes.fimYMesa)
            .subtract(DoubleExpression.doubleExpression(referenciaDimensoes.alturaPedra).multiply(1.1d));
        
        for (Pedra pedra : mao) {
            PedraFx pedraFx = pedras.get(pedra);
            pedraFx.posiciona(direcaoPedras, x, y);
            x = x.add(referenciaDimensoes.larguraPedra)
                .add(espacoEntrePedrasNaMao);
        }
        
    }
    
    
}