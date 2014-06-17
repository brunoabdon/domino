/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableDoubleValue;

/**
 *
 * @author bruno
 */
class Chicote {

    private PedraFx pedraFx;
    private Direcao direcaoFileira;
    private final PrototipoMesa prototipoMesa;

    public static Chicote[] inicia(
            PedraFx primeiraPedraFx,
            DoubleExpression larguraMesa,
            DoubleExpression offsetXMesa,
            DoubleExpression offsetYMesa){
                
        
        final DoubleExpression larguraPedras = primeiraPedraFx.widthProperty();
        final DoubleBinding metadeDaMesa = larguraMesa.divide(2);
        
        ObservableDoubleValue xMeioDaMesa = 
                offsetXMesa.add(metadeDaMesa).subtract(larguraPedras.divide(2));
        ObservableDoubleValue yMeioDaMesa = 
                offsetYMesa.add(metadeDaMesa).subtract(larguraPedras);
        
        Direcao direcaoPedra = primeiraPedraFx.getPedra().isCarroca()
            ? Direcao.PRA_BAIXO
            : Direcao.PRA_ESQUERDA;

        primeiraPedraFx.posiciona(direcaoPedra, xMeioDaMesa,yMeioDaMesa);

        PrototipoMesa prototipoMesa = new PrototipoMesa(larguraMesa, offsetXMesa, offsetYMesa);
        
        return new Chicote[] {
            new Chicote(primeiraPedraFx, Direcao.PRA_ESQUERDA, prototipoMesa),
            new Chicote(primeiraPedraFx, Direcao.PRA_DIREITA, prototipoMesa)
            
        };
    }
    
    private Chicote(
            final PedraFx primeiraPedra, 
            final Direcao direcaoFileira, 
            final PrototipoMesa prototipoMesa) {
        
        this.pedraFx = primeiraPedra;
        this.direcaoFileira = direcaoFileira;
        this.prototipoMesa = prototipoMesa;
    }
    
    public void encaixa(PedraFx novaPedraFx){
        
        if(naoCabe()){
            fazCurva();
        }
        
        if(this.pedraFx.getPedra().isCarroca()){
            encaixaNaCarroca(novaPedraFx);
        } else if(novaPedraFx.getPedra().isCarroca()){
            encaixaCarroca(novaPedraFx);
        } else {
            encaixaNormal(novaPedraFx);
        }
        this.pedraFx = novaPedraFx;
        
    }

