package br.nom.abdon.domino.ui.fx;

import java.util.function.BiConsumer;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author Bruno
 */
public class PedraFx extends Group {

    private final DoubleProperty widthProperty;
    private final ReadOnlyDoubleProperty heightProperty;
    
    private final Group grupoTudo;
        
    private final Pedra pedra;
    
    public PedraFx(Pedra pedra) {
        super();
        this.pedra = pedra;
        Rectangle retanguloPrincipal = fazRetangulo();
        
        this.widthProperty = retanguloPrincipal.widthProperty();
        this.heightProperty = retanguloPrincipal.heightProperty();

        Line linhaDoMeio = fazLinhaDoMeio();

        Group pontinhosDeCima = fazPontinhos(pedra.getPrimeiroNumero());
        
        Group pontinhosDeBaixo = fazPontinhos(pedra.getSegundoNumero());
        pontinhosDeBaixo.layoutYProperty().bind(this.heightProperty.divide(2));
        
        this.grupoTudo = new Group(
                retanguloPrincipal, 
                linhaDoMeio, 
                pontinhosDeCima, 
                pontinhosDeBaixo);
        
        super.getChildren().add(grupoTudo);
        
        this.setId(pedra.name());        
        this.getStyleClass().add("pedra");
     }


    private Line fazLinhaDoMeio() {
        Line linhaDoMeio = new Line();
        final DoubleProperty propWidthLinha = linhaDoMeio.endXProperty();
        propWidthLinha.bind(this.heightProperty.multiply(0.4));
        linhaDoMeio.strokeWidthProperty().bind(propWidthLinha.divide(20));
        linhaDoMeio.layoutXProperty().bind(this.heightProperty.divide(20));
        linhaDoMeio.layoutYProperty().bind(this.heightProperty.divide(2));
        linhaDoMeio.getStyleClass().add("linhaDePedra");
        return linhaDoMeio;
    }

    public DoubleProperty widthProperty(){
        return this.widthProperty;
    }

    public ReadOnlyDoubleProperty heightProperty(){
        return this.widthProperty;
    }

    private Group fazPontinhos(final Numero numero) {
        
        final Group grupoDePontinhos = new Group();
        final ObservableList<Node> pontinhos = grupoDePontinhos.getChildren();
        final DoubleBinding raio = this.heightProperty.divide(18);

        final BiConsumer<DoubleExpression, DoubleExpression> colocaPontinho = 
            (x,y) -> {
                    Circle pontinho = new Circle();
                    pontinho.radiusProperty().bind(raio);
                    pontinho.getStyleClass().add("pontinho");
                    pontinho.layoutXProperty().bind(x);
                    pontinho.layoutYProperty().bind(y);
                    pontinhos.add(pontinho);
                };
        
        final DoubleBinding esquerda, meio, direita;
        final DoubleBinding primeiraLinha, linhaDoMeio, linhaDeBaixo;

        esquerda = primeiraLinha = this.heightProperty.multiply(1d/2d * 1d/5d);
        meio = linhaDoMeio = this.heightProperty.multiply(1d/2d * 1d/2d); 
        direita = linhaDeBaixo = this.heightProperty.multiply(1d/2d * 4d/5d);

        final int numeroDePontos = numero.getNumeroDePontos();
        
        if((numeroDePontos % 2)!= 0){
            colocaPontinho.accept(meio, linhaDoMeio);
        }
                
        if(numeroDePontos >= 2){
            colocaPontinho.accept(esquerda, primeiraLinha);
            colocaPontinho.accept(direita, linhaDeBaixo);

            if(numeroDePontos >= 4){
                
                colocaPontinho.accept(esquerda,linhaDeBaixo);
                colocaPontinho.accept(direita, primeiraLinha);
                
                if(numeroDePontos == 6){
                    colocaPontinho.accept(esquerda, linhaDoMeio);
                    colocaPontinho.accept(direita, linhaDoMeio);
                }
            }
        }
        return grupoDePontinhos;
    }

    private Rectangle fazRetangulo() {
        final Rectangle retangulo = UtilsFx.retanguloProporcaoFixa(1, 2); 
        final DoubleProperty propAltura = retangulo.heightProperty();
        
        final DoubleProperty propriedadeTamArco = retangulo.arcHeightProperty();
        propriedadeTamArco.bind(propAltura.divide(8));
        retangulo.arcWidthProperty().bind(propriedadeTamArco);
        return retangulo;
    }
    
    public void setDirecao(Direcao d){
        this.grupoTudo.setRotate(d.getGraus());
    }

    public Pedra getPedra() {
        return pedra;
    }

}
