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
import org.apache.commons.lang3.Validate;

import pl.touk.throwing.ThrowingFunction;
import pl.touk.throwing.exception.WrappedException;


/**
 * As configurações de um {@linkplain  Jogo Jogo de dominó}, ou seja, quais 
 * {@link Jogador Jogadores} vão participar (quais são seus nomes e suas 
 * classes) e quais os {@link DominoEventListener listeners} que devem ser 
 * registrados no jogo.
 * 
 * <p>Para um Jogo acontecer, as únicas configurações obrigatórias são os
 * nomes e as classes-ou-instâncias dos 4 jogadores. Listeners são importantes 
 * pra que se possa saber o que aconteceu no jogo (para logar, salvar ou 
 * animar uma GUI), mas não necessários.</p>
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
 *      dominoConfig.setJogador0Dupla0("Bruno",new JogadorMamao());
 *      dominoConfig.setJogador1Dupla0("Amanda",new JogadorMamao());
 *      dominoConfig.setJogador0Dupla1("Marina",new JogadorAlheio());
 *      dominoConfig.setJogador1Dupla1("Paulo",new JogadorAlheio());
 *      
 *      //opcional, mas importante, um listener que mostra o andamento do jogo.
 *      dominoConfig.addEventListener(new LoggerDominoEventListener());
 *      
 *      //cria um jogo com essa configuração e joga
 *      Jogo jogo = new Jogo(dominoConfig);
 *      jogo.jogar();
 * }
 * </pre>
 * <p>Tanto para jogadores como para os listeners é possível escolher entre 
 * setar (1) suas instâncias, (2) suas {@linkplain Class classes} 
 * ou (3) o {@linkplain Class#getName() nome qualificado de suas classes}. Por 
 * exemplo, o jogador 0 da dupla 1 pode ser setado das seguinte maneiras:</p>
 * 
 * <pre>
 * {@code 
 *   DominoConfig dominoConfig = new DominoConfig();
 * 
 *   //(1) setando como instância
 *   dominoConfig.setJogador0Dupla1(new com.acme.domino.JogadorEsperto());
 *
 *   //(2) setando a classe
 *   dominoConfig.setJogador0Dupla1(com.acme.domino.JogadorEsperto.class);
 *        
 *   //(3) setando o nome da classe
 *   dominoConfig.setJogador0Dupla1("com.acme.domino.JogadorEsperto");
 * }
 * </pre>
 * <p>No caso de jogadores, ao setar uma das três opções, as outras opções (pra
 * o mesmo jogador da mesma dupla) são automaticamente setadas pra {@code 
 * null}. Já no caso dos listeners, é possível adcionar vários, cada um usando 
 * qualquer uma das formas.</p>
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
 * <p>A instânciação da classe passada na opção <em>(2)</em> ou até a checagem 
 * pela existência da classe cujo nome foi passado na opção <em>(3)</em> não 
 * acontece no momento de chamada dos métods {@code set}. Só ocorrera 
 * {@linkplain Jogo#Jogo(DominoConfig) quando a configuração for usada pra se 
 * criar um Jogo}.</p>
 * 
 * @author Bruno Abdon
 */
public class DominoConfig {

    private static final 
        Function<Class<? extends DominoEventListener>, DominoEventListener> 
            INSTN_LIST_KLASS = 
                ThrowingFunction.unchecked(DominoConfigUtils::instancia);
    
    private static final 
        Function<String, DominoEventListener> INSTN_LIST_NAME = 
            ThrowingFunction.unchecked(
                s -> DominoConfigUtils.instancia(DominoEventListener.class, s)
            )
        ;
    
    private final String[] nomesJogadores = new String[4];
    private final String[] nomesClassesJogadores  = new String[4];
    @SuppressWarnings("unchecked")
    private final Class<? extends Jogador>[] classesJogadores  = new Class[4];
    private final Jogador[] jogadores  = new Jogador[4];

    private List<String> nomesEventListeners = new ArrayList<>();
    private List<Class<? extends DominoEventListener>> classesEventListeners = 
        new ArrayList<>();
    private List<DominoEventListener> eventListeners = new ArrayList<>();

    public String getNomeJogador0Dupla0() {
        return this.nomesJogadores[0];
    }

