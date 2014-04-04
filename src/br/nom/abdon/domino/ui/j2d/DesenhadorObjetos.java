package br.nom.abdon.domino.ui.j2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

class DesenhadorObjetos {

	private final Graphics2D g;

	public static final float LARGURA_DA_MESA = 1260;
	public static final float ALTURA_DA_MESA = LARGURA_DA_MESA*0.6f;

	public static final float ALTURA_DA_PEDRA = LARGURA_DA_MESA/12;
	public static final float LARGURA_DA_PEDRA = ALTURA_DA_PEDRA/2f;

	private static final float curvaturaDaQuina = LARGURA_DA_PEDRA*0.3f;
	
	private static final float afastacaoDaLinha = LARGURA_DA_PEDRA*0.1f;
	private static final float tamanhoDaLinhaDoMeio = LARGURA_DA_PEDRA - afastacaoDaLinha - afastacaoDaLinha;

	private static final float raioDosPontinhos = LARGURA_DA_PEDRA/6f;
	private static final float afastacaoDosPontinhos = LARGURA_DA_PEDRA*0.15f;

	private static final float pontinhosDaEsquerdaX = afastacaoDosPontinhos;
	private static final float pontinhosDaDireitaX = LARGURA_DA_PEDRA - afastacaoDosPontinhos - raioDosPontinhos;

	private static final float pontinhoDoMeioX = (ALTURA_DA_PEDRA/4)-(raioDosPontinhos/2);
	private static final float pontinhoDoMeioY = (ALTURA_DA_PEDRA-raioDosPontinhos)/4f;

	
	private static final float pontinhosDaPrimeiraLinhaY = raioDosPontinhos;
	private static final float pontinhosDaSegundaLinhaY = pontinhoDoMeioY; 
	private static final float pontinhosDaTerceiraLinhaY =  (ALTURA_DA_PEDRA/2) - raioDosPontinhos - afastacaoDosPontinhos;
	
	private static final Shape toalha = new Rectangle2D.Float(0,0,LARGURA_DA_MESA,ALTURA_DA_MESA);
	private static final RoundRectangle2D.Float retanguloPedra = new RoundRectangle2D.Float(0f, 0f, LARGURA_DA_PEDRA, ALTURA_DA_PEDRA, curvaturaDaQuina, curvaturaDaQuina);
    private static final Line2D.Float linhaDoMeio = new Line2D.Float(afastacaoDaLinha, (ALTURA_DA_PEDRA/2), afastacaoDaLinha + tamanhoDaLinhaDoMeio, (ALTURA_DA_PEDRA/2));
    
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

    private static final Color COR_DA_TOALHA = new Color(0.1f,0.2f,0.1f);
    private static final Color COR_DA_LETRA_DO_NOME_DOS_JOGADORES = Color.GRAY;

    public static final int TAMANHO_DA_LETRA_DO_NOME_DOS_JOGADORES = Math.round(LARGURA_DA_MESA/100);    
	private static final Font LETRA_DO_NOME_DOS_JOGADORES = new Font(Font.SANS_SERIF, Font.PLAIN, TAMANHO_DA_LETRA_DO_NOME_DOS_JOGADORES);

    private static final AffineTransform rotacao90 = fazRotacao(1);
    private static final AffineTransform rotacao180 = fazRotacao(2);
    private static final AffineTransform rotacao270 = fazRotacao(3);
    
    private static final AffineTransform translacaoCabeca = AffineTransform.getTranslateInstance(0, ALTURA_DA_PEDRA/2);
	
    private final AffineTransform translation;
    
	private static AffineTransform fazRotacao(int numQuadrantes) {
		AffineTransform rotacao = AffineTransform.getQuadrantRotateInstance(numQuadrantes,0,0);
		if(numQuadrantes!=1)rotacao.translate(-LARGURA_DA_PEDRA, 0);
		return rotacao;
	}
    
	public DesenhadorObjetos(Graphics2D graphics2d) {
		this.g = graphics2d;
		this.translation = new AffineTransform();
	}
	
	public void desenhaPedra(Pedra pedra, Direcao direcao, float x, float y){
		
		AffineTransform transformacao = pegaRotacao(direcao);
		
		g.setColor(Color.WHITE);
		preenche(retanguloPedra, x, y, transformacao);
		g.setColor(Color.BLACK);
		desenha(retanguloPedra, x, y, transformacao);
		desenha(linhaDoMeio, x, y, transformacao);
		
		desenhaNumero(pedra.getPrimeiroNumero(), x, y,transformacao);
		
		if(transformacao != null){
			transformacao = new AffineTransform(transformacao);
			transformacao.concatenate(translacaoCabeca);
		} else {
			transformacao = translacaoCabeca;
		}
		
		desenhaNumero(pedra.getSegundoNumero(), x, y,transformacao);
		
	}

	private AffineTransform pegaRotacao(Direcao direcao) {
		AffineTransform transformacao = 
				direcao == Direcao.PRA_ESQUERDA ? rotacao90
				: direcao == Direcao.PRA_CIMA ? rotacao180
				: direcao == Direcao.PRA_DIREITA ? rotacao270
				: null;
		return transformacao;
	}
	
	private void desenhaNumero(Numero numero, float x, float y, AffineTransform transformacao){
		Shape pontinhosDoNumero[] = pontinhosDosNumeros[numero.getNumeroDePontos()];
		
		for (Shape pontinho : pontinhosDoNumero) {
			preenche(pontinho, x, y,transformacao);
		}
	}

	public void desenhaPedraEmborcada(Direcao direcao, float x, float y){
		AffineTransform transformacao = pegaRotacao(direcao);

		g.setColor(Color.DARK_GRAY);
		preenche(retanguloPedra, x, y, transformacao);
		desenha(retanguloPedra, x, y, transformacao);
	}
	
	public void escreveNomeJogador(String nome, float x, float y){
		g.setFont(LETRA_DO_NOME_DOS_JOGADORES);
		g.setColor(COR_DA_LETRA_DO_NOME_DOS_JOGADORES);
		g.drawString(nome, x, y);
	}
	
	public void desenhaMesa(){
        g.setColor(COR_DA_TOALHA);
        preenche(toalha, 0, 0);
        g.setColor(Color.BLACK);

	}

	private void preenche(Shape shape, float x, float y) {
		desenha(shape, x, y, null, true);
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
		
		this.g.transform(translation);
		
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
