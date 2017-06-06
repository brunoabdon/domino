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
import org.apache.commons.lang3.Validate;

/**
 * Uma de que um {@link Jogador} decidiu fazer. Ela diz qual {@link Pedra}
 * ele vai jogar e em que {@linkplain Lado lado} da {@linkplain Mesa mesa} 
 * jogar.
 *
 * <p>Quando o jogador não tem nenhuma peça possível de jogar, ele deve tocar, 
 * retornando {@link #TOQUE} (retornar {@code null} não é permitido).
 * 
 * <p>Essa classe é imutável e {@code final}. Suas instâncias são adquiridas pelo
 * método estático {@link #de(Pedra, Lado)}.</p>
 * 
 * @author Bruno Abdon
 */
public final class Jogada {

    private static final String ERR_LADO_NULO = "Lado 'null' numa jogada";
    private static final String ERR_PEDRA_NULA = "Pedra 'null' numa jogada";

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
                pedra -> {return new Jogada(pedra,lado);}, 
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
     * @param t A classe dos valores do mapa.
     * 
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
     * Retorna a Jogada de uma determinada {@link Pedra} em um determinado 
     * {@link Lado} da {@link Mesa}. Para tocar, o singleton {@link #TOQUE} deve
     * ser usado.
     * 
     * <p>Como existem apenas 57 jogadas possíveis(*), foi decidido esconder o 
     * construtor e fazer com que as instâncias sejam reutilizadas.</p>
     * 
     * <p>(*) As 57 jogadas possíveis são: cada uma das 28 pedras na {@linkplain 
     * Lado#ESQUERDO esquerda}, mais cada uma das 28 pedras na {@linkplain 
     * Lado#DIREITO direita}, mais o {@linkplain #TOQUE toque}.</p>
     * 
     * @param pedra a pedra que quer jogar.
     * @param lado o lado da mesa pra colocar a pedra.
     * 
     * @return Uma jogada da pedra em questão do lado informado.
     * 
     * @throws IllegalArgumentException Se a {@link Pedra} ou o {@link Lado} 
     * for {@code null}.
     */
    public static Jogada de(final Pedra pedra, final Lado lado) {
        Validate.notNull(pedra, ERR_PEDRA_NULA);
        Validate.notNull(lado, ERR_LADO_NULO);
            
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

    /**
     * Retorna a {@link Pedra} desta jogada.
     * @return a {@link Pedra} desta jogada.
     */
    public Pedra getPedra() {
        return pedra;
    }

    /**
     * Retorna o {@link Lado} desta jogada.
     * @return o {@link Lado} desta jogada.
     */
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