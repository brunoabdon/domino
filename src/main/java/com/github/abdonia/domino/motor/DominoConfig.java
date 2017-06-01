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
package com.github.abdonia.domino.motor;

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.eventos.DominoEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.touk.throwing.ThrowingFunction;
import pl.touk.throwing.exception.WrappedException;


/**
 * As configurações de um {@linkplain  Jogo Jogo de dominó}, ou seja, quais 
 * {@link Jogador Jogadores} vão participar (quais são seus nomes e suas 
 * classes) e quais os {@link DominoEventListener listeners} que devem ser 
 * registrados no jogo.
 * 
 * <p>Para um Jogo acontecer, as únicas configurações obrigatórias são os
 * nomes e as classes-ou-instâncias dos 4 jogadores. Listeners} são importantes 
 * pra que se possa saber o que aconteceu no jogo (para logar, salvar ou 
 * animar uma GUI), mas não necessários. A configuração de um {@link 
 * RandomGoddess gerador de aleatoriedade} personalizado é raramente útil 
 * (normalmente, só pra testes).</p>
 * 
 * <p>Um exemplo de <b>configuração mínima</b> é:</p>
 * 
 * <pre>
 * {@code
 *      import com.github.abdonia.domino.exemplos.*;
 *      import com.github.abdonia.domino.log.*;
 *      import com.github.abdonia.domino.motor.*;
 *
 *      //......
 *        
 *      DominoConfig dominoConfig = new DominoConfig();
 *      
 *      //obrigatoriamente 4 jogadores
 *      dominoConfig.setJogador1Dupla1("Bruno",new JogadorMamao());
 *      dominoConfig.setJogador2Dupla1("Amanda",new JogadorMamao());
 *      dominoConfig.setJogador1Dupla2("Marina",new JogadorAlheio());
 *      dominoConfig.setJogador2Dupla2("Paulo",new JogadorAlheio());
 *      
 *      //opcional, mas importante, um listener pra mostrar o andamento do jogo.
 *      dominoConfig.addEventListener(new LoggerDominoEventListener());
 *      
 *      //cria um jogo com essa configuração e joga
 *      Jogo jogo = new Jogo(dominoConfig);
 *      jogo.jogar();
 * }
 * </pre>
 * <p>Tanto para jogadores, listeners e para o gerador de aleatoriedade, é possível
 * escolher entre setar (1) suas instâncias, (2) suas {@linkplain Class classes} 
 * ou o {@linkplain Class#getName() nome qualificado de suas classes}. Por 
 * exemplo, o jogador 1 da dupla 2 pode ser setado das seguinte maneiras:</p>
 * 
 * <pre>
 * {@code 
 *   DominoConfig dominoConfig = new DominoConfig();
 * 
 *   //(1) setando como instância
 *   dominoConfig.setJogador1Dupla2(new com.acme.domino.JogadorEsperto());
 *
 *   //(2) setando a classe
 *   dominoConfig.setJogador1Dupla2(com.acme.domino.JogadorEsperto.class);
 *        
 *   //(3) setando o nome da classe
 *   dominoConfig.setJogador1Dupla2("com.acme.domino.JogadorEsperto");
 * }
 * </pre>
 * <p>Ao setar uma das três opções, as outras opões são automaticamente setadas 
 * pra {@code null}. A não ser no caso dos listeners, onde vários podem ser
 * adicionados usando qualquer uma das formas.</p>
 * 
 * <pre>
 * {@code 
 *    //adicionando listener por classes
 *    dominoConfig.addEventListener(LoggerDominoEventListener.class);
 * 
 *    //adicionando listener por nome da classe
 *    dominoConfig.addEventListener("com.acme.domino.DBSaverEventListener");
 * 
 *    //adicionando listener por instância
 *    dominoConfig.addEventListener(new DominoGUI());
 * }
 * </pre>
 * <p>A instânciação da classe passada na opção <em>(2)</em> ou mesmo a checagem 
 * pela existência da classe cujo nome foi passado na opção <em>(3)</em> não 
 * acontece no momento de chamada dos métods {@code set}. Só ocorrera 
 * {@linkplain Jogo#Jogo(com.github.abdonia.domino.motor.DominoConfig) quando a
 * configuração for usada pra se criar um Jogo}.</p>
 * 
 * @author Bruno Abdon
 */
public class DominoConfig {

