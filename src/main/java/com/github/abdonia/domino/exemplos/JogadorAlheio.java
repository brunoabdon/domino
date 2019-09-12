/*
 * Copyright (C) 2016 Bruno Abdon
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

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.github.abdonia.domino.Vontade;

/**
 * {@link Jogador} que joga aleatoriamente qualquer {@link Pedra} dentre as 
 * válidas em sua mão no momento.
 * 
 * @author Bruno Abdon
 */
public class JogadorAlheio implements Jogador {

    private static final Collector<Pedra, ?, List<Pedra>> TO_LIST_COLLECTOR = 
        Collectors.toList();

    private static final Random SORTE = new Random(2604L);
    private static final Vontade VONTADES[] = Vontade.values(); //cache....

    private Mesa mesa;
    private List<Pedra> mao;
    
    private boolean perguntouSeEuQueriaJogar;

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.mesa = mesa;
    }
    
    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {

        this.mao = new ArrayList<>(6);
        this.mao.add(pedra1);
        this.mao.add(pedra2);
        this.mao.add(pedra3);
        this.mao.add(pedra4);
        this.mao.add(pedra5);
        this.mao.add(pedra6);
        this.perguntouSeEuQueriaJogar = false;
    }

    @Override
    public Jogada joga() {
        return  
            mesa.getPedras().isEmpty()
                ? primeiraJogada()
                : jogadaNormal();
    }

    private Jogada jogadaNormal() { 
        final Jogada jogada;
        
        final Numero numeroEsquerda = mesa.getNumeroEsquerda();
        final Numero numeroDireita = mesa.getNumeroDireita();
        
        final List<Pedra> jogaveis =
            this.mao
                .stream()
                .filter(
                    p -> p.temNumero(numeroEsquerda)
                    || p.temNumero(numeroDireita))
                .collect(TO_LIST_COLLECTOR);

        if(jogaveis.isEmpty()){
            jogada = Jogada.TOQUE;

        } else {
            final Pedra pedra = jogaveis.get(SORTE.nextInt(jogaveis.size()));
            
            final Lado lado = 
                    pedra.temNumero(numeroEsquerda)
                        ? pedra.temNumero(numeroDireita)
                            ? SORTE.nextBoolean()
                                ? Lado.ESQUERDO
                                : Lado.DIREITO
                            : Lado.ESQUERDO
                        : Lado.DIREITO;
            jogada = joga(pedra, lado);
        }
        return jogada;
    }

    private Jogada primeiraJogada() {
        final Pedra pedra =
                perguntouSeEuQueriaJogar
                ? this.mao.get(SORTE.nextInt(6))
                : JogadorUtils.aMaiorCarroca(this.mao);
        final Lado lado =
                SORTE.nextBoolean()
                ? Lado.ESQUERDO
                : Lado.DIREITO;

        return joga(pedra,lado);
    }

    
    
    /**
     * Não sei o quanto eu quero. Vou chutar.
     * @return Uma {@link Vontade} aleatória.
     */
    @Override
    public Vontade getVontadeDeComecar() {
        //isso é pra eu me ligar que a primeira rodada já foi.
        this.perguntouSeEuQueriaJogar = true; 
        return VONTADES[SORTE.nextInt(5)];
    }

    private Jogada joga(final Pedra pedra, final Lado lado) {
        this.mao.remove(pedra);
        return Jogada.de(pedra,lado);
    }
}
