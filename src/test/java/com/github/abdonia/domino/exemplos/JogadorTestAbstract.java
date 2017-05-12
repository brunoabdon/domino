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
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

/**
 *
 * @author Bruno Abdon
 */
public abstract class JogadorTestAbstract {
    
    protected static final Pedra[] NENHUMA_PEDRA = {};

    protected static Mesa DUMMY_MESA_VAZIA = new Mesa() {
        @Override
        public Numero getNumeroEsquerda() {
            return null;
        }

        @Override
        public Numero getNumeroDireita() {
            return null;
        }

        @Override
        public Iterator<Pedra> iteratorEsquedaPraDireita() {
            return Collections.emptyIterator();
        }

        @Override
        public Iterator<Pedra> iteratorDireitaPraEsquerda() {
            return Collections.emptyIterator();
        }

        @Override
        public int quantasPedrasOJogadoresTem(int qualJogador) {
            return 6;
        }

        @Override
        public Pedra[] toArray() {
            return NENHUMA_PEDRA;
        }
    };
    
    public JogadorTestAbstract() {
    }

    protected void testVontadeDeComecar(final Jogador jogador) {
        System.out.println("vontadeDeComecar");
        int vontade = jogador.vontadeDeComecar();
        assertTrue(vontade >= 0 && vontade <= 10);
    }
    
