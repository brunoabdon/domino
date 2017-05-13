/*
 * Copyright (C) 2016 Bruno Abdon <brunoabdon+github@gmail.com>
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;

class JogadorWrapper implements Jogador {

    private final String nome;
    private int cadeira;
    private Collection<Pedra> mao;

    private final Jogador wrapped;

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
    JogadorWrapper(final Jogador wrapped, final String nome) 
            throws NullPointerException{

        if(nome == null) throw new NullPointerException("João SemNome não joga.");
        if(wrapped == null) throw new NullPointerException("bug");

        this.wrapped = wrapped;
        this.nome = nome;
    }

    @Override
    public void recebeMao(final Pedra[] pedras) {
        this.mao = new ArrayList<>(Arrays.asList(pedras));
        try {
            wrapped.recebeMao(pedras);
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
    public int vontadeDeComecar() {
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
        return this.mao.stream().mapToInt(p -> p.getNumeroDePontos()).sum();
    }

    Jogador getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return this.getNome() + " [" + wrapped.getClass() + "]";
    }
}