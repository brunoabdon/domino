package br.nom.abdon.domino.ui.fx;

import java.util.function.BiConsumer;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import java.util.EnumMap;
import java.util.Random;
import java.util.function.BiFunction;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 *
 * @author Bruno
 */
public class PedraFx extends Group {

//    private final DoubleProperty widthProperty;
//    private final ReadOnlyDoubleProperty heightProperty;
    
    private final Pedra pedra;
    private Direcao direcao;

    private final Prototype prototype;
    
    private final static Random randomizer = new Random();

    public static EnumMap<Pedra,PedraFx> produzJogoCompleto(DoubleBinding widthBinding){
        
        EnumMap<Pedra,PedraFx> mapaPedras = new EnumMap<>(Pedra.class);
        for (Pedra pedra : Pedra.values()) {
            PedraFx pfx = new PedraFx(pedra,new Prototype(widthBinding));
            mapaPedras.put(pedra,pfx);
        }
        return mapaPedras;
    }
    
    private PedraFx(Pedra pedra, Prototype prototype) {
        super();
        this.pedra = pedra;
        this.direcao = Direcao.PRA_BAIXO;
        
        this.prototype = prototype;
        
        Rectangle retanguloPrincipal = fazRetangulo();
        retanguloPrincipal.widthProperty().bind(prototype.widthProperty);
        Line linhaDoMeio = fazLinhaDoMeio();

        Group pontinhosDeCima = fazPontinhos(pedra.getPrimeiroNumero());
        
        Group pontinhosDeBaixo = fazPontinhos(pedra.getSegundoNumero());
        pontinhosDeBaixo.layoutYProperty().bind(prototype.widthProperty);
        
        super.getChildren().addAll(retanguloPrincipal, 
                linhaDoMeio, 
                pontinhosDeCima, 
                pontinhosDeBaixo)
        ;
        
        this.setId(pedra.name());        
        this.getStyleClass().add("pedra");
     }


    private Line fazLinhaDoMeio() {
        Line linhaDoMeio = new Line();
        final DoubleProperty propWidthLinha = linhaDoMeio.endXProperty();
        propWidthLinha.bind(this.prototype.quartoQuintosDaLargura);
        linhaDoMeio.strokeWidthProperty().bind(propWidthLinha.divide(20));
        linhaDoMeio.layoutXProperty().bind(this.prototype.umVigesimoDaAltura);
        linhaDoMeio.layoutYProperty().bind(this.prototype.widthProperty);
        linhaDoMeio.getStyleClass().add("linhaDePedra");
        return linhaDoMeio;
    }

    public DoubleExpression widthProperty(){
        return this.prototype.widthProperty;
    }

    public DoubleExpression heightExpression(){
        return this.prototype.heightExpression;
    }

    private Group fazPontinhos(final Numero numero) {
        
        final Group grupoDePontinhos = new Group();
        final ObservableList<Node> pontinhos = grupoDePontinhos.getChildren();

        final BiConsumer<DoubleExpression, DoubleExpression> colocaPontinho = 
                (x,y) -> pontinhos.add(prototype.colocaPontinho.apply(x,y));
        
        final DoubleExpression esquerda, meio, direita;
        final DoubleExpression primeiraLinha, linhaDoMeio, linhaDeBaixo;

        esquerda = primeiraLinha = this.prototype.umQuintoDaLargura;
        meio = linhaDoMeio = this.prototype.metadeDaLargura; 
        direita = linhaDeBaixo = this.prototype.quartoQuintosDaLargura;

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
    
    public void setDirecao(Direcao direcao){
        this.direcao = direcao;
        this.setRotate(direcao.getGraus()); //criar um bind depois...
    }

    public Pedra getPedra() {
        return pedra;
    }

    public Direcao getDirecao() {
        return this.direcao;
    }

    public void posiciona(
            Direcao direcao, 
            ObservableDoubleValue layoutX, 
            ObservableDoubleValue layoutY){
        
        final double pixelsPorSegundo = 400;
        
        
        final double byX = this.layoutXProperty().subtract(layoutX).multiply(-1).get();
        final double byY = this.layoutYProperty().subtract(layoutY).multiply(-1).get();
        
        final double somaDoQuadradoDosCatetos = Math.pow(byX, 2) + Math.pow(byY, 2);
        final double hipotenusa = Math.sqrt(somaDoQuadradoDosCatetos);
        final Duration tempo = Duration.seconds(pixelsPorSegundo / hipotenusa);

        TranslateTransition translation = new TranslateTransition(tempo);
        translation.setByX(byX);
        translation.setToY(byY);
        
        RotateTransition rotation = new RotateTransition(tempo);
        double graus = direcao.getGraus();
        if(randomizer.nextBoolean()){
            graus -= 360;
        }
        rotation.setByAngle(graus);
        
        ParallelTransition animation = new ParallelTransition(translation,rotation);
        animation.setNode(this);
        animation.setOnFinished(
                x -> {
                    this.setTranslateX(0);
                    this.setTranslateY(0);
                    this.setDirecao(direcao);
                    this.layoutXProperty().bind(layoutX);
                    this.layoutYProperty().bind(layoutY);
                }
        );
        animation.play();
    }
    
    
    static class Prototype {
        
        
        final DoubleBinding widthProperty;
        final DoubleExpression heightExpression;
        final DoubleExpression quartoQuintosDaLargura;
        final DoubleExpression umVigesimoDaAltura;
        final DoubleExpression nonaParteDaLargura;
        final DoubleExpression umQuintoDaLargura;
        final DoubleExpression metadeDaLargura;
        
        final BiFunction<DoubleExpression, DoubleExpression, Circle> colocaPontinho =
            (x,y) -> {
                    Circle pontinho = new Circle();
                    pontinho.radiusProperty().bind(this.nonaParteDaLargura);
                    pontinho.getStyleClass().add("pontinho");
                    pontinho.layoutXProperty().bind(x);
                    pontinho.layoutYProperty().bind(y);
                    return pontinho;
                };
        
        
        public Prototype(DoubleBinding widthProperty) {
            this.widthProperty = widthProperty;
            this.heightExpression = widthProperty.multiply(2);
            this.quartoQuintosDaLargura = widthProperty.multiply(0.8);
            this.umVigesimoDaAltura = widthProperty.divide(10);
            this.nonaParteDaLargura =  widthProperty.divide(9);
            
            this.umQuintoDaLargura = this.widthProperty.multiply(1d/5d);
            this.metadeDaLargura = this.widthProperty.divide(2);
        }
    }
}
    
    
    
    