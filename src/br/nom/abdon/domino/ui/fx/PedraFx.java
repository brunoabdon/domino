package br.nom.abdon.domino.ui.fx;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author Bruno
 */
public class PedraFx extends Group {

        public static final float LARGURA_DA_MESA = 1260;
	public static final float ALTURA_DA_MESA = LARGURA_DA_MESA*0.6f;

	public static final float ALTURA_DA_PEDRA = LARGURA_DA_MESA/10;
	public static final float LARGURA_DA_PEDRA = ALTURA_DA_PEDRA/2f;

	private static final float pontinhosDaEsquerdaX = 0;
	private static final float pontinhosDaDireitaX = 2;

	private static final float pontinhoDoMeioX = 1;
	private static final float pontinhoDoMeioY = 1;

	
	private static final float pontinhosDaPrimeiraLinhaY = 0;
	private static final float pontinhosDaSegundaLinhaY = 1; 
	private static final float pontinhosDaTerceiraLinhaY =  2;

    private final Rectangle retanguloPrincipal;
        
    public PedraFx(Pedra pedra) {
        super();
        
        this.retanguloPrincipal = new Rectangle(1,2);
        final DoubleProperty propAltura = this.retanguloPrincipal.heightProperty();
        final DoubleBinding bindingMetadeAltura = propAltura.divide(2);
        
        this.retanguloPrincipal.widthProperty().bind(bindingMetadeAltura);
        final DoubleProperty propriedadeTamArco = this.retanguloPrincipal.arcHeightProperty();
        propriedadeTamArco.bind(propAltura.divide(8));
        this.retanguloPrincipal.arcWidthProperty().bind(propriedadeTamArco);
        
        Line linhaDoMeio = new Line();
        final DoubleProperty propWidthLinha = linhaDoMeio.endXProperty();
        propWidthLinha.bind(propAltura.multiply(0.4));
        linhaDoMeio.strokeWidthProperty().bind(propWidthLinha.divide(20));
            
        linhaDoMeio.layoutXProperty().bind(propAltura.divide(20));
        linhaDoMeio.layoutYProperty().bind(bindingMetadeAltura);
        linhaDoMeio.getStyleClass().add("linhaDePedra");

        final Circle pontinho = new Circle();
        
        pontinho.radiusProperty().bind(propAltura.divide(18));
        pontinho.layoutXProperty().bind(propAltura.divide(4));
        pontinho.layoutYProperty().bind(propAltura.divide(4));

        
        pontinho.getStyleClass().add("pontinhoDoMeio");

        Circle pontinho2 = new Circle();
        
        pontinho2.radiusProperty().bind(propAltura.divide(18));
        pontinho2.layoutXProperty().bind(propAltura.divide(8));
        pontinho2.layoutYProperty().bind(propAltura.divide(8));

        
        pontinho2.getStyleClass().add("pontinhoDoMeio");
        
        Group desenhoPedraEmBranco = new Group(retanguloPrincipal, linhaDoMeio, pontinho, pontinho2);
            
        
//        Group pontinhosPrimeiroNumero = fazPontinhos(pedra.getPrimeiroNumero());
//        Group pontinhosSegundoNumero = fazPontinhos(pedra.getSegundoNumero());
//        pontinhosSegundoNumero.setTranslateY(ALTURA_DA_PEDRA/2);
//        
//        Group pontinhosDaPEdra = new Group(pontinhosPrimeiroNumero,pontinhosSegundoNumero);
        
        
//        final Circle pontinho = fazPontinho();
        
        
//        Group pontinhosDaPEdra = new Group(pontinho);
//        System.out.println(this.isAutoSizeChildren());
        
        super.getChildren().add(desenhoPedraEmBranco);
//        super.getChildren().add(pontinhosDaPEdra);
        this.setId(pedra.name());
        this.getStyleClass().add("pedra");
     }

    public DoubleProperty widthProperty(){
        return this.retanguloPrincipal.widthProperty();
    }
    public DoubleProperty heightProperty(){
        return this.retanguloPrincipal.heightProperty();
    }
    


//    private Group fazPontinhos(final Numero numero) {
//        final Group grupoDePontinhos = new Group();
//        final ObservableList<Node> pontinhos = grupoDePontinhos.getChildren();
//        
//        final int numeroDePontos = numero.getNumeroDePontos();
//        
//        if((numeroDePontos % 2)!= 0){
//            pontinhos.add(fazPontinho(pontinhoDoMeioX, pontinhoDoMeioY));
//        }
//                
//        if(numeroDePontos >= 2){
//            pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaPrimeiraLinhaY));
//            pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaTerceiraLinhaY));
//
//            if(numeroDePontos >= 4){
//                pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaTerceiraLinhaY));
//                pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaPrimeiraLinhaY));
//            
//                if(numeroDePontos == 6){
//                    pontinhos.add(fazPontinho(pontinhosDaEsquerdaX, pontinhosDaSegundaLinhaY));
//                    pontinhos.add(fazPontinho(pontinhosDaDireitaX, pontinhosDaSegundaLinhaY));
//                }
//            
//            }
//        }
//        
//        return grupoDePontinhos;
//        
//    }

    
//    private Shape fazPontinho(float x, float y) {
//        Circle pontinho = fazPontinho();
//        pontinho.setTranslateX(x);
//        pontinho.setTranslateY(y);
//        return pontinho;
//    }
//
//    private Circle fazPontinho() {
//        Circle pontinho = new Circle();
//        
//        final DoubleProperty heightProperty 
//                = this.retanguloPrincipal.heightProperty();
//        final DoubleProperty radiusProperty = pontinho.radiusProperty();
//        
//        radiusProperty.bind(heightProperty.divide(18));
//        pontinho.getStyleClass().add("pontinho");
//        return pontinho;
//    }

}
