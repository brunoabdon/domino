package br.nom.abdon.domino.ui.fx;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableDoubleValue;

/**
 * @author Bruno
 */
class ReferenciaDimensoes{
        private static final double PROPORCAO_MESA_PEDRA = 12;

        final ObservableDoubleValue larguraMesa;
        final ObservableDoubleValue alturaMesa;
        final ObservableDoubleValue iniXMesa;
        final ObservableDoubleValue iniYMesa;
        final ObservableDoubleValue fimXMesa;
        final ObservableDoubleValue fimYMesa;
        final ObservableDoubleValue xMeioDaMesa;
        final ObservableDoubleValue yMeioDaMesa;
        final ObservableDoubleValue umPorCentoDaLarguraDaMesa;
        final ObservableDoubleValue umPorCentoDaAlturaDaMesa;
        
        final ObservableDoubleValue alturaPedra;
        final ObservableDoubleValue larguraPedra;
        final ObservableDoubleValue umLadoEMeioDePedra;
        final ObservableDoubleValue medataDaLarguraDePedra;
        final ObservableDoubleValue distanciaEntrePedrasNaMao;
        
        final ObservableDoubleValue xInicialMaoImpar;
        final ObservableDoubleValue yInicialMaoPar;
        final ObservableDoubleValue xMaoEsq;
        final ObservableDoubleValue xMaoDir;
        final ObservableDoubleValue yMaoCima;
        final ObservableDoubleValue yMaoBaixo;
        
        
        public ReferenciaDimensoes(
                ObservableDoubleValue alturaMesa,
                ObservableDoubleValue larguraMesa,
                ObservableDoubleValue offsetXMesa,
                ObservableDoubleValue offsetYMesa){
            
            final DoubleExpression expLarguraMesa = 
                DoubleExpression.doubleExpression(larguraMesa);

            final DoubleExpression expAlturaMesa = 
                DoubleExpression.doubleExpression(alturaMesa);
            
            this.alturaMesa = alturaMesa;
            this.larguraMesa = larguraMesa;
            
            this.iniXMesa = offsetXMesa;
            this.iniYMesa = offsetYMesa;
            this.fimXMesa = expLarguraMesa.add(offsetXMesa);
            this.fimYMesa = expAlturaMesa.add(offsetYMesa);
            
            this.xMeioDaMesa = expLarguraMesa.divide(2).add(iniXMesa);
            this.yMeioDaMesa = expAlturaMesa.divide(2).add(iniYMesa);
        
            this.umPorCentoDaLarguraDaMesa = expLarguraMesa.divide(100d);
            this.umPorCentoDaAlturaDaMesa = expAlturaMesa.divide(100d);     
            
            final DoubleExpression expAlturaPedra = 
                expLarguraMesa.divide(PROPORCAO_MESA_PEDRA);

            this.alturaPedra = expAlturaPedra;
            this.larguraPedra = expAlturaPedra.divide(2);
            this.umLadoEMeioDePedra = expAlturaPedra.multiply(0.75);
            this.medataDaLarguraDePedra = expAlturaPedra.divide(4);
            this.distanciaEntrePedrasNaMao = umLadoEMeioDePedra;

            final ObservableDoubleValue espacoPraParede = alturaPedra;
            
            final ObservableDoubleValue espacoDoMeioPraPontaDaMao = 
                DoubleExpression.doubleExpression(
                    distanciaEntrePedrasNaMao)
                    .multiply(2.5);
            
            this.xInicialMaoImpar = 
                DoubleExpression.doubleExpression(xMeioDaMesa)
                .subtract(espacoDoMeioPraPontaDaMao);            
            
            this.yInicialMaoPar = 
                DoubleExpression.doubleExpression(yMeioDaMesa)
                .subtract(espacoDoMeioPraPontaDaMao);            
                
            this.xMaoEsq = 
                DoubleExpression
                    .doubleExpression(iniXMesa)
                    .subtract(espacoPraParede);
            this.xMaoDir = 
                DoubleExpression
                    .doubleExpression(fimXMesa)
                    .add(espacoPraParede);
            this.yMaoCima = 
                DoubleExpression
                    .doubleExpression(iniYMesa)
                    .subtract(espacoPraParede);
            this.yMaoBaixo = 
                DoubleExpression
                    .doubleExpression(fimYMesa)
                    .add(espacoPraParede);
        }
    }