    public void setNomeJogador0Dupla0(final String nomeJogador0Dupla0) {
        this.nomesJogadores[0] = nomeJogador0Dupla0;
    }

    public String getNomeJogador1Dupla0() {
        return this.nomesJogadores[1];
    }

    public void setNomeJogador1Dupla0(final String nomeJogador1Dupla0) {
        this.nomesJogadores[1] = nomeJogador1Dupla0;
    }

    public String getNomeJogador0Dupla1() {
        return this.nomesJogadores[2];
    }

    public void setNomeJogador0Dupla1(final String nomeJogador0Dupla1) {
        this.nomesJogadores[2] = nomeJogador0Dupla1;
    }

    public String getNomeJogador1Dupla1() {
        return this.nomesJogadores[3];
    }

    public void setNomeJogador1Dupla1(final String nomeJogador1Dupla1) {
        this.nomesJogadores[3] = nomeJogador1Dupla1;
    }

    public String getNomeClasseJogador0Dupla0() {
        return nomesClassesJogadores[0];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar o
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador0Dupla0 O nome da classe desse jogador.
     */
    public void setJogador0Dupla0(
            final String nomeClasseJogador0Dupla0) {
        this.nomesClassesJogadores[0] = nomeClasseJogador0Dupla0;
        this.classesJogadores[0] = null;
    }

    public String getNomeClasseJogador1Dupla0() {
        return nomesClassesJogadores[1];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar 
     * o nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador1Dupla0 O nome da classe desse jogador.
     */
    public void setJogador1Dupla0(
            final String nomeClasseJogador1Dupla0) {
        this.nomesClassesJogadores[1] = nomeClasseJogador1Dupla0;
        this.classesJogadores[1] = null;
    }

    public String getNomeClasseJogador0Dupla1() {
        return nomesClassesJogadores[2];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar 
     * o nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador0Dupla1 O nome da classe desse jogador.
     */
    public void setJogador0Dupla1(
            final String nomeClasseJogador0Dupla1) {
        this.nomesClassesJogadores[2] = nomeClasseJogador0Dupla1;
        this.classesJogadores[2] = null;
    }

    public String getNomeClasseJogador1Dupla1() {
        return nomesClassesJogadores[3];
    }

    /**
     * Seta o nome da classe do jogador indicado pelo nome do método. Ao setar 
     * o nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param nomeClasseJogador1Dupla1 O nome da classe desse jogador.
     */
    public void setJogador1Dupla1(
            final String nomeClasseJogador1Dupla1) {
        this.nomesClassesJogadores[3] = nomeClasseJogador1Dupla1;
        this.classesJogadores[3] = null;
    }

    public Class<? extends Jogador> getClasseJogador0Dupla0() {
        return classesJogadores[0];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador0Dupla0 a classe do jogador.
     */
    public void setJogador0Dupla0(
            final Class<? extends Jogador> classeJogador0Dupla0) {
        this.classesJogadores[0] = classeJogador0Dupla0;
        this.nomesClassesJogadores[0] = null;
    }

    public Class<? extends Jogador> getClasseJogador1Dupla0() {
        return classesJogadores[1];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador1Dupla0 a classe do jogador.
     */
    public void setJogador1Dupla0(
            final Class<? extends Jogador> classeJogador1Dupla0) {
        this.classesJogadores[1] = classeJogador1Dupla0;
        this.nomesClassesJogadores[1] = null;
    }

    public Class<? extends Jogador> getClasseJogador0Dupla1() {
        return classesJogadores[2];
    }

    /**
     * Seta  a classe do jogador indicado pelo nome do método. Ao setar o nome
     * da classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
     * 
     * @param classeJogador0Dupla1 a classe do jogador.
     */
    public void setJogador0Dupla1(
            final Class<? extends Jogador> classeJogador0Dupla1) {
        this.classesJogadores[2] = classeJogador0Dupla1;
        this.nomesClassesJogadores[2] = null;
    }

    public Class<? extends Jogador> getClasseJogador1Dupla1() {
        return classesJogadores[3];
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
        this.classesJogadores[3] = classeJogador1Dupla1;
        this.nomesClassesJogadores[3] = null;
    }
    /**
     * Seta o nome e a classe de um jogador de uma dupla. Ao setar o nome da 
     * classe, a classe ou a instância de um dado jogador, os valores dos 
     * outros dois atributos vai a {@code null}.
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
        
        Validate.inclusiveBetween(0,1,idxDupla,"Dupla invalida: %d",idxDupla);
        Validate.inclusiveBetween(
            0,1,idxJogadorNaDupla,"Jogador invalido: %d",idxJogadorNaDupla);

        final int index = indexJogador(idxDupla, idxJogadorNaDupla);
        
        nomesJogadores[index] = nomeJogador;
        nomesClassesJogadores[index] = nomeClasseJogador;
        classesJogadores[index] = classeJogador;
        jogadores[index] = jogador;
    }

    private int indexJogador(final int idxDupla, final int idxJogadorNaDupla) {
        return (idxDupla*2) + (idxJogadorNaDupla);
    }

    public Jogador getJogador0Dupla0() {
        return jogadores[0];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o 
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param jogador0Dupla0 a instância do jogador.
     */
    public void setJogador0Dupla0(final Jogador jogador0Dupla0) {
        this.jogadores[0] = jogador0Dupla0;
        this.nomesClassesJogadores[0] = null;
    }

    public Jogador getJogador1Dupla0() {
        return jogadores[1];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o 
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param jogador1Dupla0 a instância do jogador.
     */
    public void setJogador1Dupla0(final Jogador jogador1Dupla0) {
        this.jogadores[1] = jogador1Dupla0;
        this.nomesClassesJogadores[1] = null;
    }

    public Jogador getJogador0Dupla1() {
        return jogadores[2];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o 
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param jogador0Dupla1 a instância do jogador.
     */
    public void setJogador0Dupla1(final Jogador jogador0Dupla1) {
        this.jogadores[2] = jogador0Dupla1;
        this.nomesClassesJogadores[2] = null;
    }

    public Jogador getJogador1Dupla1() {
        return jogadores[3];
    }

    /**
     * Seta a instância do jogador indicado pelo nome do método. Ao setar o 
     * nome da classe, a classe ou a instância de um dado jogador, os valores 
     * dos outros dois atributos vai a {@code null}.
     * 
     * @param jogador1Dupla1  a instância do jogador.
     */
    public void setJogador1Dupla1(final Jogador jogador1Dupla1) {
        this.jogadores[3] = jogador1Dupla1;
        this.nomesClassesJogadores[3] = null;
    }
    
    public List<String> getNomesEventListeners() {
        return this.nomesEventListeners;
    }

    public void setNomesEventListeners(final List<String> nomesEventListeners){
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
    
    public List<Class<? extends DominoEventListener>>
               getClassesEventListeners(){
        return this.classesEventListeners;
    }

    public void setClassesEventListeners(
            final List<Class<? extends DominoEventListener>> 
                classesEventListeners) {
        this.classesEventListeners = classesEventListeners;
    }

    public void addEventListener(
            final Class<? extends DominoEventListener> classeEventListener) {
        this.classesEventListeners.add(classeEventListener);
    }
    
    JogadorWrapper makeInstanciaJogador(
        final int idxDupla, 
        final int idxJogadorNaDupla) throws DominoConfigException{
        
        final int index = indexJogador(idxDupla, idxJogadorNaDupla);        
        
        final String nome = pegaNomeJogador(index,idxJogadorNaDupla,idxDupla);
        final Jogador jogador = makeJogador(index,idxJogadorNaDupla,idxDupla);
        
        return new JogadorWrapper(jogador, nome);
    }

    private Jogador makeJogador(
            final int index, 
            final int idxJogadorNaDupla, 
            final int idxDupla) throws DominoConfigException {
        
        Jogador jogador = jogadores[index];
        if(jogador == null){
            final Class<? extends Jogador> klass = classesJogadores[index];
            if(klass == null){
                final String className = nomesClassesJogadores[index];
                if(className == null){
                    throw new DominoConfigException(
                            "O Jogador %d da dupla %d não foi setado.",
                            idxJogadorNaDupla,
                            idxDupla);
                }
                jogador = DominoConfigUtils.instancia(Jogador.class,className);
            } else {
                jogador = DominoConfigUtils.instancia(klass);
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
