package br.nom.abdon.domino.ui.fx;

import java.util.function.BiConsumer;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import java.util.Random;

/**
 *
 * @author Bruno
 */
public class PedraFx extends Group {

    private final DoubleProperty widthProperty;
    private final ReadOnlyDoubleProperty heightProperty;
    
    private final Group grupoTudo;
        
    private final Pedra pedra;
    private Direcao direcao;
    private Direcao direcaoFileira;
    
    public PedraFx(Pedra pedra) {
        super();
        this.pedra = pedra;
        this.direcao = Direcao.PRA_BAIXO;
        Rectangle retanguloPrincipal = fazRetangulo();
        
        this.widthProperty = retanguloPrincipal.widthProperty();
        this.heightProperty = retanguloPrincipal.heightProperty();

        Line linhaDoMeio = fazLinhaDoMeio();

        Group pontinhosDeCima = fazPontinhos(pedra.getPrimeiroNumero());
        
        Group pontinhosDeBaixo = fazPontinhos(pedra.getSegundoNumero());
        pontinhosDeBaixo.layoutYProperty().bind(this.heightProperty.divide(2));
        
//        this.grupoTudo = new Group(
//                retanguloPrincipal, 
//                linhaDoMeio, 
//                pontinhosDeCima, 
//                pontinhosDeBaixo);
        
        
//        super.getChildren().add(grupoTudo);
        
        super.getChildren().addAll(retanguloPrincipal, 
                linhaDoMeio, 
                pontinhosDeCima, 
                pontinhosDeBaixo)
        ;
        
        this.grupoTudo = this;
        
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
    
    public void setDirecao(Direcao direcao){
        this.direcao = direcao;
        this.grupoTudo.setRotate(direcao.getGraus()); //criar um bind depois...
    }

    public Pedra getPedra() {
        return pedra;
    }

    public DoubleProperty innerRotateProperty(){
        return this.grupoTudo.rotateProperty();
    }
    
    public void posiciona(
            Direcao direcao, 
            ObservableDoubleValue layoutX, 
            ObservableDoubleValue layoutY){
        
        this.setDirecao(direcao);
        this.layoutXProperty().bind(layoutX);
        this.layoutYProperty().bind(layoutY);
    }
    
    
    public void posicionaEmCima(PedraFx pedra){
        if(this.direcao.ehVertical()){
            pedra.posiciona(Direcao.PRA_CIMA,this.layoutXProperty(),this.layoutYProperty().subtract(this.heightProperty));
        } else {
            pedra.posiciona(Direcao.PRA_CIMA,this.layoutXProperty(),this.layoutYProperty().subtract(this.heightProperty.multiply(3).divide(4)));
        }
        
    }
    
    public void encaixa(PedraFx pedraFx){
        if(direcaoFileira == null){
            direcaoFileira = this.direcao.ehVertical()
                ? this.getPedra().isCarroca()
                ? Direcao.PRA_ESQUERDA
                : Direcao.PRA_CIMA
                : this.getPedra().isCarroca()
                ? Direcao.PRA_CIMA
                : Direcao.PRA_ESQUERDA;
        }
        
        this.direcaoFileira = Direcao.PRA_BAIXO;

        
        
        if(this.getPedra().isCarroca()){
            encaixaNaCarroca(pedraFx);
        } else if(pedraFx.getPedra().isCarroca()){
            encaixaCarroca(pedraFx);
        } else {
            encaixaNormal(pedraFx);
        }
        
    }

    private void encaixaNaCarroca(PedraFx pedraFx) {
        
        final ObservableDoubleValue layouyX;
        final ObservableDoubleValue layouyY;
        final Direcao direcaoPedraFx;
        
        if(this.direcao.ehVertical()){
            layouyY = this.layoutYProperty().add(this.heightProperty.multiply(1/4));
            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
                
                layouyX = this.layoutXProperty().subtract(this.widthProperty.multiply(1.5));
                
                direcaoPedraFx = 
                    this.pedra.getPrimeiroNumero() == pedraFx.pedra.getPrimeiroNumero()
                        ? Direcao.PRA_DIREITA
                        : Direcao.PRA_ESQUERDA;
            } else { //direcaoFileira == Direcao.PRA_DIREITA
                layouyX = this.layoutXProperty().add(this.widthProperty.multiply(1.5));
                
                direcaoPedraFx = 
                    this.pedra.getPrimeiroNumero() == pedraFx.pedra.getPrimeiroNumero()
                        ? Direcao.PRA_ESQUERDA
                        : Direcao.PRA_DIREITA;
                
            }
        } else { // this.direcao.ehHorizontal()
            layouyX = this.layoutXProperty().add(this.heightProperty.multiply(1/4));
            if(this.direcaoFileira == Direcao.PRA_CIMA){
                
                layouyY = this.layoutYProperty().subtract(this.heightProperty.multiply(0.75));
                
                direcaoPedraFx = 
                    this.pedra.getPrimeiroNumero() == pedraFx.pedra.getPrimeiroNumero()
                        ? Direcao.PRA_CIMA
                        : Direcao.PRA_BAIXO;
            } else { //direcaoFileira == Direcao.PRA_BAIXO
                layouyY = this.layoutYProperty().add(this.heightProperty.multiply(0.75));
                
                direcaoPedraFx = 
                    this.pedra.getPrimeiroNumero() == pedraFx.pedra.getPrimeiroNumero()
                        ? Direcao.PRA_BAIXO
                        : Direcao.PRA_CIMA;
            }
            
        }
        
        pedraFx.layoutXProperty().bind(layouyX);
        pedraFx.layoutYProperty().bind(layouyY);
        pedraFx.setDirecao(direcaoPedraFx);
    }

