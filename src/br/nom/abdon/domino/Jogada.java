package br.nom.abdon.domino;

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
    public static final Jogada TOQUE = new Jogada();

    /**
     * Um cache com todas as 56 jogadas possiveis criadas.
     */
    private static final EnumMap<Lado,EnumMap<Pedra,Jogada>> jogadas = 
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
     * @return 
     */
    private static <T,E extends Enum<E>> EnumMap<E,T> funcToMap(
        Function<E,T> f, Class<E> t) {
        
            return Stream.of(t.getEnumConstants())
                   .collect(
                        Collectors.toMap(
                            Function.identity(), 
                            f, 
                            (x,y)->x, 
                            ()-> new EnumMap<>(t)));
}
    
    
    
    /**
     * A jogada de uma determinada {@link Pedra}, em qualquer lado da 
     * {@link Mesa}. Um lado será escolhido de maneira opaca.
     *
     * @param pedra a pedra que se quer jogar.
     * @return Uma jogada da pedra dada de um dos lado da mesa.
     */
    public static final Jogada joga(Pedra pedra) {
        return joga(pedra, Lado.ESQUERDO);
    }

    /**
     * A retorna a jogada de uma determinada {@link Pedra} em um determinado 
     * {@link Lado} da {@link Mesa}.
     * 
     * Se a {@link Pedra} ou o {@link Lado} forem nulos, retorna uma jogada nula
     * silenciosamente (que provavelmente vai dar problema pro Jogador depois).
     * 
     * Se for muito importante realizar uma jogada onde não importa o Lado, use 
     * o método {@link #joga(br.nom.abdon.domino.Pedra)} deve ser usado.
     * 
     * Para tocar, o singleton {@link #TOQUE} deve ser usado.
     * 
     * @param pedra a pedra que quer jogar.
     * @param lado o lado da mesa pra colocar a pedra.
     * 
     * @return Uma jogada da pedra em questão do lado informado, ou 
     * <code>null</code>, caso um dos parametros seja nulo.
     * 
     */
    public static Jogada joga(Pedra pedra, Lado lado) {
        return jogadas.get(lado).get(pedra);
    }
    
    /**
     * Constroi uma jogada normal.
     *
     * @param pedra a pedra que quer jogar.
     * @param lado o lado da mesa pra colocar a pedra.
     */
    private Jogada(Pedra pedra, Lado lado) {
        this.pedra = pedra;
        this.lado = lado;
    }

    /**
     * Usado apenas pra contruir o singleton do {@link #TOQUE}, que é uma Logada
     * sem pedra nem lado.
     * 
     */
    private Jogada() {
        this.pedra = null;
        this.lado = null;
    }

    public Pedra getPedra() {
        return pedra;
    }

    public Lado getLado() {
        return lado;
    }

    @Override
    public String toString() {
        return this == TOQUE ? ""
                + "toc toc" 
                : (this.pedra.toString() + "(" + this.lado + ")");
    }
}
