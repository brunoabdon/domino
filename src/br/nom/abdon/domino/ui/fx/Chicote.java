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

/**
 *
 * @author bruno
 */
class Chicote {

    private PedraFx pedraFx;
    private Direcao direcaoFileira;

    private final ReferenciaDimensoes referenciaDeDimensoes;

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
        
        boolean ehHorizontal = true; //randomizar

        final ReferenciaDimensoes referenciaDimensoes = 
            new ReferenciaDimensoes(
                    larguraMesa, 
                    offsetXMesa, 
                    offsetYMesa,
                    primeiraPedraFx.heightProperty());

        encaixaPrimeiraPedra(
                primeiraPedraFx, 
                referenciaDimensoes, 
                ehHorizontal);
        
        return criaChicotes(primeiraPedraFx, ehHorizontal, referenciaDimensoes);
    }
 
    private static Chicote[] criaChicotes(
            final PedraFx primeiraPedraFx, 
            final boolean ehHorizontal, 
            final ReferenciaDimensoes referenciaDeDimensoes) {

        final Direcao direcaoChicoteEsquerda =
                ehHorizontal
                ? Direcao.PRA_ESQUERDA
                : Direcao.PRA_CIMA;
        
        final Direcao direcaoChicoteDireira = direcaoChicoteEsquerda.inverver();
        
        final Chicote chicoteEsquerda =
            new Chicote(
                direcaoChicoteEsquerda,primeiraPedraFx,referenciaDeDimensoes);
        
        final Chicote chicoteDireita =
            new Chicote(
                direcaoChicoteDireira,primeiraPedraFx,referenciaDeDimensoes);
        
        return new Chicote[] {chicoteEsquerda,chicoteDireita};
    }
    
    private Chicote(
            final Direcao direcaoFileira, 
            final PedraFx primeiraPedra, 
            final ReferenciaDimensoes referenciaDeDimensoes) {
        
        this.pedraFx = primeiraPedra;
        this.direcaoFileira = direcaoFileira;
        this.referenciaDeDimensoes = referenciaDeDimensoes;
    }


    private static void encaixaPrimeiraPedra(
            PedraFx primeiraPedraFx, 
            ReferenciaDimensoes referenciaDimensoes, 
            boolean horizontal) {
        
        final DoubleExpression metadeDaMesa = 
            referenciaDimensoes.larguraMesa.divide(2);
        
        final DoubleExpression xMeioDaMesa = 
            referenciaDimensoes.iniXMesa.add(metadeDaMesa);
        final DoubleExpression yMeioDaMesa = 
            referenciaDimensoes.iniYMesa.add(metadeDaMesa);
        
        final boolean ehCarroca = primeiraPedraFx.getPedra().isCarroca();
        final Direcao direcaoPedra = 
            (horizontal && !ehCarroca) || (!horizontal && ehCarroca) 
                ? Direcao.PRA_DIREITA
                : Direcao.PRA_BAIXO;
        
        primeiraPedraFx.posiciona(direcaoPedra, xMeioDaMesa,yMeioDaMesa);
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
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.alturaPedra;
        
        posiciona(novaPedraFx, direcaoPedraFx, distancia);
    }

    private void posiciona(
            final PedraFx novaPedraFx, 
            final Direcao direcaoPedraFx, 
            final DoubleExpression distancia) {

        final BiFunction<DoubleExpression, DoubleExpression, DoubleExpression> 
            operacao = 
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
        
        final Direcao direcaoPedraFx = 
            novaPedra.getSegundoNumero() == numeroExposto
                ? proximaDirecaoFileira.inverver() 
                : proximaDirecaoFileira;
        
        final DoubleExpression distanciaX = direcaoFileira.ehHorizontal()
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.medataDaLarguraDePedra;
                    
        final DoubleExpression distanciaY = direcaoFileira.ehVertical()
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.medataDaLarguraDePedra;
                
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
        
        final int qtosQuadradosDevemCaber = pedra.isCarroca()? 4 : 3;
        
        final double espacoSeguranca = 
            referenciaDeDimensoes.larguraPedra.get() * qtosQuadradosDevemCaber;

        final double dimencaoPraMedir;
        final double mesaMax;
        
        final Bounds bounds = this.pedraFx.boundsInParentProperty().get();
        
        final Predicate<Double> temEspaco;
        
        if(direcaoFileira == Direcao.PRA_DIREITA){
            dimencaoPraMedir = bounds.getMaxX();
            mesaMax = referenciaDeDimensoes.fimXMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir + espaco < mesaMax;
        } else if(direcaoFileira == Direcao.PRA_ESQUERDA){
            dimencaoPraMedir = bounds.getMinX();
            mesaMax = referenciaDeDimensoes.iniXMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir - espaco > mesaMax;
        } else if(direcaoFileira == Direcao.PRA_BAIXO){
            dimencaoPraMedir = bounds.getMaxY();
            mesaMax = referenciaDeDimensoes.fimYMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir + espaco < mesaMax;
        } else {// if(direcaoFileira == Direcao.PRA_CIMA){
            dimencaoPraMedir = bounds.getMinY();
            mesaMax = referenciaDeDimensoes.iniYMesa.get();
            temEspaco = (espaco) -> dimencaoPraMedir - espaco > mesaMax;
        }    
        
        return temEspaco.test(espacoSeguranca);
    }

    private static Direcao fazCurva(Direcao direcao) {
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
}

class ReferenciaDimensoes{
        final DoubleExpression larguraMesa; //ela é quadrada. ponto.
        final DoubleExpression iniXMesa;
        final DoubleExpression iniYMesa;
        final DoubleExpression fimXMesa;
        final DoubleExpression fimYMesa;

        final DoubleExpression alturaPedra;
        final DoubleExpression larguraPedra;
        final DoubleExpression umLadoEMeioDePedra;
        final DoubleExpression medataDaLarguraDePedra;

        
        public ReferenciaDimensoes(
                DoubleExpression larguraMesa,
                DoubleExpression offsetXMesa,
                DoubleExpression offsetYMesa,
                DoubleExpression alturaPedra) {
            
            this.larguraMesa = larguraMesa;
            this.iniXMesa = offsetXMesa;
            this.iniYMesa = offsetYMesa;
            this.fimXMesa = offsetXMesa.add(larguraMesa);
            this.fimYMesa = offsetYMesa.add(larguraMesa);
            
            this.alturaPedra = alturaPedra;
            this.larguraPedra = alturaPedra.divide(2);
            this.umLadoEMeioDePedra = this.larguraPedra.multiply(1.5);
            this.medataDaLarguraDePedra = alturaPedra.divide(4);

        }
    }