    private void encaixaCarroca(PedraFx pedraFx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void encaixaNormal(PedraFx pedraFx) {
        final ObservableDoubleValue layouyX;
        final ObservableDoubleValue layouyY;
        Direcao direcaoPedraFx = null;

        if(this.direcaoFileira.ehHorizontal()){
            layouyY = this.layoutYProperty();
            
            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
                layouyX = this.layoutXProperty().subtract(this.heightProperty);
                
                if(this.direcao == Direcao.PRA_ESQUERDA){
                    //segundo numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getSegundoNumero() 
                            ? Direcao.PRA_ESQUERDA
                            : Direcao.PRA_DIREITA; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getPrimeiroNumero() 
                            ? Direcao.PRA_ESQUERDA
                            : Direcao.PRA_DIREITA; 
                }
                
            } else { //this.direcaoFileira == Direcao.PRA_DIREITA
                layouyX = this.layoutXProperty().add(this.heightProperty);

                if(this.direcao == Direcao.PRA_ESQUERDA){
                    //primeiro numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getPrimeiroNumero()
                            ? Direcao.PRA_DIREITA
                            : Direcao.PRA_ESQUERDA; 
                } else {
                    //segundo numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getSegundoNumero()
                            ? Direcao.PRA_DIREITA
                            : Direcao.PRA_ESQUERDA; 
                }

            }
            
        } else { //this.direcaoFileira.ehVertical()
            layouyX = this.layoutXProperty();
            
            if(this.direcaoFileira == Direcao.PRA_CIMA){
                layouyY = this.layoutYProperty().subtract(this.heightProperty);
                if(this.direcao == Direcao.PRA_CIMA){
                    //segundo numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getSegundoNumero() 
                            ? Direcao.PRA_CIMA
                            : Direcao.PRA_BAIXO; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getPrimeiroNumero() 
                            ? Direcao.PRA_CIMA
                            : Direcao.PRA_BAIXO; 
                }
            } else {//this.direcaoFileira == Direcao.PRA_BAIXO
                layouyY = this.layoutYProperty().add(this.heightProperty);
                if(this.direcao == Direcao.PRA_CIMA){
                    //primeiro numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getPrimeiroNumero()
                            ? Direcao.PRA_BAIXO
                            : Direcao.PRA_CIMA; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = pedraFx.pedra.getPrimeiroNumero() == this.pedra.getSegundoNumero()
                            ? Direcao.PRA_BAIXO
                            : Direcao.PRA_CIMA; 
                }
            }
        }
        pedraFx.layoutXProperty().bind(layouyX);
        pedraFx.layoutYProperty().bind(layouyY);
        pedraFx.setDirecao(direcaoPedraFx);

    }
}
