package br.nom.abdon.domino.ui.fx;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableDoubleValue;

/**
 * @author Bruno
 */
class ReferenciaDimensoes{
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
        
            this.umPorCentoDaLarguraDaMesa = expLarguraMesa.divide(100d);
            this.umPorCentoDaAlturaDaMesa = expAlturaMesa.divide(100d);     
            
            final DoubleExpression expAlturaPedra = 
                DoubleExpression.doubleExpression(alturaPedra);
            
            this.alturaPedra = alturaPedra;
            this.larguraPedra = expAlturaPedra.divide(2);
            this.umLadoEMeioDePedra = expAlturaPedra.multiply(0.75);
            this.medataDaLarguraDePedra = expAlturaPedra.divide(4);
        }
    }
