/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;


import javafx.beans.binding.DoubleExpression;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import java.util.function.BiFunction;


/**
 *
 * @author bruno
 */
class Chicote {

    private PedraFx pedraFx;
    private Direcao direcaoFileira;
    private final PrototipoMesa prototipoMesa;
    private final PrototipoPedra prototipoPedra;

    private static final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
        adicionaDistancia = 
            (prop,distancia) -> {return prop.add(distancia);
        };
    
    private static final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
        subtraiDistancia = 
            (prop,distancia) -> {return prop.subtract(distancia);
        };
    
    private static final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
            identidade = (prop,ignoreme) -> {return prop;};
    
    public static Chicote[] inicia(
            PedraFx primeiraPedraFx,
            DoubleExpression larguraMesa,
            DoubleExpression offsetXMesa,
            DoubleExpression offsetYMesa){
        
        final DoubleExpression larguraPedras = primeiraPedraFx.widthProperty();
        final DoubleExpression metadeDaMesa = larguraMesa.divide(2);
        
        DoubleExpression xMeioDaMesa = 
                offsetXMesa.add(metadeDaMesa).subtract(larguraPedras.divide(2));
        DoubleExpression yMeioDaMesa = 
                offsetYMesa.add(metadeDaMesa).subtract(larguraPedras);
        
        Direcao direcaoPedra = !primeiraPedraFx.getPedra().isCarroca()
            ? Direcao.PRA_BAIXO
            : Direcao.PRA_ESQUERDA;

        primeiraPedraFx.posiciona(direcaoPedra, xMeioDaMesa,yMeioDaMesa);

        PrototipoMesa prototipoMesa = 
                new PrototipoMesa(larguraMesa, offsetXMesa, offsetYMesa);

        PrototipoPedra prototipoPedra = 
                new PrototipoPedra(primeiraPedraFx.heightProperty());
        
        return new Chicote[] {
            new Chicote(Direcao.PRA_CIMA, primeiraPedraFx, prototipoMesa, prototipoPedra),
            new Chicote(Direcao.PRA_BAIXO, primeiraPedraFx, prototipoMesa, prototipoPedra)
            
        };
    }
    
    private Chicote(
            final Direcao direcaoFileira, 
            final PedraFx primeiraPedra, 
            final PrototipoMesa prototipoMesa,
            final PrototipoPedra prototipoPedra) {
        
        this.pedraFx = primeiraPedra;
        this.direcaoFileira = direcaoFileira;
        this.prototipoMesa = prototipoMesa;
        this.prototipoPedra = prototipoPedra;
    }
    
    public void encaixa(PedraFx novaPedraFx){
        
        if(naoCabe()){
//            encaixaFazendoCurva(novaPedraFx);
            throw new UnsupportedOperationException("tá cheio");
        } else {
            encaixaNormal(novaPedraFx);
        }
        this.pedraFx = novaPedraFx;
    }

    private void encaixaNormal(PedraFx novaPedraFx) {
        
        final Pedra pedra = this.pedraFx.getPedra();
        final Pedra novaPedra = novaPedraFx.getPedra();
 
        final boolean eraCarroca = pedra.isCarroca();
        final boolean ehCarroca = novaPedra.isCarroca();
        
        final Direcao direcaoPedraFx;
        if(ehCarroca){
            direcaoPedraFx = this.direcaoFileira.ehHorizontal()
                ? Direcao.PRA_BAIXO
                : Direcao.PRA_ESQUERDA;            
        } else {
            final Numero numeroExposto = 
                this.pedraFx.getDirecao() != this.direcaoFileira || eraCarroca
                    ? pedra.getPrimeiroNumero()
                    : pedra.getSegundoNumero();

            direcaoPedraFx = 
                    novaPedra.getPrimeiroNumero() == numeroExposto
                    ? direcaoFileira 
                    : direcaoFileira.inverver();
       }
 
        final DoubleExpression distancia = 
            eraCarroca || ehCarroca
                ? this.prototipoPedra.ladoEMeio
                : this.prototipoPedra.altura;
        
        posiciona(novaPedraFx, direcaoPedraFx, distancia);
    }

