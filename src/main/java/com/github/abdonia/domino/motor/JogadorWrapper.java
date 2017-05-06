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

    private JogadorWrapper(final Jogador wrapped, final String nome) {

        if(nome == null) throw new IllegalArgumentException("João SemNome não joga.");
        if(wrapped == null) throw new IllegalArgumentException("bug");

        this.wrapped = wrapped;
        this.nome = nome;
    }

    /**
     * Cria um {@link JogadorWrapper jogador} dado seu nome e o nome de sua 
     * classe.
     * @param nomeJogador O nome do jogador.
     * @param nomeClasse O nome completo da classe do jogador.
     * @return Um {@link JogadorWrapper jogador} pronto pra jogar.
     * @throws DominoAppException Caso não consiga instanciar o jogador.
     */
    static JogadorWrapper criaJogador(
            final String nomeJogador, 
            final String nomeClasse) {

        final Jogador jogador = 
            DominoUtils
                .instancia(
                    Jogador.class, 
                    nomeClasse);

        return new JogadorWrapper(jogador, nomeJogador);
    }
    
    
    @Override
    public void recebeMao(final Pedra[] pedras) {
        this.mao = new ArrayList<>(Arrays.asList(pedras));
        wrapped.recebeMao(pedras);
    }

    @Override
    public Jogada joga() {
        return wrapped.joga();
    }

    @Override
    public int vontadeDeComecar() {
        return wrapped.vontadeDeComecar();
    }

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.cadeira = cadeiraQueSentou;
        wrapped.sentaNaMesa(mesa, cadeiraQueSentou);
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