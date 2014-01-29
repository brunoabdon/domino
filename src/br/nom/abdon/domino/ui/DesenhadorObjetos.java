package br.nom.abdon.domino.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import br.nom.abdon.domino.Numero;

class DesenhadorObjetos {

	public enum Posicao {EM_PE,DEITADO};
	
	private final Graphics2D g;
	
	private static final float alturaDaPedra = 150;
	
	private static final float larguraDaPedra = alturaDaPedra/2f;
	private static final float curvaturaDaQuina = larguraDaPedra*0.3f;
	
	private static final float afastacaoDaLinha = larguraDaPedra*0.1f;
	private static final float tamanhoDaLinhaDoMeio = larguraDaPedra - afastacaoDaLinha - afastacaoDaLinha;

	private static final float raioDosPontinhos = larguraDaPedra/6f;
	private static final float afastacaoDosPontinhos = larguraDaPedra*0.15f;

	private static final float pontinhosDaEsquerdaX = afastacaoDosPontinhos;
	private static final float pontinhosDaDireitaX = larguraDaPedra - afastacaoDosPontinhos - raioDosPontinhos;

	private static final float pontinhoDoMeioX = (alturaDaPedra/4)-(raioDosPontinhos/2);
	private static final float pontinhoDoMeioY = (alturaDaPedra-raioDosPontinhos)/4f;

	
	private static final float pontinhosDaPrimeiraLinhaY = raioDosPontinhos;
	private static final float pontinhosDaSegundaLinhaY = pontinhoDoMeioY; 
	private static final float pontinhosDaTerceiraLinhaY =  (alturaDaPedra/2) - raioDosPontinhos - afastacaoDosPontinhos;
	
	private static final RoundRectangle2D.Float retanguloPedra = new RoundRectangle2D.Float(0f, 0f, larguraDaPedra, alturaDaPedra, curvaturaDaQuina, curvaturaDaQuina);
    private static final Line2D.Float linhaDoMeio = new Line2D.Float(afastacaoDaLinha, (alturaDaPedra/2), afastacaoDaLinha + tamanhoDaLinhaDoMeio, (alturaDaPedra/2));
    
    private static final Shape pontinhoEmCimaEsquerda = fazPontinho(pontinhosDaEsquerdaX, pontinhosDaPrimeiraLinhaY);
    private static final Shape pontinhoNoMeioEsquerda = fazPontinho(pontinhosDaEsquerdaX, pontinhosDaSegundaLinhaY);
    private static final Shape pontinhoEmbaixoEsquerda = fazPontinho(pontinhosDaEsquerdaX, pontinhosDaTerceiraLinhaY);
    private static final Shape pontinhoEmCimaDireita = fazPontinho(pontinhosDaDireitaX, pontinhosDaPrimeiraLinhaY);
    private static final Shape pontinhoNoMeioDireita = fazPontinho(pontinhosDaDireitaX, pontinhosDaSegundaLinhaY);
    private static final Shape pontinhoEmBaixoDireita = fazPontinho(pontinhosDaDireitaX, pontinhosDaTerceiraLinhaY);
    private static final Shape pontinhoDoMeio = fazPontinho(pontinhoDoMeioX,pontinhoDoMeioY);
    		
    private static final Shape pontinhosDosNumeros[][] = new Shape[][]{
    	new Shape[]{}, //limpo
    	new Shape[]{pontinhoDoMeio}, //pio
    	new Shape[]{pontinhoEmCimaEsquerda,pontinhoEmBaixoDireita}, //duque
    	new Shape[]{pontinhoEmCimaEsquerda,pontinhoDoMeio,pontinhoEmBaixoDireita}, //terno
    	new Shape[]{pontinhoEmCimaEsquerda,pontinhoEmCimaDireita,pontinhoEmbaixoEsquerda,pontinhoEmBaixoDireita}, //quadra
    	new Shape[]{pontinhoEmCimaEsquerda,pontinhoEmCimaDireita,pontinhoDoMeio,pontinhoEmbaixoEsquerda,pontinhoEmBaixoDireita}, //quina
    	new Shape[]{pontinhoEmCimaEsquerda,pontinhoNoMeioEsquerda, pontinhoEmbaixoEsquerda,pontinhoEmCimaDireita,pontinhoNoMeioDireita,pontinhoEmBaixoDireita}, //senha
    };
    
    private static final AffineTransform rotacaoPedra; 
    private static final AffineTransform translacaoCabeca = AffineTransform.getTranslateInstance(0, alturaDaPedra/2);


    private AffineTransform translation;
    
    static {
    	rotacaoPedra = AffineTransform.getQuadrantRotateInstance(3,0,0);
    	rotacaoPedra.translate(-larguraDaPedra, 0);
    }
    
	public DesenhadorObjetos(Graphics2D graphics2d) {
		this.g = graphics2d;
		this.translation = new AffineTransform();

	}
	
	public void desenhaPedra(Numero primeiraCabeca, Numero segundaCabeca, Posicao posicao, float x, float y){
		
		AffineTransform transformacao = posicao == Posicao.EM_PE ? null : rotacaoPedra;
		
		g.setColor(Color.WHITE);
		preenche(retanguloPedra, x, y, transformacao);
		g.setColor(Color.BLACK);
		desenha(retanguloPedra, x, y, transformacao);
		desenha(linhaDoMeio, x, y, transformacao);
		
		desenhaNumero(primeiraCabeca, x, y,transformacao);
		
		if(transformacao != null){
			transformacao.concatenate(translacaoCabeca);
		} else {
			transformacao = translacaoCabeca;
		}
		
		desenhaNumero(segundaCabeca, x, y,transformacao);
		
	}
	
	private void desenhaNumero(Numero numero, float x, float y, AffineTransform transformacao){
		Shape pontinhosDoNumero[] = pontinhosDosNumeros[numero.getNumeroDePontos()];
		
		for (Shape pontinho : pontinhosDoNumero) {
			preenche(pontinho, x, y,transformacao);
		}
	}

	public void desenhaPedraEmborcada(Posicao posicao, float x, float y){
		AffineTransform transformacao = posicao == Posicao.EM_PE ? null : rotacaoPedra;

		g.setColor(Color.DARK_GRAY);
		preenche(retanguloPedra, x, y, transformacao);
		desenha(retanguloPedra, x, y, transformacao);
	
	}
	
	
	public void escreveNomeJogador(String nome, float x, float y){
		g.drawString(nome, x, y);
	}

	private void preenche(Shape shape, float x, float y, AffineTransform transformacao) {
		desenha(shape, x, y, transformacao, true);
	}

	private void desenha(Shape shape, float x, float y, AffineTransform transformacao){
		desenha(shape, x, y, transformacao, false);
	}
	
	private void desenha(Shape shape, float x, float y, AffineTransform transformacao, boolean preenche){
		this.translation.setToTranslation(x, y);
		
		if(transformacao != null){
			this.translation.concatenate(transformacao);
		}
		
		AffineTransform originalTransform = this.g.getTransform();
		this.g.setTransform(translation);
		
		if(preenche){
			this.g.fill(shape);
		} else {
			this.g.draw(shape);
		}
		this.g.setTransform(originalTransform);
	}
	

	private static Shape fazPontinho(float x, float y){
		return new Ellipse2D.Float(x, y, raioDosPontinhos, raioDosPontinhos);
	}
	
}