    protected void testaComecoDePartida(final Jogador jogador){
        jogador.sentaNaMesa(DUMMY_MESA_VAZIA, 1);
        
        jogador.recebeMao(
            new Pedra[]{Pedra.DUQUE_SENA, 
                Pedra.LIMPO_QUADRA, 
                Pedra.CARROCA_DE_LIMPO, 
                Pedra.CARROCA_DE_PIO, 
                Pedra.CARROCA_DE_DUQUE, 
                Pedra.CARROCA_DE_TERNO});

        jogador.vontadeDeComecar();
        Jogada jogada = jogador.joga();
        assertNotNull("Jogada nula",jogada);
        assertNotEquals(jogada,Jogada.TOQUE);
    }
    protected  void testaMaiorCarroca(final Jogador jogador){
        jogador.sentaNaMesa(DUMMY_MESA_VAZIA, 1);
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUADRA,new Pedra[]{Pedra.TERNO_SENA,Pedra.PIO_TERNO,Pedra.CARROCA_DE_QUADRA,Pedra.PIO_SENA,Pedra.TERNO_QUADRA,Pedra.DUQUE_TERNO});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_DUQUE,new Pedra[]{Pedra.CARROCA_DE_DUQUE,Pedra.QUINA_SENA,Pedra.CARROCA_DE_LIMPO,Pedra.PIO_QUADRA,Pedra.TERNO_QUINA,Pedra.LIMPO_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.PIO_DUQUE,Pedra.DUQUE_SENA,Pedra.QUADRA_QUINA,Pedra.LIMPO_QUINA,Pedra.CARROCA_DE_SENA,Pedra.CARROCA_DE_QUINA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_PIO,new Pedra[]{Pedra.DUQUE_QUINA,Pedra.DUQUE_QUADRA,Pedra.CARROCA_DE_PIO,Pedra.LIMPO_SENA,Pedra.LIMPO_PIO,Pedra.QUADRA_SENA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.LIMPO_QUADRA,Pedra.PIO_SENA,Pedra.TERNO_SENA,Pedra.CARROCA_DE_TERNO,Pedra.CARROCA_DE_SENA,Pedra.QUINA_SENA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUADRA,new Pedra[]{Pedra.LIMPO_QUINA,Pedra.TERNO_QUINA,Pedra.CARROCA_DE_QUADRA,Pedra.CARROCA_DE_PIO,Pedra.DUQUE_TERNO,Pedra.CARROCA_DE_DUQUE});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUINA,new Pedra[]{Pedra.CARROCA_DE_QUINA,Pedra.DUQUE_QUINA,Pedra.PIO_TERNO,Pedra.QUADRA_QUINA,Pedra.LIMPO_TERNO,Pedra.DUQUE_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_DUQUE,new Pedra[]{Pedra.DUQUE_SENA,Pedra.CARROCA_DE_DUQUE,Pedra.TERNO_SENA,Pedra.PIO_DUQUE,Pedra.CARROCA_DE_LIMPO,Pedra.LIMPO_DUQUE});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.TERNO_QUADRA,Pedra.CARROCA_DE_TERNO,Pedra.PIO_QUINA,Pedra.QUADRA_SENA,Pedra.CARROCA_DE_SENA,Pedra.LIMPO_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUINA,new Pedra[]{Pedra.DUQUE_TERNO,Pedra.LIMPO_SENA,Pedra.PIO_TERNO,Pedra.QUINA_SENA,Pedra.CARROCA_DE_QUADRA,Pedra.CARROCA_DE_QUINA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_PIO,new Pedra[]{Pedra.PIO_SENA,Pedra.QUADRA_QUINA,Pedra.CARROCA_DE_PIO,Pedra.TERNO_QUINA,Pedra.LIMPO_PIO,Pedra.PIO_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_DUQUE,new Pedra[]{Pedra.DUQUE_SENA,Pedra.QUINA_SENA,Pedra.CARROCA_DE_DUQUE,Pedra.DUQUE_QUINA,Pedra.TERNO_SENA,Pedra.PIO_TERNO});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.CARROCA_DE_LIMPO,Pedra.LIMPO_PIO,Pedra.CARROCA_DE_QUADRA,Pedra.CARROCA_DE_SENA,Pedra.TERNO_QUINA,Pedra.LIMPO_DUQUE});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_DUQUE,new Pedra[]{Pedra.LIMPO_QUINA,Pedra.DUQUE_SENA,Pedra.CARROCA_DE_PIO,Pedra.PIO_QUINA,Pedra.CARROCA_DE_DUQUE,Pedra.TERNO_QUINA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.PIO_QUADRA,Pedra.LIMPO_QUADRA,Pedra.CARROCA_DE_LIMPO,Pedra.DUQUE_QUADRA,Pedra.CARROCA_DE_SENA,Pedra.TERNO_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUINA,new Pedra[]{Pedra.DUQUE_TERNO,Pedra.TERNO_SENA,Pedra.CARROCA_DE_QUINA,Pedra.PIO_SENA,Pedra.LIMPO_TERNO,Pedra.LIMPO_SENA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_TERNO,new Pedra[]{Pedra.CARROCA_DE_TERNO,Pedra.QUINA_SENA,Pedra.QUADRA_SENA,Pedra.PIO_DUQUE,Pedra.QUADRA_QUINA,Pedra.LIMPO_DUQUE});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUINA,new Pedra[]{Pedra.CARROCA_DE_QUINA,Pedra.PIO_QUINA,Pedra.PIO_TERNO,Pedra.DUQUE_SENA,Pedra.LIMPO_TERNO,Pedra.TERNO_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_SENA,new Pedra[]{Pedra.DUQUE_QUINA,Pedra.PIO_SENA,Pedra.CARROCA_DE_DUQUE,Pedra.LIMPO_SENA,Pedra.CARROCA_DE_SENA,Pedra.CARROCA_DE_LIMPO});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_QUADRA,new Pedra[]{Pedra.LIMPO_DUQUE,Pedra.LIMPO_PIO,Pedra.PIO_QUADRA,Pedra.CARROCA_DE_QUADRA,Pedra.PIO_DUQUE,Pedra.QUADRA_QUINA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_TERNO,new Pedra[]{Pedra.CARROCA_DE_PIO,Pedra.LIMPO_QUADRA,Pedra.CARROCA_DE_TERNO,Pedra.DUQUE_TERNO,Pedra.LIMPO_QUINA,Pedra.DUQUE_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_LIMPO,new Pedra[]{Pedra.CARROCA_DE_LIMPO,Pedra.LIMPO_QUADRA,Pedra.QUADRA_QUINA,Pedra.DUQUE_TERNO,Pedra.LIMPO_QUINA,Pedra.DUQUE_QUADRA});
        testaMaiorCarroca(jogador,Pedra.CARROCA_DE_LIMPO,new Pedra[]{Pedra.QUADRA_QUINA,Pedra.QUINA_SENA,Pedra.CARROCA_DE_LIMPO,Pedra.PIO_QUADRA,Pedra.TERNO_QUINA,Pedra.LIMPO_QUADRA});
    }
    
    private void testaMaiorCarroca(final Jogador jogador,final Pedra carroca, final Pedra[] mao){
        jogador.recebeMao(mao);
        Jogada jogada = jogador.joga();
        assertNotNull("Jogada nula",jogada);
        assertSame("Maior carroca era " + carroca,jogada.getPedra(),carroca);
    }
            
    public static void main(String[] args) {
        final List<Pedra> pedras = Arrays.asList(Pedra.values());
        
        final Function<Pedra,String> print = 
                p -> { 
                    final Numero primeiroNumero = p.getPrimeiroNumero();
                    final StringBuilder sb = new StringBuilder("Pedra.");
                    if(p.isCarroca()){
                        sb
                        .append("CARROCA_DE_")
                        .append(primeiroNumero);
                    } else {
                        sb
                        .append(primeiroNumero)
                        .append("_")
                        .append(p.getSegundoNumero());
                    }
                    return sb.toString();
                };


        for (int n = 0; n < 20;) {
            Collections.shuffle(pedras,new Random());
            for (int i = 0; i < 4; i++) {
                final int ini = i*6, fim = ini+6;
                final List<Pedra> mao = pedras.subList(ini, fim);
                if(mao.stream().anyMatch(Pedra::isCarroca)){
                    System.out.println(mao.stream().map(print).collect(Collectors.joining(",","testaMaiorCarroca(jogador,Pedra.,new Pedra[]{","});")));
                    n++;
                }
            }
        }
    }
    
    
}
