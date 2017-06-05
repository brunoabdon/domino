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

import java.util.Collection;
import java.util.EnumSet;
import org.apache.commons.lang3.Validate;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;
/**
 * Represena um jogador sentado na mesa, jogando uma partida: É a visão de um
 * {@link Jogador} do ponto de vista do motor do jogo.
 * 
 * @author Bruno Abdon
 */
class JogadorWrapper implements Jogador {

    private final String nome;
    private int cadeira;
    private EnumSet<Pedra> mao;

    private final Jogador wrapped;

    /**
     * Exceção que indica que um {@link Jogador} levantou uma {@link 
     * RuntimeException} enquanto tentava executar algum de seus métodos. A
     * exceção levantada pelo jogador será guardada como {@link 
     * Throwable#getCause() causa} desta exceção.
     */
    public class RuntimeBugDeJogadorException extends RuntimeException{

        public RuntimeBugDeJogadorException(final RuntimeException cause) {
            super(cause);
        }
        
        public JogadorWrapper getJogadorBuguento(){
            return JogadorWrapper.this;
        }
        
        @Override
        public RuntimeException getCause(){
            //Throwable.cause não é final.Mas, quem vai mudar isso???
            return (RuntimeException)super.getCause();
        }
    }
    
    /**
     * Cria um {@link JogadorWrapper jogador} dado seu nome e o {@link Jogador}
     * que implenta a IA.
     * 
     * @param nome O nome do jogador.
     * @param wrapped A IA do jogador.
     * 
     * @throws NullPointerException caso um dos parâmetros seja nulo.
     */
    JogadorWrapper(final Jogador wrapped, final String nome){

        Validate.notNull(nome,"João SemNome não joga.");
        Validate.notNull(wrapped,"Bug.");

        this.wrapped = wrapped;
        this.nome = nome;
    }

    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        
        this.mao = EnumSet.of(pedra1,pedra2,pedra3,pedra4,pedra5,pedra6);
        
        try {
            wrapped.recebeMao(pedra1,pedra2,pedra3,pedra4,pedra5,pedra6);
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    @Override
    public Jogada joga() {
        try {
            return wrapped.joga();
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    @Override
    public Vontade vontadeDeComecar() {
        try {
            return wrapped.vontadeDeComecar();
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.cadeira = cadeiraQueSentou;
        try {
            wrapped.sentaNaMesa(mesa, cadeiraQueSentou);
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    String getNome() {
        return nome;
    }

    int getCadeira() {
        return cadeira;
    }
    
    Collection<Pedra> getMao(){
        return this.mao;
    }
    
    int getNumeroDePontosNaMao(){
        return 
            this.mao.parallelStream().mapToInt(Pedra::getNumeroDePontos).sum();
    }

    Jogador getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return this.getNome() + " [" + wrapped.getClass() + "]";
    }
}