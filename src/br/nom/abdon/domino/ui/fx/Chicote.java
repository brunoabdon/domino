/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;


import java.util.function.BiFunction;
import java.util.function.Predicate;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Bounds;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import static br.nom.abdon.domino.ui.fx.Direcao.PRA_BAIXO;
import static br.nom.abdon.domino.ui.fx.Direcao.PRA_DIREITA;


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
            (prop,distancia) -> {
                return prop.add(distancia);
        };
    
    private static final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
        subtraiDistancia = 
            (prop,distancia) -> {
                return prop.subtract(distancia);
        };
    
    private static final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
        identidade = (prop,ignoreme) -> {
            return prop;
        };
    
    public static Chicote[] inicia(
            PedraFx primeiraPedraFx,
            DoubleExpression larguraMesa,
            DoubleExpression offsetXMesa,
            DoubleExpression offsetYMesa){
        
        final DoubleExpression metadeDaMesa = larguraMesa.divide(2);
        
        DoubleExpression xMeioDaMesa = offsetXMesa.add(metadeDaMesa);
        DoubleExpression yMeioDaMesa = offsetYMesa.add(metadeDaMesa);
        
        Direcao direcaoPedra = !primeiraPedraFx.getPedra().isCarroca()
            ? Direcao.PRA_BAIXO
            : Direcao.PRA_DIREITA;

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
        
        if(cabe(novaPedraFx.getPedra())){
            encaixaNormal(novaPedraFx);
        } else {
            encaixaFazendoCurva(novaPedraFx);
        }
        this.pedraFx = novaPedraFx;
    }

    private void encaixaNormal(PedraFx novaPedraFx) {
        
        final Pedra pedra = this.pedraFx.getPedra();
        final Pedra novaPedra = novaPedraFx.getPedra();
 
        final boolean eraCarroca = pedra.isCarroca();
        final boolean ehCarroca = novaPedra.isCarroca();
        
        final Direcao direcaoPedraChicote = this.pedraFx.getDirecao();

        final boolean fileiraTaNaHorizontal = 
                this.direcaoFileira.ehHorizontal();
        
        final Direcao direcaoPedraFx;
        if(ehCarroca){
            direcaoPedraFx = fileiraTaNaHorizontal
                ? Direcao.PRA_BAIXO
                : Direcao.PRA_ESQUERDA;            
        } else {
            final Numero numeroExposto = 
                eraCarroca || direcaoPedraChicote != this.direcaoFileira
                    ? pedra.getPrimeiroNumero()
                    : pedra.getSegundoNumero();

            direcaoPedraFx = 
                    novaPedra.getPrimeiroNumero() == numeroExposto
                    ? direcaoFileira 
                    : direcaoFileira.inverver();
        }
 
        final DoubleExpression distancia = 
            direcaoPedraChicote.ehHorizontal() != fileiraTaNaHorizontal
            || ehCarroca 
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
        
        final DoubleExpression meioX = 
                operacaoX.apply(this.pedraFx.xMeioProperty(),distancia);
        
        final DoubleExpression meioY = 
                operacaoY.apply(this.pedraFx.yMeioProperty(),distancia);
            
        novaPedraFx.posiciona(direcaoPedraFx, meioX, meioY);
    }

   
    
    private void encaixaFazendoCurva(PedraFx novaPedraFx) {
        final Pedra pedra = this.pedraFx.getPedra();
        final Pedra novaPedra = novaPedraFx.getPedra();
        final Direcao proximaDirecaoFileira = fazCurva(this.direcaoFileira);
        
        final Numero numeroExposto = 
            this.pedraFx.getDirecao() != this.direcaoFileira
                ? pedra.getPrimeiroNumero()
                : pedra.getSegundoNumero();
        
        System.out.println("numeroExposto " + numeroExposto);
        
        final Direcao direcaoPedraFx = 
            novaPedra.getSegundoNumero() == numeroExposto
                ? proximaDirecaoFileira.inverver() 
                : proximaDirecaoFileira;
        
        final DoubleExpression distanciaX = direcaoFileira.ehHorizontal()
                ? this.prototipoPedra.ladoEMeio
                : this.prototipoPedra.medataDaLargura;
                    
        final DoubleExpression distanciaY = direcaoFileira.ehVertical()
                ? this.prototipoPedra.ladoEMeio
                : this.prototipoPedra.medataDaLargura;
                
        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
            operacaoX = 
                direcaoFileira == Direcao.PRA_DIREITA
                || direcaoFileira == Direcao.PRA_CIMA
                    ? adicionaDistancia
                    : subtraiDistancia;

        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
            operacaoY = 
                direcaoFileira == Direcao.PRA_DIREITA
                || direcaoFileira == Direcao.PRA_BAIXO
                    ? adicionaDistancia
                    : subtraiDistancia;
        
        final DoubleExpression meioX = 
                operacaoX.apply(this.pedraFx.xMeioProperty(),distanciaX);
        
        final DoubleExpression meioY = 
                operacaoY.apply(this.pedraFx.yMeioProperty(),distanciaY);
        
        
        novaPedraFx.posiciona(direcaoPedraFx, meioX, meioY);
        this.direcaoFileira = proximaDirecaoFileira;
    }
    
    private boolean cabe(Pedra pedra) {
        boolean cabe;
        
        System.out.println("\ncabe " + pedra + "?");
        
        final int qtosQuadradosDevemCaber = pedra.isCarroca()? 4 : 3;
        
        final double espacoSeguranca = 
                prototipoPedra.largura.get() * qtosQuadradosDevemCaber;

        System.out.println("Tem que ter espaço pra " + qtosQuadradosDevemCaber 
                + " quadrados (" + espacoSeguranca + " pixels).");
        
        final double dimencaoPraMedir;
        final double mesaMax;
        
        final Bounds bounds = this.pedraFx.boundsInParentProperty().get();

        
        Predicate<Double> temEspaco;
        
        if(direcaoFileira == Direcao.PRA_DIREITA){
            dimencaoPraMedir = bounds.getMaxX();
            mesaMax = prototipoMesa.fimXMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir + espaco < mesaMax;
            
        } else if(direcaoFileira == Direcao.PRA_ESQUERDA){
            dimencaoPraMedir = bounds.getMinX();
            mesaMax = prototipoMesa.iniXMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir - espaco > mesaMax;
        } else if(direcaoFileira == Direcao.PRA_BAIXO){
            dimencaoPraMedir = bounds.getMaxY();
            mesaMax = prototipoMesa.fimYMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir + espaco < mesaMax;
        } else {// if(direcaoFileira == Direcao.PRA_CIMA){
            dimencaoPraMedir = bounds.getMinY();
            mesaMax = prototipoMesa.iniYMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir - espaco > mesaMax;
        }    

        
        cabe = temEspaco.test(espacoSeguranca);

        System.out.println("O chicote tá indo até o pixel " + dimencaoPraMedir);
        System.out.println("A mesa vai até o pixel " + mesaMax);
        System.out.println("cabe? cabe " + (cabe?"sim":"não"));
            
        return cabe;
    }

    public static Direcao fazCurva(Direcao direcao) {
        Direcao direcaoCurva;

        switch(direcao){
                case PRA_BAIXO:    direcaoCurva = Direcao.PRA_ESQUERDA; break;
                case PRA_ESQUERDA: direcaoCurva = Direcao.PRA_CIMA; break;
                case PRA_CIMA:     direcaoCurva = Direcao.PRA_DIREITA; break;
                case PRA_DIREITA:  direcaoCurva = Direcao.PRA_BAIXO; break;
                default: throw new IllegalStateException();
        }

        return direcaoCurva;
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
        final DoubleExpression ladoEMeio;
        final DoubleExpression medataDaLargura;

        public PrototipoPedra(
                DoubleExpression alturaPedra) {
            
            this.altura = alturaPedra;
            this.largura = alturaPedra.divide(2);
            this.ladoEMeio = this.largura.multiply(1.5);
            this.medataDaLargura = alturaPedra.divide(4);
        }
    }
    
}
