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

/**
 * Cada uma das 28 pedras de uma dominó.
 * @author bruno
 */
public enum Pedra {

    /**
     * <span style="font-size:2em">\uD83C\uDC63</span>
     */
    CARROCA_DE_LIMPO            (Numero.LIMPO), 

    //1

    /**
     * <span style="font-size:2em">\uD83C\uDC64</span>	
     */
    LIMPO_PIO       (Numero.LIMPO, Numero.PIO),

    //2

    /**
     * <span style="font-size:2em">\uD83C\uDC6B</span>
     */
    CARROCA_DE_PIO                (Numero.PIO),

    /**
     * <span style="font-size:2em">\uD83C\uDC65</span>
     */
    LIMPO_DUQUE   (Numero.LIMPO, Numero.DUQUE),

    //3

    /**
     * <span style="font-size:2em">\uD83C\uDC66</span>
     */
    LIMPO_TERNO   (Numero.LIMPO, Numero.TERNO),

    /**
     * <span style="font-size:2em">\uD83C\uDC6C</span>
     */
    PIO_DUQUE       (Numero.PIO, Numero.DUQUE),

    //4

    /**
     * <span style="font-size:2em">\uD83C\uDC67</span>
     */
    LIMPO_QUADRA (Numero.LIMPO, Numero.QUADRA),

    /**
     * <span style="font-size:2em">\uD83C\uDC6D</span>
     */
    PIO_TERNO       (Numero.PIO, Numero.TERNO),

    /**
     * <span style="font-size:2em">\uD83C\uDC73</span>
     */
    CARROCA_DE_DUQUE            (Numero.DUQUE),

    //5

    /**
     * <span style="font-size:2em">\uD83C\uDC68</span>
     */
    LIMPO_QUINA   (Numero.LIMPO, Numero.QUINA),

    /**
     * <span style="font-size:2em">\uD83C\uDC6E</span>
     */
    PIO_QUADRA     (Numero.PIO, Numero.QUADRA),

    /**
     * <span style="font-size:2em">\uD83C\uDC74</span>
     */
    DUQUE_TERNO   (Numero.DUQUE, Numero.TERNO),

    //6

    /**
     * <span style="font-size:2em">\uD83C\uDC69</span>
     */
    LIMPO_SENA     (Numero.LIMPO, Numero.SENA),

    /**
     * <span style="font-size:2em">\uD83C\uDC6F</span>
     */
    PIO_QUINA       (Numero.PIO, Numero.QUINA),

    /**
     * <span style="font-size:2em">\uD83C\uDC75</span>
     */
    DUQUE_QUADRA (Numero.DUQUE, Numero.QUADRA),

    /**
     * <span style="font-size:2em">\uD83C\uDC7B</span>
     */
    CARROCA_DE_TERNO            (Numero.TERNO),

    //7

    /**
     * <span style="font-size:2em">\uD83C\uDC70</span>
     */
    PIO_SENA         (Numero.PIO, Numero.SENA),

    /**
     * <span style="font-size:2em">\uD83C\uDC76</span>
     */
    DUQUE_QUINA   (Numero.DUQUE, Numero.QUINA),

    /**
     * <span style="font-size:2em">\uD83C\uDC7C</span>
     */
    TERNO_QUADRA (Numero.TERNO, Numero.QUADRA),

    //8

    /**
     * <span style="font-size:2em">\uD83C\uDC77</span>
     */
    DUQUE_SENA     (Numero.DUQUE, Numero.SENA),

    /**
     * <span style="font-size:2em">\uD83C\uDC7D</span>
     */
    TERNO_QUINA   (Numero.TERNO, Numero.QUINA),

    /**
     * <span style="font-size:2em">\uD83C\uDC83</span>
     */
    CARROCA_DE_QUADRA          (Numero.QUADRA),

    //9

    /**
     * <span style="font-size:2em">\uD83C\uDC7E</span>
     */
    TERNO_SENA     (Numero.TERNO, Numero.SENA),