    private void encaixaNaCarroca(PedraFx novaPedraFx) {
        
        final ObservableDoubleValue layouyX;
        final ObservableDoubleValue layouyY;
        final Direcao direcaoPedraFx;
        
        if(this.direcaoFileira.ehHorizontal()){
            layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.heightExpression().multiply(1/4));
            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
                
                layouyX = this.pedraFx.layoutXProperty().subtract(this.pedraFx.widthProperty().multiply(1.5));
                
                direcaoPedraFx = 
                    this.pedraFx.getPedra().getPrimeiroNumero() == novaPedraFx.getPedra().getPrimeiroNumero()
                        ? Direcao.PRA_ESQUERDA
                        : Direcao.PRA_DIREITA;
            } else { //direcaoFileira == Direcao.PRA_DIREITA
                layouyX = this.pedraFx.layoutXProperty().add(this.pedraFx.widthProperty().multiply(1.5));
                
                direcaoPedraFx = 
                    this.pedraFx.getPedra().getPrimeiroNumero() == novaPedraFx.getPedra().getPrimeiroNumero()
                        ? Direcao.PRA_DIREITA
                        : Direcao.PRA_ESQUERDA;
                
            }
        } else { // this.novaPedraFx.getDirecao().ehHorizontal()
            layouyX = this.pedraFx.layoutXProperty().add(this.pedraFx.heightExpression().multiply(1/4));
            if(this.direcaoFileira == Direcao.PRA_CIMA){
                
                layouyY = this.pedraFx.layoutYProperty().subtract(this.pedraFx.heightExpression().multiply(0.75));
                
                direcaoPedraFx = 
                    this.pedraFx.getPedra().getPrimeiroNumero() == novaPedraFx.getPedra().getPrimeiroNumero()
                        ? Direcao.PRA_CIMA
                        : Direcao.PRA_BAIXO;
            } else { //direcaoFileira == Direcao.PRA_BAIXO
                layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.heightExpression().multiply(0.75));
                
                direcaoPedraFx = 
                    this.pedraFx.getPedra().getPrimeiroNumero() == novaPedraFx.getPedra().getPrimeiroNumero()
                        ? Direcao.PRA_BAIXO
                        : Direcao.PRA_CIMA;
            }
            
        }
        
        novaPedraFx.posiciona(direcaoPedraFx, layouyX, layouyY);
    }

    private void encaixaNormal(PedraFx novaPedraFx) {
        final ObservableDoubleValue layouyX;
        final ObservableDoubleValue layouyY;
        final Direcao direcaoPedraFx;

        if(this.direcaoFileira.ehHorizontal()){
            layouyY = this.pedraFx.layoutYProperty();
            
            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
                layouyX = this.pedraFx.layoutXProperty().subtract(this.pedraFx.heightExpression());
                
                if(this.pedraFx.getDirecao() == Direcao.PRA_ESQUERDA){
                    //segundo numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero() 
                            ? Direcao.PRA_ESQUERDA
                            : Direcao.PRA_DIREITA; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero() 
                            ? Direcao.PRA_ESQUERDA
                            : Direcao.PRA_DIREITA; 
                }
                
            } else { //this.direcaoFileira == Direcao.PRA_DIREITA
                layouyX = this.pedraFx.layoutXProperty().add(this.pedraFx.heightExpression());

                if(this.pedraFx.getDirecao() == Direcao.PRA_ESQUERDA){
                    //primeiro numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero()
                            ? Direcao.PRA_DIREITA
                            : Direcao.PRA_ESQUERDA; 
                } else {
                    //segundo numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero()
                            ? Direcao.PRA_DIREITA
                            : Direcao.PRA_ESQUERDA; 
                }

            }
            
        } else { //this.direcaoFileira.ehVertical()
            layouyX = this.pedraFx.layoutXProperty();
            
            if(this.direcaoFileira == Direcao.PRA_CIMA){
                layouyY = this.pedraFx.layoutYProperty().subtract(this.pedraFx.heightExpression());
                if(this.pedraFx.getDirecao() == Direcao.PRA_CIMA){
                    //segundo numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero() 
                            ? Direcao.PRA_CIMA
                            : Direcao.PRA_BAIXO; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero() 
                            ? Direcao.PRA_CIMA
                            : Direcao.PRA_BAIXO; 
                }
            } else {//this.direcaoFileira == Direcao.PRA_BAIXO
                layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.heightExpression());
                if(this.pedraFx.getDirecao() == Direcao.PRA_CIMA){
                    //primeiro numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getPrimeiroNumero()
                            ? Direcao.PRA_BAIXO
                            : Direcao.PRA_CIMA; 
                } else {
                    //primeiro numero exposto;
                    direcaoPedraFx = novaPedraFx.getPedra().getPrimeiroNumero() == this.pedraFx.getPedra().getSegundoNumero()
                            ? Direcao.PRA_BAIXO
                            : Direcao.PRA_CIMA; 
                }
            }
        }
        
        novaPedraFx.posiciona(direcaoPedraFx, layouyX, layouyY);
    }
    
    private void encaixaCarroca(PedraFx novaPedraFx) {
        final ObservableDoubleValue layouyX;
        final ObservableDoubleValue layouyY;
        final Direcao direcaoPedraFx;
        
        if(this.direcaoFileira.ehHorizontal()){
            layouyY = this.pedraFx.layoutYProperty();
            direcaoPedraFx = Direcao.PRA_BAIXO;
            if(this.direcaoFileira == Direcao.PRA_ESQUERDA){
                layouyX = this.pedraFx.layoutXProperty().subtract(this.pedraFx.widthProperty().multiply(1.5));
            } else {
                layouyX = this.pedraFx.layoutXProperty().add(this.pedraFx.widthProperty().multiply(1.5));
            }
        } else {
            layouyX = this.pedraFx.layoutXProperty();
            direcaoPedraFx = Direcao.PRA_ESQUERDA;
            if(this.direcaoFileira == Direcao.PRA_BAIXO){
                layouyY = this.pedraFx.layoutYProperty().subtract(this.pedraFx.heightExpression().multiply(3/4));
            } else {
                layouyY = this.pedraFx.layoutYProperty().add(this.pedraFx.heightExpression().multiply(1.5));
            }
        }
        
        novaPedraFx.posiciona(direcaoPedraFx, layouyX, layouyY);
    }

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

    
}
