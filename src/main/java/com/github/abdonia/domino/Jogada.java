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
package com.github.abdonia.domino;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Uma jogada que um {@link Jogador} decidiu fazer. Ela diz qual {@link Pedra}
 * ele vai jogar e, caso seja necessário, em que cabeça jogar (obrigatório
 * apenas quando a pedra se encaixa nas duas cabeças da mesa e nao é uma
 * carroça).
 *
 * Quando o jogador toca, deve usar a jogada singleton {@link #TOQUE} (não deve
 * usar <code>null</code>, por exemplo).
 *
 * @author bruno
 *
 */
public final class Jogada {

    private final Pedra pedra;
    private final Lado lado;

    /**
     * A jogada singleton que um {@link Jogador} deve retornar quando vai tocar.
     */
    public static final Jogada TOQUE = new Jogada(null,null);

    /**
     * Um cache com todas as 56 jogadas possiveis criadas.
     */
    private static final EnumMap<Lado,EnumMap<Pedra,Jogada>> JOGADAS = 
        funcToMap (
            lado->funcToMap(
                pedra-> {return new Jogada(pedra,lado);}, 
                Pedra.class), 
            Lado.class
        );
    
    /**
     * Método auxiliar que cria um {@link EnumMap} a partir de uma {@link 
     * Function} que mapeia o {@link Enum} em alguma coisa.
     * 
     * @param <T> O tipo do valor do EnumMap
     * @param <E> O tipo do Enum que sera chave do EnumMap
     * @param f A função que determina qual o valor de cada chave
     * @param t A classe do Enum 
     * @param values os valores do enum que devem aparecer no EnumMap
     * @return Um EnumMap que replica uma Function.
     */
    private static <T,E extends Enum<E>> EnumMap<E,T> funcToMap(
        final Function<E,T> f, final Class<E> t) {
        
            return Stream.of(t.getEnumConstants())
                   .collect(
                        Collectors.toMap(
                            Function.identity(), 
                            f, 
                            (x,y)-> x, 
                            ()-> new EnumMap<>(t)));
    }

    /**
     * A retorna a Jogada de uma determinada {@link Pedra} em um determinado 
     * {@link Lado} da {@link Mesa}.
     * 
     * Para tocar, o singleton {@link #TOQUE} deve ser usado.
     * 
     * @param pedra a pedra que quer jogar.
     * @param lado o lado da mesa pra colocar a pedra.
     * 
     * @return Uma jogada da pedra em questão do lado informado, ou 
     * <code>null</code>, caso um dos parametros seja nulo.
     * 
     * @throws IllegalArgumentException Se a {@link Pedra} ou o {@link Lado} 
     * for <code>null</code>.
     */
    public static Jogada jogada(final Pedra pedra, final Lado lado) {
        if(pedra == null || lado == null) 
            throw new IllegalArgumentException(
                "Nem a Pedra nem o Lado podem ser nulos numa jogada");
            
        return  JOGADAS.get(lado).get(pedra);
    }

    /**
     * Constroi uma jogada normal.
     *
     * @param pedra a pedra que quer jogar.
     * @param lado o lado da mesa pra colocar a pedra.
     */
    private Jogada(final Pedra pedra, final Lado lado) {
        this.pedra = pedra;
        this.lado = lado;
    }

    public Pedra getPedra() {
        return pedra;
    }

    public Lado getLado() {
        return lado;
    }

    @Override
    public String toString() {
        return this == TOQUE ? 
                "toc toc" 
                : (this.pedra + "(" + this.lado + ")");
    }
}
