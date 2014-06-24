/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.nom.abdon.domino.ui.fx;


import java.util.function.BiFunction;
import java.util.function.Predicate;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableDoubleValue;
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

    private static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        adicionaDistancia = 
            (prop,distancia) -> {
                return DoubleExpression.doubleExpression(prop).add(distancia);
        };
    
    private static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        subtraiDistancia = 
            (prop,distancia) -> {
                return DoubleExpression.doubleExpression(prop).subtract(distancia);
        };
    
    private static final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
        ignoraDistancia = (prop,distancia) -> {
            return prop;
        };
    
    public static Chicote[] inicia(
            PedraFx primeiraPedraFx,
            DoubleExpression alturaMesa,
            DoubleExpression larguraMesa,
            DoubleExpression offsetXMesa,
            DoubleExpression offsetYMesa){
        
        boolean ehHorizontal = true; //randomizar

        final ReferenciaDimensoes referenciaDimensoes = 
            new ReferenciaDimensoes(
                    alturaMesa,
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
        
        final boolean ehCarroca = primeiraPedraFx.getPedra().isCarroca();
        final Direcao direcaoPedra = 
            (horizontal && !ehCarroca) || (!horizontal && ehCarroca) 
                ? Direcao.PRA_DIREITA
                : Direcao.PRA_BAIXO;
        
        primeiraPedraFx.posiciona(
            direcaoPedra, 
            referenciaDimensoes.xMeioDaMesa,
            referenciaDimensoes.yMeioDaMesa);
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
 
        final ObservableDoubleValue distancia = 
            direcaoPedraChicote.ehHorizontal() != fileiraTaNaHorizontal
            || ehCarroca 
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.alturaPedra;
        
        posiciona(novaPedraFx, direcaoPedraFx, distancia);
    }

    private void posiciona(
            final PedraFx novaPedraFx, 
            final Direcao direcaoPedraFx, 
            final ObservableDoubleValue distancia) {

        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
            operacao = 
                this.direcaoFileira == Direcao.PRA_BAIXO 
                || this.direcaoFileira == Direcao.PRA_DIREITA
                    ? adicionaDistancia
                    : subtraiDistancia;

        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> operacaoX;
        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> operacaoY;

        if(this.direcaoFileira.ehHorizontal()){
            operacaoX = operacao;
            operacaoY = ignoraDistancia;
        } else {
            operacaoX = ignoraDistancia;
            operacaoY = operacao;
        }
        
        final ObservableDoubleValue meioX = 
            operacaoX.apply(this.pedraFx.xMeioProperty(),distancia);
        
        final ObservableDoubleValue meioY = 
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
        
        final ObservableDoubleValue distanciaX = direcaoFileira.ehHorizontal()
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.medataDaLarguraDePedra;
                    
        final ObservableDoubleValue distanciaY = direcaoFileira.ehVertical()
                ? this.referenciaDeDimensoes.umLadoEMeioDePedra
                : this.referenciaDeDimensoes.medataDaLarguraDePedra;
                
        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
            operacaoX = 
                direcaoFileira == Direcao.PRA_DIREITA
                || direcaoFileira == Direcao.PRA_CIMA
                    ? adicionaDistancia
                    : subtraiDistancia;

        final BiFunction<ObservableDoubleValue, ObservableDoubleValue, ObservableDoubleValue> 
            operacaoY = 
                direcaoFileira == Direcao.PRA_DIREITA
                || direcaoFileira == Direcao.PRA_BAIXO
                    ? adicionaDistancia
                    : subtraiDistancia;
        
        final ObservableDoubleValue meioX = 
            operacaoX.apply(this.pedraFx.xMeioProperty(),distanciaX);
        
        final ObservableDoubleValue meioY = 
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

    private static Direcao fazCurva(final Direcao direcao) {
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
        final ObservableDoubleValue iniXMesa;
        final ObservableDoubleValue iniYMesa;
        final ObservableDoubleValue fimXMesa;
        final ObservableDoubleValue fimYMesa;
        final ObservableDoubleValue xMeioDaMesa;
        final ObservableDoubleValue yMeioDaMesa;

        final ObservableDoubleValue alturaPedra;
        final ObservableDoubleValue larguraPedra;
        final ObservableDoubleValue umLadoEMeioDePedra;
        final ObservableDoubleValue medataDaLarguraDePedra;

        
        public ReferenciaDimensoes(
                ObservableDoubleValue alturaMesa,
                ObservableDoubleValue larguraMesa,
                ObservableDoubleValue offsetXMesa,
                ObservableDoubleValue offsetYMesa,
                ObservableDoubleValue alturaPedra) {

            final DoubleExpression expLarguraMesa = 
                DoubleExpression.doubleExpression(larguraMesa);

            final DoubleExpression expAlturaMesa = 
                DoubleExpression.doubleExpression(alturaMesa);
            
            this.iniXMesa = offsetXMesa;
            this.iniYMesa = offsetYMesa;
            this.fimXMesa = expLarguraMesa.add(offsetXMesa);
            this.fimYMesa = expAlturaMesa.add(offsetYMesa);
            
            this.xMeioDaMesa = expLarguraMesa.divide(2).add(iniXMesa);
            this.yMeioDaMesa = expAlturaMesa.divide(2).add(iniYMesa);
            
            final DoubleExpression expAlturaPedra = 
                DoubleExpression.doubleExpression(alturaPedra);
            
            this.alturaPedra = alturaPedra;
            this.larguraPedra = expAlturaPedra.divide(2);
            this.umLadoEMeioDePedra = expAlturaPedra.multiply(0.75);
            this.medataDaLarguraDePedra = expAlturaPedra.divide(4);
        }
    }