    private static final Function<Class, DominoEventListener> INSTN_LIST_KLASS = 
        ThrowingFunction.unchecked(
            k -> DominoConfigUtils.instancia(DominoEventListener.class, k)
        )
    ;
    
    private static final Function<String, DominoEventListener> INSTN_LIST_NAME = 
        ThrowingFunction.unchecked(
            s -> DominoConfigUtils.instancia(DominoEventListener.class, s)
        )
    ;
    
    private final String[] nomesJogadores = new String[4];
    private final String[] nomesClassesJogadores  = new String[4];
    private final Class[] classesJogadores  = new Class[4];
    private final Jogador[] jogadores  = new Jogador[4];

    private String nomeRandomizadora;
    private Class<? extends RandomGoddess> classeRandomizadora;
    private RandomGoddess randomizadora;

    private List<String> nomesEventListeners = new ArrayList<>();
    private List<Class> classesEventListeners = new ArrayList<>();
    private List<DominoEventListener> eventListeners = new ArrayList<>();

    public String getNomeJogador1Dupla1() {
        return this.nomesJogadores[0];
    }

    public void setNomeJogador1Dupla1(final String nomeJogador1Dupla1) {
        this.nomesJogadores[0] = nomeJogador1Dupla1;
    }

    public String getNomeJogador2Dupla1() {
        return this.nomesJogadores[1];
    }

    public void setNomeJogador2Dupla1(final String nomeJogador2Dupla1) {
        this.nomesJogadores[1] = nomeJogador2Dupla1;
    }

    public String getNomeJogador1Dupla2() {
        return this.nomesJogadores[2];
    }

    public void setNomeJogador1Dupla2(final String nomeJogador1Dupla2) {
        this.nomesJogadores[2] = nomeJogador1Dupla2;
    }

    public String getNomeJogador2Dupla2() {
        return this.nomesJogadores[3];
    }

    public void setNomeJogador2Dupla2(final String nomeJogador2Dupla2) {
        this.nomesJogadores[3] = nomeJogador2Dupla2;
    }

