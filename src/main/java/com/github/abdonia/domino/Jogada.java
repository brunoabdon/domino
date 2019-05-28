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
 * Uma jogada, por um um {@link Jogador}, de uma {@link Pedra} em  um 
 * {@linkplain Lado lado} da {@linkplain Mesa mesa}.
 *
 * <p>Quando o jogador não tem nenhuma peça possível de jogar, ele deve tocar, 
 * retornando {@link #TOQUE} (retornar {@code null} é considerado um erro).</p>
 * 
 * <p>Essa classe é imutável e {@code final}. Suas instâncias são adquiridas 
 * pelo método estático {@link #de(Pedra, Lado)}.</p>
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
            lado -> funcToMap(pedra -> new Jogada(pedra,lado), Pedra.class), 
            Lado.class
        );
    
    /**
     * Método auxiliar que cria um {@link EnumMap} a partir de uma {@link 
     * Function} que mapeia o {@link Enum} em alguma coisa.
     * 
     * @param <T> O tipo do valor do {@link EnumMap}.
     * @param <E> O tipo do enum que sera chave do {@link EnumMap}.
     * @param mapperFunction A {@linkplain função} que vai ser refletida num 
     * mapa.
     * @param enumClass A classe dos valores do {@link EnumMap}.
     * 
     * @return Um {@link EnumMap} que replica uma {@link Function}.
     */
    private static <T,E extends Enum<E>> EnumMap<E,T> funcToMap(
        final Function<E,T> mapperFunction, 
        final Class<E> enumClass) {

        return 
            Stream.of(enumClass.getEnumConstants()).collect(
                Collectors.toMap(
                    Function.identity(), 
                    mapperFunction, 
                    (x,y)-> x, 
                    ()-> new EnumMap<>(enumClass)
                )
            );
    }

    /**
     * Retorna a Jogada de uma determinada {@link Pedra} em um determinado 
     * {@link Lado} da {@linkplain Mesa mesa}. Para tocar, o singleton {@link 
     * #TOQUE} deve ser usado.
     * 
     * <p>Como existem apenas 57 jogadas possíveis(*), foi decidido esconder o 
     * construtor e fazer com que as instâncias sejam reutilizadas.</p>
     * 
     * <p>(*) As 57 jogadas possíveis são: cada uma das 28 pedras na 
     * {@linkplain Lado#ESQUERDO esquerda}, mais cada uma das 28 pedras na 
     * {@linkplain Lado#DIREITO direita}, mais o {@linkplain #TOQUE toque}.</p>
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
        notNull(pedra, ERR_PEDRA_NULA);
        notNull(lado, ERR_LADO_NULO);

        return  JOGADAS.get(lado).get(pedra);
    }

    /**
     * Lança {@link IllegalArgumentException} com a mensagem dada se o 
     * argumento passado seja {@code null}. 
     * @param o Um objeto.
     * @param err A mensagem usada no {@link IllegalArgumentException} caso o
     * objeto seja {@code null}.
     * @throws IllegalArgumentException caso o primeiro parâmetro seja null.
     */
    private static void notNull(final Object o, final String err) {
        if(o == null) throw new IllegalArgumentException(err);
        
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
