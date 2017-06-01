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

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;

/**
 * Implementação mais simples possível de um {@link Jogador}.
 * 
 * @author Bruno Abdon
 */
public class JogadorSimplorio implements Jogador{

    private Mesa mesa;
    private Pedra[] pedras;
    
    private boolean jaJogueiAlgumaVez;
    
    /**
     * Vai guardar a referência pra a {@link Mesa} pra usar na hora de 
     * {@linkplain #joga() jogar}.
     * 
     * @param mesa A mesa do jogo.
     * @param cadeiraQueSentou a cadeira em que sentei (1, 2, 3 ou 4). Vou 
     * ignorar esse parâmetro.
     * 
     */
    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.mesa = mesa;
        this.jaJogueiAlgumaVez = false;
    }

    /**
     * Recebo as 6 {@linkplain Pedra pedras} no início de cada partida.
     * @param pedra1 A primeira {@linkplain Pedra pedra} da mão.
     * @param pedra2 A segunda {@linkplain Pedra pedra} da mão.
     * @param pedra3 A terceira {@linkplain Pedra pedra} da mão.
     * @param pedra4 A quarta {@linkplain Pedra pedra} da mão.
     * @param pedra5 A quinta {@linkplain Pedra pedra} da mão.
     * @param pedra6 A última {@linkplain Pedra pedra} da mão.
     */
    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        this.pedras = new Pedra[]{pedra1,pedra2,pedra3,pedra4,pedra5,pedra6};
    }

    /**
     * Vou dizer quais de minhas {@linkplain Pedra pedras} vou querer jogar.
     * 
     * <p>Se for a primeira jogada do jogo inteiro, então tenho que jogar 
     * obrigatoriamente a {@link Pedra#CARROCA_DE_SENA carroça de senna}. Se não
     * tiver, tem que ser a {@link Pedra#CARROCA_DE_QUINA carroça de quina}. Se
     * não, a de {@linkplain Pedra#CARROCA_DE_QUINA quadra}.</p>
     * 
     * <p>Se não for qualquer outra jogada além da primeira do jogo, vou jogar
     * alguma das minhas pedras que caibam na {@linkplain Mesa mesa} ou, se não
     * tiver nenhuma pedra, vou {@linkplain Jogada#TOQUE tocar}.</p>
     * 
     * @return A {@link Jogada} (qual  pedra em que lado da mesa) que eu decidi 
     * jogar.
     */
    @Override
    public Jogada joga() {
        final Jogada jogada;

        if(mesa.taVazia()){
            //é o comeco de uma partida e eu sou o primeiro.
            if(!jaJogueiAlgumaVez){
                //é a primeira partida do jogo. devo jogar  maior carroca.
                //(provavelmente a carroça de senna).
                jogada = jogaMaiorCarroca();
            } else {
                //poço começar a partida com qualquer pedra. vai ser a primeira.
                jogada = jogarAPrimeiraPedraDaMao();
            }
        } else {
            //vou jogar uma peça qualquer que caiba na mesa.
            jogada = jogaAlgumaPedraQueCaibaNaMesa();
        }
        //já joguei pelo menos uma vez. não preciso mais me preocupar em jogar a
        //maior carroça.
        jaJogueiAlgumaVez = true;
        return jogada;
    }

    
    /**
     * Retorna uma {@link Jogada} com a primeira {@link Pedra} que eu tiver na 
     * mão, de um {@link Lado} qualquer. A pedra jogada será removida da 
     * {@linkplain #pedras mão}, pois não poderá ser jogada de novo.
     * 
     * @return Uma {@link Jogada}.
     */
    private Jogada jogarAPrimeiraPedraDaMao(){
        final Pedra pedra = pedras[0]; //a primeira pedra da mão.
        pedras[0] = null; //tirando a pedra da mão.
        
        final Lado lado = Lado.ESQUERDO; //qualquer lado... a mesa está vazia.
        
        final Jogada jogada = Jogada.jogada(pedra, lado); 
        
        return jogada;
    }
    
    /**
     * Retorna uma {@link Jogada} com a maior carroça que tem na minha mão.
     * @return uma {@link Jogada} com a maior carroça que tem na minha mão.
     */
    private Jogada jogaMaiorCarroca() {
        Pedra maiorCarroca = null; //a maior até agora (nenhuma)
        int indiceMaioCarroca = -1; //onde essa pedra está na mão
        //procurando a carroça entre as pedras....
        for (int i = 0; i<pedras.length;i++) {
            final Pedra pedra = pedras[i];
            if(pedra.isCarroca()){  //precisa ser carroca
                //se eh a primeira carroca (mariorCarroca==null) ou
                //se eh uma maior do que a anterior, ela é a maior agora.
                if(maiorCarroca == null                     
                    || pedra.compareTo(maiorCarroca) > 0){
                    maiorCarroca = pedra;
                    indiceMaioCarroca = i;
                }
            }
        }
        
        //retirando a pedra da mao
        pedras[indiceMaioCarroca] = null;
        //retornando uma jogada com a maior carroça. o Lado é irrelevante.
        return Jogada.jogada(maiorCarroca, Lado.ESQUERDO);
    }

    /**
     * Retorna uma {@link Jogada} com alguma das minhas {@linkplain Pedra 
     * pedras} que caibam na {@linkplain Mesa mesa}, ou retorna um {@link 
     * Jogada#TOQUE} caso eu não tenha nenhuma pedra que sirva pa jogar.
     * 
     * @return Uma jogada válida.
     */
    private Jogada jogaAlgumaPedraQueCaibaNaMesa() {
        Jogada jogada = null;
        //procurando entre as pedras....
        for (int i = 0; i<pedras.length;i++) {
            final Pedra pedra = pedras[i];
            if(pedra != null){ //ainda não joguei essa pedra
                if(pedra.temNumero(mesa.getNumeroEsquerda())){
                    //a pedra cabe do lado esquerdo da mesa. vou jogar ela lá.
                    jogada = Jogada.jogada(pedra,Lado.ESQUERDO);
                    pedras[i] = null; //removendo ela da minha mão.
                    break; //não precisa procurar mais.
                } else if(pedra.temNumero(mesa.getNumeroDireita())){
                    //a pedra cabe do lado direito da mesa. vou jogar ela lá.
                    jogada = Jogada.jogada(pedra,Lado.DIREITO);
                    pedras[i] = null; //removendo ela da minha mão.
                    break; //não precisa procurar mais.
                } 
            }
        }
        
        //se nao encontrei nenhuma pedra jogável, tenho que tocar.
        if(jogada == null) jogada = Jogada.TOQUE;
        
        return jogada;
    }
    
    /**
     * Pra mim, tanto faz começar ou não.
     * @return {@link Vontade#TANTO_FAZ}.
     */
    @Override
    public Vontade vontadeDeComecar() {
        return Vontade.TANTO_FAZ;
    }
}