    public String getNomeClasseJogador1Dupla1() {
        return nomesClassesJogadores[0];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar o
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador1Dupla1 O nome da classe desse jogador.
     */
    public void setJogador1Dupla1(
            final String nomeClasseJogador1Dupla1) {
        this.nomesClassesJogadores[0] = nomeClasseJogador1Dupla1;
        this.classesJogadores[0] = null;
    }

    public String getNomeClasseJogador2Dupla1() {
        return nomesClassesJogadores[1];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar o
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador2Dupla1 O nome da classe desse jogador.
     */
    public void setJogador2Dupla1(
            final String nomeClasseJogador2Dupla1) {
        this.nomesClassesJogadores[1] = nomeClasseJogador2Dupla1;
        this.classesJogadores[1] = null;
    }

    public String getNomeClasseJogador1Dupla2() {
        return nomesClassesJogadores[2];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar o
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador1Dupla2 O nome da classe desse jogador.
     */
    public void setJogador1Dupla2(
            final String nomeClasseJogador1Dupla2) {
        this.nomesClassesJogadores[2] = nomeClasseJogador1Dupla2;
        this.classesJogadores[2] = null;
    }

    public String getNomeClasseJogador2Dupla2() {
        return nomesClassesJogadores[3];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar o
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador2Dupla2 O nome da classe desse jogador.
     */
    public void setJogador2Dupla2(
            final String nomeClasseJogador2Dupla2) {
        this.nomesClassesJogadores[3] = nomeClasseJogador2Dupla2;
        this.classesJogadores[3] = null;
    }

    public Class<? extends Jogador> getClasseJogador1Dupla1() {
        return classesJogadores[0];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador1Dupla1 a classe do jogador.
     */
    public void setJogador1Dupla1(
            final Class<? extends Jogador> classeJogador1Dupla1) {
        this.classesJogadores[0] = classeJogador1Dupla1;
        this.nomesClassesJogadores[0] = null;
    }

    public Class<? extends Jogador> getClasseJogador2Dupla1() {
        return classesJogadores[1];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador2Dupla1 a classe do jogador.
     */
    public void setJogador2Dupla1(
            final Class<? extends Jogador> classeJogador2Dupla1) {
        this.classesJogadores[1] = classeJogador2Dupla1;
        this.nomesClassesJogadores[1] = null;
    }

    public Class<? extends Jogador> getClasseJogador1Dupla2() {
        return classesJogadores[2];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador1Dupla2 a classe do jogador.
     */
    public void setJogador1Dupla2(
            final Class<? extends Jogador> classeJogador1Dupla2) {
        this.classesJogadores[2] = classeJogador1Dupla2;
        this.nomesClassesJogadores[2] = null;
    }

    public Class<? extends Jogador> getClasseJogador2Dupla2() {
        return classesJogadores[3];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador2Dupla2 a classe do jogador.
     */
    public void setJogador2Dupla2(
            final Class<? extends Jogador> classeJogador2Dupla2) {
        this.classesJogadores[3] = classeJogador2Dupla2;
        this.nomesClassesJogadores[3] = null;
    }
    /**
     * Seta o nome e a classe de um jogador de uma dupla. Ao setar o nome da 
     * classe, a classe ou a instância de um dado jogador, os valores dos outros
     * dois atributos vai a {@code null}.
     * 
     * @param nomeJogador O nome do jogador.
     * @param classeJogador A classe do jogador.
     * @param idxDupla O número da dupla (1 o 2).
     * @param idxJogadorNaDupla O número do jogador na dupla (1 ou 2).
     * 
     * @throws  IllegalArgumentException caso o número da dupla ou do jogador
     * seja algo difernente de 1 e 2.

     */
    public void setJogador(
            final String nomeJogador, 
            final Class<? extends Jogador> classeJogador, 
            final int idxDupla, 
            final int idxJogadorNaDupla){
        this.setNomeEClasseJogador(
                nomeJogador, 
                null, 
                classeJogador, 
                null, 
                idxDupla, 
                idxJogadorNaDupla);
    }    

    /**
     * Seta o nome e o nome da classe de um jogador de uma dupla. Ao setar o 
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos  outros dois atributos vai a {@code null}.
     * 
     * 
     * @param nomeJogador O nome do jogador.
     * @param nomeClasseJogador O nome da classe do jogador.
     * @param idxDupla O número da dupla (1 o 2).
     * @param idxJogadorNaDupla O número do jogador na dupla (1 ou 2).
     * 
     * @throws  IllegalArgumentException caso o número da dupla ou do jogador
     * seja algo difernente de 1 e 2.
     */
    public void setJogador(
            final String nomeJogador, 
            final String nomeClasseJogador, 
            final int idxDupla, 
            final int idxJogadorNaDupla){
        this.setNomeEClasseJogador(
                nomeJogador, 
                nomeClasseJogador, 
                null, 
                null, 
                idxDupla, 
                idxJogadorNaDupla);
    }    

    
 /**
     * Seta o nome e a instância de um jogador de uma dupla. Ao setar o nome da
     * classe, a classe ou a instância de um dado jogador, os valores dos
     * outros dois atributos vai a {@code null}.
     * 
     * @param nomeJogador O nome do jogador.
     * @param jogador O jogador.
     * @param idxDupla O número da dupla (1 o 2).
     * @param idxJogadorNaDupla O número do jogador na dupla (1 ou 2).
     * 
     * @throws  IllegalArgumentException caso o número da dupla ou do jogador
     * seja algo difernente de 1 e 2.
     */
    public void setJogador(
            final String nomeJogador, 
            final Jogador jogador, 
            final int idxDupla, 
            final int idxJogadorNaDupla){
        this.setNomeEClasseJogador(
                nomeJogador, 
                null, 
                null, 
                jogador, 
                idxDupla, 
                idxJogadorNaDupla);
    }        
    /**
     * Método auxiliar que seta ao mesmo tempo o nome, o nome da classe, a 
     * classe e a instância de um dos dois jogadores de uma das duas duplas. 
     * Apenas um entre os parâmetros {@code nomeClasseJogador}, {@code 
     * classeJogador} e {@code jogador} deve ser não nulo (porém, isso não é 
     * verificado).
     * 
     * @param nomeJogador O nome do jogador.
     * @param nomeClasseJogador O nome da classe do jogador.
     * @param classeJogador A classe do jogador.
     * @param jogador A instância do jogador.
     * @param idxDupla O número da dupla (1 o 2).
     * @param idxJogadorNaDupla O número do jogador na dupla (1 ou 2).
     * 
     * @throws  IllegalArgumentException caso o número da dupla ou do jogador
     * seja algo difernente de 1 e 2;
     */
    private void setNomeEClasseJogador(
            final String nomeJogador, 
            final String nomeClasseJogador, 
            final Class<? extends Jogador> classeJogador, 
            final Jogador jogador, 
            final int idxDupla, 
            final int idxJogadorNaDupla){
        
        valida1ou2(idxDupla, "Dupla invalida: %d");
        valida1ou2(idxJogadorNaDupla, "Jogador invalido: %d");
        
        final int index = indexJogador(idxDupla, idxJogadorNaDupla);
        
        nomesJogadores[index] = nomeJogador;
        nomesClassesJogadores[index] = nomeClasseJogador;
        classesJogadores[index] = classeJogador;
        jogadores[index] = jogador;
    }

    private int indexJogador(final int idxDupla, final int idxJogadorNaDupla) {
        return (idxDupla-1)*2 + (idxJogadorNaDupla-1);
    }

    /**
     * Checa se um dado valor é igual a 1 ou 2, lançando uma exceção com uma
     * dada mensagem {@linkplain String#format(String, Object...) parametrizada}
     * (usando "{@code %d}" pra exibir o número inválido) caso o valor não seja.
     * 
     * @param valor o valor a ser checado.
     * @param errorMsg A mensagem parametrizada a ser colocada na exceção, no 
     * caso de erro.
     * 
     * @throws  IllegalArgumentException caso o valor seja algo difernente de 1 
     * e 2;
     */
    private static void valida1ou2(final int valor, final String errorMsg){
        if(valor != 1 && valor != 2){
            throw new IllegalArgumentException(String.format(errorMsg, valor));
        }
    }

    public Jogador getJogador1Dupla1() {
        return jogadores[0];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param jogador1Dupla1 a instância do jogador.
     */
    public void setJogador1Dupla1(final Jogador jogador1Dupla1) {
        this.jogadores[0] = jogador1Dupla1;
        this.nomesClassesJogadores[0] = null;
    }

    public Jogador getJogador2Dupla1() {
        return jogadores[1];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param jogador2Dupla1 a instância do jogador.
     */
    public void setJogador2Dupla1(final Jogador jogador2Dupla1) {
        this.jogadores[1] = jogador2Dupla1;
        this.nomesClassesJogadores[1] = null;
    }

    public Jogador getJogador1Dupla2() {
        return jogadores[2];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param jogador1Dupla2 a instância do jogador.
     */
    public void setJogador1Dupla2(final Jogador jogador1Dupla2) {
        this.jogadores[2] = jogador1Dupla2;
        this.nomesClassesJogadores[2] = null;
    }

    public Jogador getJogador2Dupla2() {
        return jogadores[3];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param jogador2Dupla2  a instância do jogador.
     */
    public void setJogador2Dupla2(final Jogador jogador2Dupla2) {
        this.jogadores[3] = jogador2Dupla2;
        this.nomesClassesJogadores[3] = null;
    }
    
    public List<String> getNomesEventListeners() {
        return this.nomesEventListeners;
    }

    public void setNomesEventListeners(final List<String> nomesEventListeners) {
        this.nomesEventListeners = nomesEventListeners;
    }

    public void addEventListener(final String nomeEventListener) {
        this.nomesEventListeners.add(nomeEventListener);
    }
    
    public List<DominoEventListener> getEventListeners() {
        return this.eventListeners;
    }

    public void setEventListeners(
            final List<DominoEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void addEventListener(final DominoEventListener eventListener) {
        this.eventListeners.add(eventListener);
    }
    
    public List<Class> getClassesEventListeners() {
        return this.classesEventListeners;
    }

    public void setClassesEventListeners(
            final List<Class> classesEventListeners) {
        this.classesEventListeners = classesEventListeners;
    }

    public void addEventListener(
            final Class<? extends DominoEventListener> classeEventListener) {
        this.classesEventListeners.add(classeEventListener);
    }
    
    public String getNomeRandomizadora() {
        return nomeRandomizadora;
    }

    /**
     * Seta, pelo nome completo da classe, qual {@link RandomGoddess} será usado
     * pra gerar os eventos aleatórios de uma partida. É uma configuração 
     * opcional que normalmente só é útil para testes controlados.
     * 
     * @param nomeRandomizadora O nome de uma classe que implementa a interface
     * {@link RandomGoddess}.
     */
    public void setNomeRandomizadora(final String nomeRandomizadora) {
        this.nomeRandomizadora = nomeRandomizadora;
        this.randomizadora = null;
    }
    
    public RandomGoddess getRandomizadora() {
        return randomizadora;
    }

    /**
     * Seta qual {@link RandomGoddess} será usado pra gerar os eventos 
     * aleatórios de uma partida. É uma configuração opcional que normalmente só
     * é útil para testes controlados.
     * 
     * @param randomizadora Uma instância de uma {@link RandomGoddess} .
     */
    public void setRandomizadora(final RandomGoddess randomizadora) {
        this.randomizadora = randomizadora;
        this.nomeRandomizadora = null;
    }

    public Class<? extends RandomGoddess> getClasseRandomizadora() {
        return classeRandomizadora;
    }

    /**
     * Seta qual a classe concreta de {@link RandomGoddess} que será usado pra 
     * gerar os eventos aleatórios de uma partida. A classe deverá ter um 
     * construtor vazio, que será usado ao se instanciar um {@link Jogo}. É uma
     * configuração opcional que normalmente só é útil para testes controlados.
     * 
     * @param classeRandomizadora Uma classe que implementa a interface {@link 
     * RandomGoddess} e possui um construtor público vazio.
     */
    public void setClasseRandomizadora(
            final Class<? extends RandomGoddess> classeRandomizadora) {
        this.classeRandomizadora = classeRandomizadora;
    }
    
    JogadorWrapper makeInstanciaJogador(
        final int idxDupla, 
        final int idxJogadorNaDupla) throws DominoConfigException{
        
        final int index = indexJogador(idxDupla, idxJogadorNaDupla);        
        
        final String nome = pegaNomeJogador(index, idxJogadorNaDupla, idxDupla);
        final Jogador jogador = makeJogador(index, idxJogadorNaDupla, idxDupla);
        
        return new JogadorWrapper(jogador, nome);
    }

    private Jogador makeJogador(
            final int index, 
            final int idxJogadorNaDupla, 
            final int idxDupla) throws DominoConfigException {
        
        Jogador jogador = jogadores[index];
        if(jogador == null){
            final Class klass = classesJogadores[index];
            if(klass == null){
                final String className = nomesClassesJogadores[index];
                if(className == null){
                    throw new DominoConfigException(
                            "O Jogador %d da dupla %d não foi setado.",
                            idxJogadorNaDupla,
                            idxDupla);
                }
                jogador = DominoConfigUtils.instancia(Jogador.class, className);
            } else {
                jogador = DominoConfigUtils.instancia(Jogador.class, klass);
            }
        }
        return jogador;
    }

    private String pegaNomeJogador(
            final int index, 
            final int idxJogadorNaDupla, 
            final int idxDupla) throws DominoConfigException {
        
        final String nome = this.nomesJogadores[index];
        if(nome == null) {
            throw new DominoConfigException(
                    "O nome do Jogador %d da dupla %d não foi setado.",
                    idxJogadorNaDupla,
                    idxDupla);
        }
        return nome;
    }
    
    RandomGoddess makeInstanciaRandomGoddess(
            final Class<? extends RandomGoddess> defaultClass) 
                throws DominoConfigException{
        
        final RandomGoddess randomGoddess;
        if (this.nomeRandomizadora != null) {
            randomGoddess = 
                DominoConfigUtils
                    .instancia(
                        RandomGoddess.class, 
                        this.nomeRandomizadora);
            
        } else if(this.randomizadora != null){
            randomGoddess = this.randomizadora;
            
        } else {
            final Class<? extends RandomGoddess> klass = 
                this.classeRandomizadora != null
                    ? this.classeRandomizadora
                    : defaultClass;
            
            randomGoddess = 
                DominoConfigUtils.instancia(RandomGoddess.class, klass);
        }
        return randomGoddess;
    }
    
    Collection<DominoEventListener> makeInstanciasListeners() 
            throws DominoConfigException {
        
        final Collection<DominoEventListener> listeners
                = new ArrayList<>(this.eventListeners.size() 
                                  + this.classesEventListeners.size()
                                  + this.nomesEventListeners.size());
        
        listeners.addAll(this.eventListeners);

        try {
            
            listeners.addAll(
                this.classesEventListeners
                    .parallelStream()
                    .map(INSTN_LIST_KLASS)
                    .collect(Collectors.toList())
            );

            listeners.addAll(
                this.nomesEventListeners
                    .parallelStream()
                    .map(INSTN_LIST_NAME)
                    .collect(Collectors.toList())
            );
        } catch (final WrappedException wep){
            throw (DominoConfigException) wep.getCause();
        }
   
        return listeners;
    }    
}
