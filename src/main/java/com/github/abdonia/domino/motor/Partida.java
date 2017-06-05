/*
 * Copyright (C) 2016 Bruno Abdon
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
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import static com.github.abdonia.domino.Vitoria.CONTAGEM_DE_PONTOS;
import static com.github.abdonia.domino.Vitoria.SEIS_CARROCAS_NA_MAO;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

class Partida {

    private final RandomGoddess fortuna;

    private final MesaImpl mesa;

    private final OmniscientDominoEventListener eventListener;
    
    private static final Function<Integer[],BiFunction<Integer,Integer,Integer>> 
        MENOR_NO_ARRAY = (arr) -> (i,j) -> {return arr[i] <= arr[j] ? i : j;};

    /**
     * Uma array auxliar, contendo só as 5 maiores carroças, em ordem 
     * decrescente (de {@linkplain  Pedra#CARROCA_DE_SENA limpo} a 
     * {@linkplain  Pedra#CARROCA_DE_DUQUE sena}): {@code {\uD83C\uDC93,\uD83C\uDC8B,\uD83C\uDC83,\uD83C\uDC7B,\uD83C\uDC73
     * que são as carroças possíveis de serem a pedra da jogada inicial da
     * primeira partida.
     */
    final Pedra MAIORES_CARROCAS[] = {
            Pedra.CARROCA_DE_SENA, 
            Pedra.CARROCA_DE_QUINA, 
            Pedra.CARROCA_DE_QUADRA, 
            Pedra.CARROCA_DE_TERNO, 
            Pedra.CARROCA_DE_DUQUE};        
    
    Partida(
        final MesaImpl mesa,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener) {

        this.fortuna = fortuna;
        this.mesa = mesa;
        this.eventListener = eventListener;
    }

    protected ResultadoPartida jogar(final Dupla duplaQueGanhouApartidaAnterior) 
            throws BugDeJogadorException{

        mesa.embaralhaEdistribui();
        
        ResultadoPartida resultadoPartida = verificaMorteSubita();
        if(resultadoPartida != null){
            //retorno subito!
            return resultadoPartida;
        }
        
        JogadorWrapper jogadorDaVez = null;

        Pedra pedra = null;

        boolean alguemBateu = false, trancou = false;

        int numeroDeToquesSeguidos = 0;

        boolean ehPrimeiraRodada = duplaQueGanhouApartidaAnterior == null;

        int vez;
        
        if(ehPrimeiraRodada){
            vez = primeiraJogada();
        } else {
            vez = decideDeQuemDosDoisVaiComecar(duplaQueGanhouApartidaAnterior);
        }

        while(!(alguemBateu || trancou)){

            jogadorDaVez = this.jogadorDaVez(vez);

            final int cadeira = jogadorDaVez.getCadeira();

            final Collection<Pedra> maoDoJogadorDaVez = jogadorDaVez.getMao();

            final Jogada jogada = jogadorDaVez.joga();

            if(jogada == null){
                throw new BugDeJogadorException(
                    Falha.NAO_JOGOU_NEM_TOCOU, 
                    jogadorDaVez);
            } else if(jogada == Jogada.TOQUE){
                
                this.eventListener.jogadorTocou(cadeira);
                
                //tocou mesmo?
                final boolean tinhaPedraPraJogar = 
                    mesa.taVazia()
                    || maoDoJogadorDaVez.stream().anyMatch(
                        pedraNaMao -> 
                            pedraNaMao.temNumero(mesa.getNumeroEsquerda()) 
                            || pedraNaMao.temNumero(mesa.getNumeroDireita()));

                if(tinhaPedraPraJogar){
                    throw new BugDeJogadorException(
                        Falha.TOCOU_TENDO, 
                        jogadorDaVez);
                }

                trancou = ++numeroDeToquesSeguidos == 4;

            } else {
                final Lado lado = jogada.getLado();
                pedra = jogada.getPedra();

                this.eventListener.jogadorJogou(cadeira,lado,pedra);
                
                //o jogador tinha mesmo essa pedra, ou tirou do bolso?
                if(!maoDoJogadorDaVez.contains(pedra)){
                    throw new BugDeJogadorException(
                        Falha.TIROU_PEDRA_DO_BOLSO, 
                        jogadorDaVez, 
                        pedra);
                }

                maoDoJogadorDaVez.remove(pedra);

                boolean colocouMesmo = this.mesa.coloca(pedra, lado);
                if(!colocouMesmo){
                    throw new BugDeJogadorException(
                            Falha.PEDRA_INVALIDA,
                            jogadorDaVez, 
                            pedra,
                            mesa.getNumero(lado));
                }

                numeroDeToquesSeguidos = 0;

                alguemBateu = maoDoJogadorDaVez.isEmpty();
            }

            vez = avanca(vez);
        }

        if(trancou){
            resultadoPartida = contaPontos();
        } else {
            final Vitoria tipoDaBatida = veOTipoDaBatida(pedra);
            resultadoPartida = batida(jogadorDaVez,tipoDaBatida);
        }

        return resultadoPartida;
    }

    private Vitoria veOTipoDaBatida(final Pedra pedra) {

        final boolean carroca = pedra.isCarroca();
        final boolean laELo = mesa.taFechada();

        return
            carroca && laELo ?  Vitoria.CRUZADA
            : laELo ?           Vitoria.LA_E_LO
            : carroca ?         Vitoria.CARROCA
            :                   Vitoria.BATIDA_SIMPLES;
    }

    /**
     * Conta quantos pontos cada jogador tem na mão, definindo quem ganha numa
     * mesa travada. Lança o evento correspondente ao resultado, que pode ser 
     * avisar sobre um empate ou sobre uma vitória por pontos.
     * 
     * @return O resultado da partida, que vai ser ou um empate ou uma vitória
     * por pontos (pelo Jogador que tiver menos pontos).
     */
    private ResultadoPartida contaPontos() {

        final ResultadoPartida resultado;

        final Integer pontos[] = 
            mesa.getJogadores()
                .stream()
                .map(JogadorWrapper::getNumeroDePontosNaMao)
                .toArray(Integer[]::new);
        
        final BiFunction<Integer,Integer, Integer> menor = 
            MENOR_NO_ARRAY.apply(pontos);
        
        final int melhorIdxDupla1 = menor.apply(0,2);
        final int melhorIdxDupla2 = menor.apply(1,3);
        
        if(pontos[melhorIdxDupla1].equals(pontos[melhorIdxDupla2])){
            resultado = ResultadoPartida.EMPATE;
            eventListener.partidaEmpatou();
        } else {
            final int melhorIdx = menor.apply(melhorIdxDupla1, melhorIdxDupla2);
            final JogadorWrapper jogadorComMenosPontosNaMao = 
                this.jogadorDaVez(melhorIdx);
            
            resultado = batida(jogadorComMenosPontosNaMao,CONTAGEM_DE_PONTOS);
        }
        
        return resultado;
    }

    private int decideDeQuemDosDoisVaiComecar(final Dupla duplaQueComeca) 
            throws BugDeJogadorException {
            
        final int quemDaDuplaComeca = duplaQueComeca.quemComeca();
        
        final boolean houveConsenso = quemDaDuplaComeca != 0;

        final boolean comecaOJogador1 = 
            houveConsenso ? quemDaDuplaComeca > 0: fortuna.jogador1Comeca();
        
        final JogadorWrapper jogadorQueComeca = 
            comecaOJogador1 ? 
                duplaQueComeca.getJogador1():
                duplaQueComeca.getJogador2();
        
        final int cadeiraDoJogadorQueComeca = jogadorQueComeca.getCadeira();
        
        this.eventListener.decididoQuemComeca(
            cadeiraDoJogadorQueComeca, houveConsenso);
        
        return cadeiraDoJogadorQueComeca-1; //vez
    }

    /**
     * Realiza a primeira rodada da primeira partida, que deve ser do {@link 
     * Jogador} que tiver a maior {@link Pedra#isCarroca() carroça} na mão.
     * 
     * O jogador será definido e será 
     * {@link Jogador#joga() chamado a jogar}. Sua {@link Jogada} será validada,
     * devendo ser obrigatoriamente a maior carroça.
     * 
     * @return A vez do próximo jogador a jogar.
     * 
     * @throws BugDeJogadorException Caso o jogador realize qualquer jogada que
     * não seja a da maior carroça da mesa (que está na mão dele).
     */
    private int primeiraJogada() throws BugDeJogadorException{

        int vez = -1;
        
        loopProcurarMaiorCarroca: 
        for(final Pedra carroca : MAIORES_CARROCAS){
            for (vez = 0; vez < 4 ; vez++) {

                final JogadorWrapper jogador = this.jogadorDaVez(vez);
                
                final Collection<Pedra> mao = jogador.getMao();
                if(mao.contains(carroca)){

                    final Jogada primeiraJogada = jogador.joga();
                    
                    if(primeiraJogada == null){
                        throw new BugDeJogadorException(
                            Falha.NAO_JOGOU_NEM_TOCOU, jogador);
                    }
                    
                    final Pedra pedra = primeiraJogada.getPedra();
                    final Lado lado = primeiraJogada.getLado();

                    this.eventListener.jogadorJogou(
                        jogador.getCadeira(), 
                        lado, 
                        pedra);

                    //agora erre, meu velho
                    if(pedra != carroca){
                        throw new BugDeJogadorException(
                            Falha.JA_COMECOU_ERRANDO,
                            jogador,
                            pedra
                        );
                    }
                    //limpeza
                    mao.remove(pedra);
                    this.mesa.coloca(pedra,lado);

                    break loopProcurarMaiorCarroca;
                }
            }
        }
        return avanca(vez);
    }

    private static int avanca(int vez){
        return (vez+1)%4;
    }

    private ResultadoPartida verificaMorteSubita() {
        
        ResultadoPartida resultado = null;
        
        final Collection<JogadorWrapper> jogadores = mesa.getJogadores();
        for (final JogadorWrapper jogador : jogadores) {
            int quantasNaoCarrocas = 0;
            for (final Pedra pedra : jogador.getMao()) {
                if(!pedra.isCarroca() && ++quantasNaoCarrocas == 2){
                    break;
                }
            }

            if(quantasNaoCarrocas <= 1){
                resultado = 
                    quantasNaoCarrocas == 1
                        //partida voltou! 5 carrocas na mao!
                        ? volta(jogador) 
                        //batida imediata! 6 carrocas na mao!
                        : batida(jogador, SEIS_CARROCAS_NA_MAO);
                break;
            }
        }
        return resultado;
    }

    /**
     * Métido auxiliar que anuncia o evento de que um dado {@link Jogador} bateu 
     * (com um dado {@link Vitoria tipo de batida}) e cria e retorna um {@link 
     * ResultadoPartida} equivalente a essa vitória.
     * 
     * @param vencedor O jogador que bateu.
     * @param tipoDeBatida O tipo de batida.
     * @return Um {@link ResultadoPartida} equivalente a essa vitória.
     */
    private ResultadoPartida batida(
            final JogadorWrapper vencedor, final Vitoria tipoDeBatida) {
        this.eventListener.jogadorBateu(vencedor.getCadeira(),tipoDeBatida);
        return ResultadoPartida.batida(tipoDeBatida, vencedor);
    }
    
    /**
     * Métido auxiliar que anuncia o evento de que um dado {@link Jogador} tinha
     * 5 carroças na mão e a partida vai voltar e cria e retorna um {@link 
     * ResultadoPartida} equivalente a essa situação.
     * 
     * @param vencedor O jogador que bateu.
     * @param tipoDeBatida O tipo de batida.
     * @return Um {@link ResultadoPartida} equivalente a essa vitória.
     */
    private ResultadoPartida volta(final JogadorWrapper jogador) {
        this.eventListener.partidaVoltou(jogador.getCadeira());
        return ResultadoPartida.volta(jogador);
    }
   
    private JogadorWrapper jogadorDaVez(final int vez){
        return this.mesa.jogadorNaCadeira(vez+1);
    }
}