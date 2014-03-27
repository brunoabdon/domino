package br.nom.abdon.domino.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

class Surface extends JPanel {

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        
        
//        g2d.scale(1, 1);
//        desenhaDomino(g2d, Posicao.EM_PE, 100, 100 );
//        
//        desenhaDomino(g2d, Posicao.DEITADO, 100, 100);
        
        DesenhadorObjetos desenhadorObjetos = new DesenhadorObjetos(g2d);
//        desenhador.desenhaPedra(Numero.PIO, Numero.DUQUE, Posicao.EM_PE, 10f, 10f);
//        desenhador.desenhaPedra(Numero.DUQUE, Numero.PIO, Posicao.EM_PE, 90f, 10f);
//        desenhador.desenhaPedra(Numero.TERNO, Numero.LIMPO, Posicao.EM_PE, 170f, 10f);
//        desenhador.desenhaPedra(Numero.QUADRA, Numero.QUADRA, Posicao.EM_PE, 250f, 10f);
//        desenhador.desenhaPedra(Numero.QUINA, Numero.TERNO, Posicao.EM_PE, 330f, 10f);
//        desenhador.desenhaPedra(Numero.SENA, Numero.LIMPO, Posicao.EM_PE, 410f, 10f);
//
//        desenhador.escreveNomeJogador("Bruno Abdon", 10f, 190f);
//        desenhador.desenhaPedra(Numero.SENA, Numero.DUQUE, Posicao.DEITADO, 410f, 200f);
//        desenhador.desenhaPedraEmborcada(Posicao.EM_PE, 90f, 190f);
//        desenhador.desenhaPedra(Numero.QUINA, Numero.SENA, Posicao.DEITADO, 220f, 100f);

        CalculadorDeCoordenadaDePedras calculadorDeCordenadaDePedras = null; //new CalculadorDeCoordenadasDePedrasImpl();
        calculadorDeCordenadaDePedras.init(DesenhadorObjetos.LARGURA_DA_MESA-100, DesenhadorObjetos.ALTURA_DA_MESA-100, DesenhadorObjetos.ALTURA_DA_PEDRA);
		DesenhadorMesa desenhadorMesa = new DesenhadorMesa(desenhadorObjetos, calculadorDeCordenadaDePedras);
        desenhadorMesa.desenhaMesaInicial("Bruno Abdon","Eudes Rafael","Igor Souza","Ronaldo Lopes");
        
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_PIO, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.PIO_DUQUE, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_DUQUE, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.DUQUE_TERNO, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_TERNO, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.TERNO_QUADRA, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.QUADRA_SENA, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_SENA, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_TERNO, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.TERNO_QUADRA, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.QUADRA_SENA, Lado.DIREITO, Numero.QUINA);
        desenhadorMesa.desenhaJogada("bruno", Pedra.CARROCA_DE_SENA, Lado.DIREITO, Numero.QUINA);
        
        
    }
    

    

	private static final float LARGURA_DOMINO = 30;
	private static final float ALTURA_DOMINO = LARGURA_DOMINO*2f;
	private static final float CURVATURA_ESQUINA = LARGURA_DOMINO*0.4f;
	
    private static final float RAIO_DOS_CIRCULOS = LARGURA_DOMINO/5f;
    
    private static final float AFASTACAO_LINHA = LARGURA_DOMINO*0.1f;
    private static final float LINHA_DO_MEIO_TAMANHO = LARGURA_DOMINO - AFASTACAO_LINHA - AFASTACAO_LINHA;

    private void desenhaDomino(Graphics2D g2d, Direcao direcao, float x, float y){

    	final float meioDominoX = x + (LARGURA_DOMINO/2f); 
    	final float meioDominoY = y + (ALTURA_DOMINO/2f); 
			
        final float linhaDoMeioX1 = x + AFASTACAO_LINHA;
	    final float linhaDoMeioX2 = linhaDoMeioX1 + LINHA_DO_MEIO_TAMANHO;
	    final float linhaDoMeioY = y + (ALTURA_DOMINO/2);
			
		final float PONTO_DO_MEIO_CASA_1_X = x + (LARGURA_DOMINO/2f) - (RAIO_DOS_CIRCULOS/2f); 
		final float PONTO_DO_MEIO_CASA_1_Y = x + (ALTURA_DOMINO/4f) - (RAIO_DOS_CIRCULOS/2f); 

//		AffineTransform transform = g2d.getTransform();
		
//		if(posicao == Posicao.DEITADO)  {
//			
//			
//			
//			AffineTransform rotacaoPedra = AffineTransform.getQuadrantRotateInstance(4,x,y);
//			g2d.setTransform(rotacaoPedra);
//			
//			
////			g2d.rotate(Math.toRadians(270),x,y);
//		}
		
		RoundRectangle2D.Float retangulo = new RoundRectangle2D.Float(x, y,LARGURA_DOMINO,ALTURA_DOMINO,CURVATURA_ESQUINA,CURVATURA_ESQUINA);
		Line2D.Float linhaDoMeio = new Line2D.Float(linhaDoMeioX1, linhaDoMeioY, linhaDoMeioX2, linhaDoMeioY);
		
		Area area = new Area(retangulo);
		Area areaLinha = new Area(linhaDoMeio);
		area.add(areaLinha);


		g2d.draw(area);
		
		AffineTransform quadrantRotateInstance = AffineTransform.getQuadrantRotateInstance(3,x,y);
		g2d.setTransform(quadrantRotateInstance);
		g2d.setColor(Color.CYAN);
		g2d.draw(area);

		quadrantRotateInstance.translate(-LARGURA_DOMINO,0);
		g2d.setTransform(quadrantRotateInstance);
		g2d.setColor(Color.MAGENTA);
		g2d.draw(area);

		
		
		
//		g2d.setTransform(transform);
//		g2d.setColor(Color.WHITE);
//		g2d.fill(retangulo);
//
//		g2d.setColor(Color.BLACK);
//		g2d.draw(retangulo);
//		
//		g2d.draw(linhaDoMeio);
        
//        g2d.fill(new Ellipse2D.Double(PONTO_DO_MEIO_CASA_1_X, PONTO_DO_MEIO_CASA_1_Y, RAIO_DOS_CIRCULOS,RAIO_DOS_CIRCULOS));
    }
    
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}

public class Skeleton extends JFrame {

    public Skeleton() {

        initUI();
    }

    private void initUI() {

        setTitle("Simple Java 2D example");

        Surface comp = new Surface();
		add(comp);

        setSize((int)DesenhadorObjetos.LARGURA_DA_MESA+10, (int)DesenhadorObjetos.ALTURA_DA_MESA+10);
        System.out.println(getSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                Skeleton sk = new Skeleton();
                sk.setVisible(true);
            }
        });
    }
}