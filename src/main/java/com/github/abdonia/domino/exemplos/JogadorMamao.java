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
 * @author bruno
 *
 */
public class JogadorMamao implements Jogador {

    protected Mesa mesa;
    protected List<Pedra> mao;

    boolean perguntouSeEuQueriaJogar;

    @Override
    public void sentaNaMesa(Mesa mesa, int cadeiraQueSentou) {
        this.mesa = mesa;
    }

    @Override
    public void recebeMao(Pedra[] mao) {
        //guardar como uma List
        this.mao = new ArrayList<>(6);
        Collections.addAll(this.mao, mao);
        perguntouSeEuQueriaJogar = false;
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
                pedraPraJogar = aMaiorCarroca();
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
            //a mesa nao tah vazia. o jogo tah rolando. tenho que jogar uma peca
            //que se encaixe ou no lado esquerdo ou no direito dos dominos.

            //deixa eu ver quais sao os numeros
            final Numero numeroEsquerda = mesa.getNumeroEsquerda();
            final Numero numeroDireita = mesa.getNumeroDireita();
            //hum, otimo. agora deixa achar alguma pedra na minha mao
            //que tenha algum dessezs numeroos.
            for (final Pedra pedraDaMao : mao) {
                if(pedraDaMao.temNumero(numeroEsquerda)){
                    //opa, achei uma. vai ser ela mesmo.
                    pedraPraJogar = pedraDaMao;
                    //jogar na esquerda, que foi o que eu vi que tenho o número.
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
            //lasquei-me. nao achei nenhuma. toc toc
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
     * Retorna qual é a maior {@link Pedra#isCarroca() carroça} que eu 
     * tenho na mão. Tem que ser essa {@link Pedra} pra jogar quando 
     * sou o primeiro a jogar numa primeira partida.
     *  
     * @TODO usar streams
     * @return Uma carroça.
     */
    private Pedra aMaiorCarroca() {
        Pedra maiorCarroca = null;
        for(Pedra pedra : mao) {
            if(pedra.isCarroca()) {
                if(maiorCarroca == null 
                    || (pedra.compareTo(maiorCarroca) >= 1)){
                    maiorCarroca = pedra;
                }
            }
        }
        return maiorCarroca;
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
