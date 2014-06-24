package br.nom.abdon.domino.ui.fx;

import java.util.EnumMap;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author Bruno
 */
public class PedraFx extends Group {

    private final Pedra pedra;
    private final ObjectProperty<Direcao> direcao;
    private final DoubleProperty xMeio, yMeio;

    private final DoubleBinding rotationDirecaoBinding;
    
    private final Prototype prototype;
    
    private final static Random randomizer = new Random();

    public static EnumMap<Pedra,PedraFx> produzJogoCompleto(DoubleExpression widthBinding){
        
        final Prototype prototype = new Prototype(widthBinding);
        
        EnumMap<Pedra,PedraFx> mapaPedras = new EnumMap<>(Pedra.class);
        for (Pedra pedra : Pedra.values()) {
            PedraFx pfx = new PedraFx(pedra, prototype);
            mapaPedras.put(pedra,pfx);
        }
        return mapaPedras;
    }
    
    private PedraFx(Pedra pedra, Prototype prototype) {
        super();
        this.pedra = pedra;
        this.direcao = new  SimpleObjectProperty<>(Direcao.PRA_BAIXO);
        
        this.rotationDirecaoBinding = Bindings.createDoubleBinding(
                () -> {
                    return direcao.getValue().getGraus();
                }, this.direcao);

        this.rotateProperty().bind(rotationDirecaoBinding);
        
        this.prototype = prototype;
        
        this.xMeio = new SimpleDoubleProperty(0);
        this.yMeio = new SimpleDoubleProperty(0);
        
        this.layoutXProperty().bind(makeLayoutBinding(xMeio, true));
        this.layoutYProperty().bind(makeLayoutBinding(yMeio, false));
        
        Rectangle retanguloPrincipal = fazRetangulo();
        retanguloPrincipal.widthProperty().bind(prototype.widthProperty);
        Line linhaDoMeio = fazLinhaDoMeio();

        Group pontinhosDeCima = fazPontinhos(pedra.getPrimeiroNumero());
        
        Group pontinhosDeBaixo = fazPontinhos(pedra.getSegundoNumero());
        pontinhosDeBaixo.layoutYProperty().bind(prototype.widthProperty);
        
        super.getChildren().addAll(retanguloPrincipal, 
                linhaDoMeio, 
                pontinhosDeCima, 
                pontinhosDeBaixo);
        
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

    public ObservableDoubleValue widthProperty(){
        return this.prototype.widthProperty;
    }

    public ObservableDoubleValue heightProperty(){
        return this.prototype.heightProperty;
    }
    
    public ObservableDoubleValue xMeioProperty(){
        return xMeio;
    }
    
    public ObservableDoubleValue yMeioProperty(){
        return yMeio;
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
        this.direcao.set(direcao);
    }

    public Pedra getPedra() {
        return pedra;
    }

    public Direcao getDirecao() {
        return this.direcao.getValue();
    }

    public void posiciona(
            Direcao direcao, 
            ObservableDoubleValue xMeio, 
            ObservableDoubleValue yMeio){
        
        final double pixelsPorSegundo = 1800;
        
        final double byX = this.xMeio.subtract(xMeio).multiply(-1).get();
        final double byY = this.yMeio.subtract(yMeio).multiply(-1).get();
        
        final double somaDoQuadradoDosCatetos = Math.pow(byX, 2) + Math.pow(byY, 2);
        final double hipotenusa = Math.sqrt(somaDoQuadradoDosCatetos);
        final Duration tempo = Duration.seconds(hipotenusa / pixelsPorSegundo);
        
        this.rotateProperty().unbind();
        this.xMeio.unbind();
        this.yMeio.unbind();
        
        this.setDirecao(direcao);
        
        KeyValue kvx = new KeyValue(this.xMeio,xMeio.getValue());
        KeyValue kvy = new KeyValue(this.yMeio,yMeio.getValue());
        KeyFrame kf = new KeyFrame(tempo,kvx,kvy);
        Timeline timeline = new Timeline(kf);
        
        RotateTransition rotation = new RotateTransition(tempo);
        double graus = direcao.getGraus();
        if(randomizer.nextBoolean()){
            graus -= 360;
        }
        rotation.setByAngle(graus);
        
        ParallelTransition animation = new ParallelTransition(timeline,rotation);
        animation.setNode(this);
        animation.setOnFinished(
                x -> {
                    this.xMeio.bind(xMeio);
                    this.yMeio.bind(yMeio);
                    this.rotateProperty().bind(rotationDirecaoBinding);
                }
        );
        animation.play();
    }
    
    private DoubleBinding makeLayoutBinding(
            final ObservableDoubleValue coordenada,
            final boolean coordenadaEhHorizontal){
        
        return Bindings.createDoubleBinding(
            () ->  {
                final double gap = 
                    coordenadaEhHorizontal
                        ? this.prototype.metadeDaLargura.getValue()
                        : this.prototype.metadeDaAltura.getValue();
                    
                return coordenada.get() - gap;
            }, coordenada, this.direcao, this.prototype.widthProperty); 
        
    }
    
    static class Prototype {
        
        final DoubleExpression widthProperty;
        final DoubleExpression heightProperty;
        final DoubleExpression quartoQuintosDaLargura;
        final DoubleExpression umVigesimoDaAltura;
        final DoubleExpression nonaParteDaLargura;
        final DoubleExpression umQuintoDaLargura;
        final DoubleExpression metadeDaLargura;
        final DoubleExpression metadeDaAltura;
        
        final BiFunction<DoubleExpression, DoubleExpression, Circle> colocaPontinho =
            (x,y) -> {
                    Circle pontinho = new Circle();
                    pontinho.radiusProperty().bind(this.nonaParteDaLargura);
                    pontinho.getStyleClass().add("pontinho");
                    pontinho.layoutXProperty().bind(x);
                    pontinho.layoutYProperty().bind(y);
                    return pontinho;
                };
        
        
        public Prototype(DoubleExpression widthProperty) {
            this.widthProperty = widthProperty;
            this.heightProperty = widthProperty.multiply(2);
            this.quartoQuintosDaLargura = widthProperty.multiply(0.8);
            this.umVigesimoDaAltura = widthProperty.divide(10);
            this.nonaParteDaLargura =  widthProperty.divide(9);
            
            this.umQuintoDaLargura = this.widthProperty.multiply(1d/5d);
            this.metadeDaLargura = this.widthProperty.divide(2);
            this.metadeDaAltura = widthProperty;
        }
    }
}
    
    
    
    