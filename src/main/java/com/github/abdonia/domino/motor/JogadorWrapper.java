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

    /**
     * Recebe as {@linkplain Pedra pedras}, {@linkplain #getMao() armazena} e 
     * repassa pra o {@link #getWrapped jogador wrapped}. 
     * 
     * @param pedra1 A primeira {@linkplain Pedra pedra} da mão.
     * @param pedra2 A segunda {@linkplain Pedra pedra} da mão.
     * @param pedra3 A terceira {@linkplain Pedra pedra} da mão.
     * @param pedra4 A quarta {@linkplain Pedra pedra} da mão.
     * @param pedra5 A quinta {@linkplain Pedra pedra} da mão.
     * @param pedra6 A última {@linkplain Pedra pedra} da mão.
     * 
     * @throws BugDeJogadorException caso o {@link #getWrapped jogador wrapped} 
     * lance uma {@link RuntimeException} ao receber as {@linkplain  Pedra 
     * pedras}.
     */
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

    /**
     * 
     * Pede pra o {@link #getWrapped jogador wrapped} {@linkplain Jogador#joga() 
     * jogar} e retorna o que ele retornar (sem nenhuma validação). A 
     * {@linkplain Pedra pedra} retornada na {@link Jogada} não será removida do
     * {@linkplain #getMao() armazenamento interno}, que deverá depois ser 
     * atualizado}.
     * 
     * @return O que o o {@link #getWrapped jogador wrapped} retornar ao tentar 
     * jogar (podendo ser {@code null} ou outra coisa inválida).
     * 
     * @throws BugDeJogadorException caso o {@link #getWrapped jogador wrapped} 
     * lance uma {@link RuntimeException} ao {@linkplain Jogador#joga() tentar 
     * jogar}.
     */
    @Override
    public Jogada joga() {
        try {
            return wrapped.joga();
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    /**
     * 
     * Perguna ao {@link #getWrapped jogador wrapped} sua {@linkplain 
     * Jogador#vontadeDeComecar() vonta de começar} e retorna o que ele disser 
     * (sem nenhuma validação).
     * 
     * @return O que o o {@link #getWrapped jogador wrapped} retornar sobre sua 
     * vontade de jogar (podendo ser {@code null} ou outra coisa inválida).
     * 
     * @throws BugDeJogadorException caso o {@link #getWrapped jogador wrapped} 
     * lance uma {@link RuntimeException} ao {@linkplain 
     * Jogador#vontadeDeComecar()  tentar responder sobre sua vontade}.
     */
    @Override
    public Vontade vontadeDeComecar() {
        try {
            return wrapped.vontadeDeComecar();
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    /**
     * {@linkplain #getCadeira() Decora qual foi a cadeira que sentou} e repassa
     * a informação para o {@link #getWrapped jogador wrapped}.
     
     * @param mesa A {@linkplain mesa} do jogo. 
     * @param cadeiraQueSentou A {@linkplain cadeira} em que sentei.
     * 
     * @throws BugDeJogadorException caso o {@link #getWrapped jogador wrapped} 
     * lance uma {@link RuntimeException} na hora que for {@linkplain 
     * Jogador#sentaNaMesa(Mesa, int) sentar.
     */
    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.cadeira = cadeiraQueSentou;
        try {
            wrapped.sentaNaMesa(mesa, cadeiraQueSentou);
        } catch (RuntimeException e){
            throw new RuntimeBugDeJogadorException(e);
        }
    }

    /**
     * O nome desse jogador.
     * @return 
     */
    String getNome() {
        return nome;
    }

    /**
     * A cadeira que esse jogador {@linkplain #sentaNaMesa(Mesa, int) sentou}.
     * @return A cadeira em que esse jogador {@linkplain #sentaNaMesa(Mesa, int) 
     * se sentou}.
     */
    int getCadeira() {
        return cadeira;
    }
    
    /**
     * As {@linkplain Pedra pedras} na mão desse jogador. Essa colleção não é
     * automáticamente atualizada quando um jogador retorna uma {@link Pedra}
     * numa {@link Jogada} em {@link #joga()}.
     * @return 
     */
    Collection<Pedra> getMao(){
        return this.mao;
    }
    
    /**
     * Conta a soma dos {@linkplain Pedra#getNumeroDePontos() pontos} das 
     * {@linkplain #getMao() pedras na mão desse jogador}.
     * @return Quantos {@linkplain Pedra#getNumeroDePontos() pontos} somam as 
     * {@linkplain #getMao() pedras na mão desse jogador}.
     */
    int getNumeroDePontosNaMao(){
        return 
            this.mao.parallelStream().mapToInt(Pedra::getNumeroDePontos).sum();
    }

    /**
     * Retorna o {@link Jogador} mantido internamente.
     * @return o {@link Jogador} mantido internamente.
     */
    Jogador getWrapped() {
        return wrapped;
    }

    @Override
    public String toString() {
        return this.getNome() + " [" + wrapped.getClass() + "]";
    }
}