    /**
     * <span style="font-size:2em">\uD83C\uDC84</span>
     */
    QUADRA_QUINA (Numero.QUADRA, Numero.QUINA),

    //10

    /**
     * <span style="font-size:2em">\uD83C\uDC85</span>
     */
    QUADRA_SENA   (Numero.QUADRA, Numero.SENA),

    /**
     * <span style="font-size:2em">\uD83C\uDC8B</span>
     */
    CARROCA_DE_QUINA            (Numero.QUINA),

    //11

    /**
     * <span style="font-size:2em">\uD83C\uDC8C</span>
     */
    QUINA_SENA     (Numero.QUINA, Numero.SENA),

    //12

    /**
     * <span style="font-size:2em">\uD83C\uDC93</span>
     */
    CARROCA_DE_SENA              (Numero.SENA);

    /**
     * Uma array auxliar, contendo só as carroças, em ordem 
     * crescente (de {@link #CARROCA_DE_LIMPO limpo} a 
     * {@link #CARROCA_DE_SENA sena}): <code>
     * {\uD83C\uDC63,\uD83C\uDC6B,\uD83C\uDC73,\uD83C\uDC7B,\uD83C\uDC83,\uD83C\uDC8B,\uD83C\uDC93}</code>.
     */
    public static final Pedra[] carrocas = 
        new Pedra[]{
            CARROCA_DE_LIMPO, 
            CARROCA_DE_PIO, 
            CARROCA_DE_DUQUE, 
            CARROCA_DE_TERNO, 
            CARROCA_DE_QUADRA, 
            CARROCA_DE_QUINA, 
            CARROCA_DE_SENA
    };

    private final Numero primeiroNumero;
    private final Numero segundoNumero;

    private final int numeroDePontos;

    private Pedra(final Numero primeiroNumero, final Numero secundoNumero){
        this.primeiroNumero = primeiroNumero;
        this.segundoNumero = secundoNumero;
        this.numeroDePontos = 
            primeiroNumero.getNumeroDePontos() 
            + segundoNumero.getNumeroDePontos();
    }

    private Pedra(final Numero numeroDaCarroca){
        this(numeroDaCarroca,numeroDaCarroca);
    }

    /**
     * O menor {@link Numero}dessa pedra. (Ou o {@link Numero} repetido, caso
     * seja uma carroça).
     * @return O menor número dessa pedra.
     */
    public Numero getPrimeiroNumero() {
        return primeiroNumero;
    }

    /**
     * O maior {@link Numero}dessa pedra. (Ou o {@link Numero} repetido, caso
     * seja uma carroça).
     * @return O menor número dessa pedra.
     */
    public Numero getSegundoNumero() {
        return segundoNumero;
    }

    /**
     * A soma dos dois {@link Numero}s. Usado quando tranca e tem que
     * contar os pontos na mão.
     * @return A soma dos dois números;
     */
    public int getNumeroDePontos() {
        return this.numeroDePontos;
    }

    /**
     * Método auxiliar que diz se um dos dois {@link Numero}s dessa 
     * pedra é o número dado como parâmentro.
     * 
     * @param numero Um {@link Numero}, pra testar se essa pedra tem ele.
     * @return <code>true</code> só se um dos dois {@link Numero}s dessa pedra
     * for o dado como parâmetro.
     */
    public boolean temNumero(final Numero numero){
        return numero == primeiroNumero || numero == segundoNumero; 
    }

    /**
     * Diz se essa pedra é uma carroça. Ou seja, se o {@link 
     * #getPrimeiroNumero() primeiro número} é igual ao {@link 
     * #getSegundoNumero() segundo}.
     * 
     * @return Se a predra é ou não carroça.
     */
    public boolean isCarroca(){
        return this.primeiroNumero == this.segundoNumero;
    }

    @Override
    public String toString() {
        return "[" + primeiroNumero + "|" + segundoNumero + "]";
    }
}