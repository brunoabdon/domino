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

    public static EnumMap<Pedra,PedraFx> produzJogoCompleto(
            ObservableDoubleValue widthBinding){
        
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
        linhaDoMeio.endXProperty().bind(this.prototype.tamanhoDaLinha);
        linhaDoMeio.strokeWidthProperty().bind(this.prototype.grossuraDaLinha);
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

        final BiConsumer<ObservableDoubleValue, ObservableDoubleValue> colocaPontinho = 
                (x,y) -> pontinhos.add(prototype.colocaPontinho.apply(x,y));
        
        final ObservableDoubleValue esquerda, meio, direita;
        final ObservableDoubleValue primeiraLinha, linhaDoMeio, linhaDeBaixo;

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
        retangulo.arcHeightProperty().bind(this.prototype.arcoDoRetangulo);
        retangulo.arcWidthProperty().bind(this.prototype.arcoDoRetangulo);
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
                        ? this.prototype.metadeDaLargura.get()
                        : this.prototype.metadeDaAltura.get();
                    
                return coordenada.get() - gap;
            }, coordenada, this.direcao, this.prototype.widthProperty); 
        
    }
    
    static class Prototype {
        
        final ObservableDoubleValue widthProperty;
        final ObservableDoubleValue heightProperty;
        final ObservableDoubleValue quartoQuintosDaLargura;
        final ObservableDoubleValue umVigesimoDaAltura;
        final ObservableDoubleValue nonaParteDaLargura;
        final ObservableDoubleValue umQuintoDaLargura;
        final ObservableDoubleValue metadeDaLargura;
        final ObservableDoubleValue metadeDaAltura;
        final ObservableDoubleValue tamanhoDaLinha;
        final ObservableDoubleValue grossuraDaLinha;
        final ObservableDoubleValue arcoDoRetangulo;
        
        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, Circle> 
            colocaPontinho =
            (x,y) -> {
                    Circle pontinho = new Circle();
                    pontinho.radiusProperty().bind(this.nonaParteDaLargura);
                    pontinho.getStyleClass().add("pontinho");
                    pontinho.layoutXProperty().bind(x);
                    pontinho.layoutYProperty().bind(y);
                    return pontinho;
                };
        
        
        public Prototype(ObservableDoubleValue larguraPedra) {
            
            final DoubleExpression expLargura = 
                DoubleExpression.doubleExpression(larguraPedra);
            
            this.widthProperty = larguraPedra;
            this.heightProperty = expLargura.multiply(2);
            this.quartoQuintosDaLargura = expLargura.multiply(0.8);
            this.umVigesimoDaAltura = expLargura.divide(10);
            this.nonaParteDaLargura =  expLargura.divide(9);
            
            this.umQuintoDaLargura = expLargura.multiply(1d/5d);
            this.metadeDaLargura = expLargura.divide(2);
            this.metadeDaAltura = widthProperty;
            
            this.tamanhoDaLinha = quartoQuintosDaLargura;
            this.grossuraDaLinha = 
                DoubleExpression.doubleExpression(tamanhoDaLinha).divide(20);
            
            this.arcoDoRetangulo = expLargura.divide(4);
        }
    }
}