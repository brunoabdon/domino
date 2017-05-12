/*
 * Copyright (C) 2017 Bruno Abdon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.abdonia.domino.exemplos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 * {@link Jogador} mais simplório possível. Procura a primeira 
 * {@link Pedra} na mão que dá pra jogar na mesa e joga. 
 * 
 * @author Bruno Abdon
 *
 */
public class JogadorMamao implements Jogador {

    /**
     * A  {@link Mesa} em que esse {@link Jogador} 
     * {@link Jogador#sentaNaMesa(com.github.abdonia.domino.Mesa, int) sentou}
     * pra jogar.
     */
    protected Mesa mesa;
    
    /**
     * As {@link Pedra pedras} na mão desse {@link Jogador}.
     */
    protected List<Pedra> mao;

    private boolean perguntouSeEuQueriaJogar;

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.mesa = mesa;
    }

    @Override
    public void recebeMao(final Pedra[] mao) {
        //guardar como uma List
        this.mao = new ArrayList<>(6);
        Collections.addAll(this.mao, mao);
        this.perguntouSeEuQueriaJogar = false;
    }

    @Override
    public Jogada joga() {
        final Jogada jogada;

        Pedra pedraPraJogar = null;
        Lado ladoDeJogar = null;

        if(mesa.taVazia()){
            //opa! minha vez e a mesa tah vazia? é pra comecar agora! sou o 
            //primeiro;
            if(!perguntouSeEuQueriaJogar){
                /* 
                se nao me perguntaram se eu queria ser o da dupla a comecar a 
                jogar,  entao essa é a primeira partida, e o sistema já se 
                ligou que o jogador que tem a maior carroça sou eu. Tenho que 
                jogar ela, se não é roubo.
                */
                pedraPraJogar = JogadorUtils.aMaiorCarroca(this.mao);
            } else {
                /* 
                ah, eles perguntaram se eu queria ser o da dupla a comecar a 
                jogar, e acabou que vou ser eu mesmo a comecar. nao precisa ser 
                carroça. vou jogar a primeira que tiver.
                */
                pedraPraJogar = mao.get(0); 
            }
            //o lado, tanto faz. tá vazia a mesa mesmo.
            ladoDeJogar = Lado.ESQUERDO;
        } else {
            //a mesa nao tá vazia. o jogo tá rolando. tenho que jogar uma peça
            //que se encaixe ou no lado esquerdo ou no direito dos dominós.

            //deixa eu ver quais sao os numeros
            final Numero numeroEsquerda = mesa.getNumeroEsquerda();
            final Numero numeroDireita = mesa.getNumeroDireita();
            //hum, ótimo. agora deixa achar alguma pedra na minha mão
            //que tenha algum desses números.
            for (final Pedra pedraDaMao : mao) {
                if(pedraDaMao.temNumero(numeroEsquerda)){
                    //opa, achei uma. vai ser ela mesmo.
                    pedraPraJogar = pedraDaMao;
                    //na esquerda, que foi o lado que eu vi que tenho o número.
                    ladoDeJogar = Lado.ESQUERDO; 
                    //precisa nem procurar mais.
                    break;
                } else if (pedraDaMao.temNumero(numeroDireita)){
                    //opa, achei uma. vai ser ela mesmo.
                    pedraPraJogar = pedraDaMao;
                    //jogar na direita, que foi o que eu vi que tenho o número.
                    ladoDeJogar = Lado.DIREITO;
                    //precisa nem procurar mais.
                    break;
                }
            }
        }

        if(pedraPraJogar == null){ 
            //lasquei-me. não achei nenhuma. toc toc
            jogada = Jogada.TOQUE;
        } else {
            //pronto, vou tirar essa pedra da minha mão, pra não jogar duplicada 
            //depois.
            mao.remove(pedraPraJogar);
            //criando a jogada: a pedra que escolhi e a cabeça em que vou 
            //colocar ela.
            jogada = Jogada.jogada(pedraPraJogar,ladoDeJogar);
        }
        //retornando a jogada
        return jogada; 
    }

    /**
     * Não sei se comece... não sei se não comece... vai 5 mesmo.
     * 
     * @return 5
     */
    @Override
    public int vontadeDeComecar() {
        //isso é pra eu me ligar que a primeira rodada já foi.
        this.perguntouSeEuQueriaJogar = true; 
        //vai 5 mesmo
        return 5;
    }
}