    private void posiciona(PedraFx novaPedraFx, Direcao direcaoPedraFx, DoubleExpression distancia) {

        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> operacao = 
            this.direcaoFileira == Direcao.PRA_BAIXO 
            || this.direcaoFileira == Direcao.PRA_DIREITA
                ? adicionaDistancia
                : subtraiDistancia;

        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> operacaoX;
        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> operacaoY;

        if(this.direcaoFileira.ehHorizontal()){
            operacaoX = operacao;
            operacaoY = identidade;
        } else {
            operacaoX = identidade;
            operacaoY = operacao;
        }
        
        final DoubleExpression layouyX = 
                operacaoX.apply(this.pedraFx.layoutXProperty(),distancia);
        
        final DoubleExpression layouyY = 
                operacaoY.apply(this.pedraFx.layoutYProperty(),distancia);
            
        novaPedraFx.posiciona(direcaoPedraFx, layouyX, layouyY);
    }

        
        
        
    
    
//    private void encaixaFazendoCurva(PedraFx novaPedraFx) {
//        final DoubleExpression layouyX;
//        final DoubleExpression layouyY;
//        final Direcao direcaoPedraFx;
//
//        if(this.direcaoFileira.ehHorizontal()){
//            
//            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
//                layouyX = this.pedraFx.layoutXProperty();
//                layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.widthProperty());
//
//                final Numero primeiroNumeroDaPedraDoChicote = novaPedraFx.getPedra().getPrimeiroNumero();
//                if(this.pedraFx.getDirecao() == Direcao.PRA_CIMA){
//                    
//                    direcaoPedraFx = primeiroNumeroDaPedraDoChicote == this.pedraFx.getPedra().getSegundoNumero() 
//                            ? Direcao.PRA_CIMA
//                            : Direcao.PRA_BAIXO; 
//                } else {
//                    direcaoPedraFx = primeiroNumeroDaPedraDoChicote == this.pedraFx.getPedra().getPrimeiroNumero() 
//                            ? Direcao.PRA_CIMA
//                            : Direcao.PRA_BAIXO; 
//                }
//                
//            } else { //this.direcaoFileira == Direcao.PRA_DIREITA
//                layouyX = this.pedraFx.layoutXProperty().add(this.pedraFx.heightExpression());
//
//                if(this.pedraFx.getDirecao() == Direcao.PRA_ESQUERDA){
//                    //primeiro numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero()
//                            ? Direcao.PRA_DIREITA
//                            : Direcao.PRA_ESQUERDA; 
//                } else {
//                    //segundo numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero()
//                            ? Direcao.PRA_DIREITA
//                            : Direcao.PRA_ESQUERDA; 
//                }
//
//            }
//            
//        } else { //this.direcaoFileira.ehVertical()
//            layouyX = this.pedraFx.layoutXProperty();
//            
//            if(this.direcaoFileira == Direcao.PRA_CIMA){
//                layouyY = this.pedraFx.layoutYProperty().subtract(this.pedraFx.heightExpression());
//                if(this.pedraFx.getDirecao() == Direcao.PRA_CIMA){
//                    //segundo numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero() 
//                            ? Direcao.PRA_CIMA
//                            : Direcao.PRA_BAIXO; 
//                } else {
//                    //primeiro numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero() 
//                            ? Direcao.PRA_CIMA
//                            : Direcao.PRA_BAIXO; 
//                }
//            } else {//this.direcaoFileira == Direcao.PRA_BAIXO
//                layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.heightExpression());
//                if(this.pedraFx.getDirecao() == Direcao.PRA_CIMA){
//                    //primeiro numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero()
//                            ? Direcao.PRA_BAIXO
//                            : Direcao.PRA_CIMA; 
//                } else {
//                    //primeiro numero exposto;
//                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero()
//                            ? Direcao.PRA_BAIXO
//                            : Direcao.PRA_CIMA; 
//                }
//            }
//        }
//        
//        novaPedraFx.posiciona(direcaoPedraFx, layouyX, layouyY);
//    }
    
    private boolean naoCabe() {
        boolean naoCabe;
        
        final double espacoSeguranca = this.pedraFx.widthProperty().get() * 3;
        
        if(direcaoFileira == Direcao.PRA_DIREITA){
            final double chicoteMaxX = this.pedraFx.boundsInParentProperty().get().getMaxX();
            final double mesaMaxX = prototipoMesa.fimXMesa.get();
            
            naoCabe = chicoteMaxX + espacoSeguranca > mesaMaxX;
            
        } else if (direcaoFileira == Direcao.PRA_ESQUERDA){

            final double chicoteMinX = this.pedraFx.boundsInParentProperty().get().getMinX();
            final double mesaMinX = prototipoMesa.iniXMesa.get();
            
            naoCabe = chicoteMinX - espacoSeguranca < mesaMinX;
            
            System.out.println("min x = " + chicoteMinX);
            System.out.println("ini da mesa  = " + mesaMinX);
            System.out.println("cabe? cabe " + (naoCabe?"não":"sim"));

        }
        
        return false;
    }

    private void fazCurva() {
        this.direcaoFileira = 
                
                direcaoFileira == 
                
                Direcao.PRA_ESQUERDA ? Direcao.PRA_CIMA    
		
                : direcaoFileira == 
                
                Direcao.PRA_CIMA ? Direcao.PRA_DIREITA 
                
                : direcaoFileira == 
                
                Direcao.PRA_DIREITA ?  Direcao.PRA_BAIXO   
		
                : Direcao.PRA_ESQUERDA;
                
    }
        
    static class PrototipoMesa{
    
        final DoubleExpression larguraMesa; //ela é quadrada. ponto.
        final DoubleExpression iniXMesa;
        final DoubleExpression iniYMesa;
        final DoubleExpression fimXMesa;
        final DoubleExpression fimYMesa;

        public PrototipoMesa(
                DoubleExpression larguraMesa,
                DoubleExpression offsetXMesa,
                DoubleExpression offsetYMesa) {
            
            this.larguraMesa = larguraMesa;
            this.iniXMesa = offsetXMesa;
            this.iniYMesa = offsetYMesa;
            this.fimXMesa = offsetXMesa.add(larguraMesa);
            this.fimYMesa = offsetYMesa.add(larguraMesa);
        }
    }

    static class PrototipoPedra{
    
        final DoubleExpression altura;
        final DoubleExpression largura;
        final DoubleExpression ladoDoQuadrado;
        final DoubleExpression ladoEMeio;

        public PrototipoPedra(
                DoubleExpression alturaPedra) {
            
            this.altura = alturaPedra;
            this.largura = alturaPedra.divide(2);
            this.ladoDoQuadrado = largura;
            this.ladoEMeio = this.ladoDoQuadrado.multiply(1.5);
            
            System.out.println("altura:  " + alturaPedra.get());
            System.out.println("altura/4: " + alturaPedra.divide(4d).get());
            System.out.println("altura * 1/4: " + alturaPedra.multiply(1/4).get());
            
        }
    }
    
}
