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

    CARROCA_DE_LIMPO            (Numero.LIMPO), 

    //1
    LIMPO_PIO       (Numero.LIMPO, Numero.PIO),

    //2
    CARROCA_DE_PIO                (Numero.PIO),
    LIMPO_DUQUE   (Numero.LIMPO, Numero.DUQUE),

    //3
    LIMPO_TERNO   (Numero.LIMPO, Numero.TERNO),
    PIO_DUQUE       (Numero.PIO, Numero.DUQUE),

    //4
    LIMPO_QUADRA (Numero.LIMPO, Numero.QUADRA),
    PIO_TERNO       (Numero.PIO, Numero.TERNO),
    CARROCA_DE_DUQUE            (Numero.DUQUE),

    //5
    LIMPO_QUINA   (Numero.LIMPO, Numero.QUINA),
    PIO_QUADRA     (Numero.PIO, Numero.QUADRA),
    DUQUE_TERNO   (Numero.DUQUE, Numero.TERNO),

    //6
    LIMPO_SENA     (Numero.LIMPO, Numero.SENA),
    PIO_QUINA       (Numero.PIO, Numero.QUINA),
    DUQUE_QUADRA (Numero.DUQUE, Numero.QUADRA),
    CARROCA_DE_TERNO            (Numero.TERNO),

    //7
    PIO_SENA         (Numero.PIO, Numero.SENA),
    DUQUE_QUINA   (Numero.DUQUE, Numero.QUINA),
    TERNO_QUADRA (Numero.TERNO, Numero.QUADRA),

    //8
    DUQUE_SENA     (Numero.DUQUE, Numero.SENA),
    TERNO_QUINA   (Numero.TERNO, Numero.QUINA),
    CARROCA_DE_QUADRA          (Numero.QUADRA),

    //9
    TERNO_SENA     (Numero.TERNO, Numero.SENA),
    QUADRA_QUINA (Numero.QUADRA, Numero.QUINA),

    //10
    QUADRA_SENA   (Numero.QUADRA, Numero.SENA),
    CARROCA_DE_QUINA            (Numero.QUINA),

    //11
    QUINA_SENA     (Numero.QUINA, Numero.SENA),

    //12
    CARROCA_DE_SENA              (Numero.SENA);

    /**
     * Uma array auxliar, contendo só as carroças, em ordem 
     * crescente (de {@link Numero#LIMPO limpo} a 
     * {@link Numero#SENA sena})
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
     * Diz se essa pedra é uma carroça.
     * @return se a predra é ou não carroça.
     */
    public boolean isCarroca(){
        return this.primeiroNumero == this.segundoNumero;
    }

    @Override
    public String toString() {
        return "[" + primeiroNumero + "|" + segundoNumero + "]";
    }
}