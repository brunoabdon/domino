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

import static com.github.abdonia.domino.Vitoria.CONTAGEM_DE_PONTOS;
import static com.github.abdonia.domino.Vitoria.SEIS_CARROCAS_NA_MAO;
import static com.github.abdonia.domino.Vitoria.tipoDeBatida;

import java.util.Collection;
import java.util.EnumSet;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

class Partida {

    private final RandomGoddess fortuna;

    private final MesaImpl mesa;

    private final OmniscientDominoEventListener eventListener;

    /**
     * Uma array auxliar, contendo só as 5 maiores carroças, em ordem
     * decrescente (de {@linkplain  Pedra#CARROCA_DE_SENA limpo} a
     * {@linkplain  Pedra#CARROCA_DE_DUQUE sena}): {@code {\uD83C\uDC93,
     * \uD83C\uDC8B, \uD83C\uDC83, \uD83C\uDC7B, \uD83C\uDC73}} que são as
     * carroças possíveis de serem a pedra da jogada inicial da primeira
     * partida.
     */
    private static final Pedra MAIORES_CARROCAS[] = {
        Pedra.CARROCA_DE_SENA,
        Pedra.CARROCA_DE_QUINA,
        Pedra.CARROCA_DE_QUADRA,
        Pedra.CARROCA_DE_TERNO,
        Pedra.CARROCA_DE_DUQUE
    };

    Partida(
        final MesaImpl mesa,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener) {

        this.fortuna = fortuna;
        this.mesa = mesa;
        this.eventListener = eventListener;
    }

    protected ResultadoPartida jogar(final Dupla duplaQueGanhouApartidaAnterior)
            throws BugDeJogadorException {

        mesa.embaralhaEdistribui();

        ResultadoPartida resultadoPartida = verificaMorteSubita();
        if(resultadoPartida != null){
            //retorno subito!
            return resultadoPartida;
        }

        Pedra pedra = null;

        boolean alguemBateu = false;
        boolean trancou = false;

        int numeroDeToquesSeguidos = 0;

        boolean ehPrimeiraRodada = duplaQueGanhouApartidaAnterior == null;

        JogadorWrapper jogadorDaVez ;
        if(ehPrimeiraRodada){
            final JogadorWrapper primeiroJogador = primeiraJogada();
            jogadorDaVez = proximo(primeiroJogador);
        } else {
            jogadorDaVez =
                decideDeQuemDosDoisVaiComecar(duplaQueGanhouApartidaAnterior);
        }

        while(!(alguemBateu || trancou)){

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
                    mesa.getPedras().isEmpty()
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
            if(!alguemBateu){
                jogadorDaVez = proximo(jogadorDaVez); //passado a vez
            }

        }

        if(trancou){
            resultadoPartida = contaPontos();
        } else {
            final Vitoria tipoDaBatida = tipoDeBatida(pedra,mesa.taFechada());
            resultadoPartida = batida(jogadorDaVez,tipoDaBatida);
        }

        return resultadoPartida;
    }

    private JogadorWrapper proximo(final JogadorWrapper jogadorDaVez) {
        return mesa.jogadorNaCadeira((jogadorDaVez.getCadeira()+1)%4);
    }

    /**
     * Conta quantos pontos cada {@linkplain JogadorWrapper jogador} tem na mão,
     * definindo quem ganha numa mesa travada. Lança o evento correspondente ao
     * resultado, que pode ser avisar sobre um empate ou sobre uma vitória por
     * pontos.
     *
     * @return O resultado da partida, que vai ser ou um empate ou uma vitória
     * por pontos (pelo {@linkplain JogadorWrapper jogador} que tiver menos
     * pontos na mão).
     */
    private ResultadoPartida contaPontos() {

        final Integer[] pontos =
            mesa.getJogadores()
                .stream()
                .map(JogadorWrapper::getPontosNaMao)
                .toArray(Integer[]::new);

        final Integer melhorIdx = menorNoArray(pontos);

        return melhorIdx != null
            ? batida(this.mesa.jogadorNaCadeira(melhorIdx),CONTAGEM_DE_PONTOS)
            : empate();

    }

    private JogadorWrapper decideDeQuemDosDoisVaiComecar(final Dupla dupla)
            throws BugDeJogadorException {

        final int quemTemMaisVontade = dupla.quemTemMaisVontadeDeComecar();

        final boolean houveConsenso = quemTemMaisVontade != 0;

        final boolean primeiroJogadorComeca =
            houveConsenso
                ? quemTemMaisVontade > 0
                : fortuna.primeiroJogadorComeca();

        final JogadorWrapper jogadorQueComeca =
            primeiroJogadorComeca
                ? dupla.getJogador(0)
                : dupla.getJogador(1);

        final int cadeiraDoJogadorQueComeca = jogadorQueComeca.getCadeira();

        this.eventListener.decididoQuemComeca(
            cadeiraDoJogadorQueComeca, houveConsenso);

        return this.mesa.jogadorNaCadeira(cadeiraDoJogadorQueComeca);
    }

    /**
     * Realiza a primeira rodada da primeira partida, que deve ser do
     * {@linkplain JogadorWrapper jogador} que tiver a maior {@link
     * Pedra#isCarroca() carroça} na mão.
     *
     * <p>O jogador será definido e será {@link JogadorWrapper#joga() chamado a
     * jogar}. Sua {@link Jogada} será validada, devendo ser obrigatoriamente a
     * maior carroça.</p>
     *
     * @return A vez do próximo jogador a jogar.
     *
     * @throws BugDeJogadorException Caso o jogador realize qualquer jogada que
     * não seja a da maior carroça da mesa (que está na mão dele).
     */
    private JogadorWrapper primeiraJogada() throws BugDeJogadorException{

        //a pedra que tem que ser jogada
        final Pedra carrocaInicial = getMaiorCarrocaForaDoDorme();

        //o jogador que tem que jogar
        final JogadorWrapper jogadorComMaiorCarroca =
            this.mesa
                .getJogadores()
                .parallelStream()
                .filter(j -> j.getMao().contains(carrocaInicial))
                .findAny()
                .get();

        //a jogada do jogador
        this.primeiraJogada(jogadorComMaiorCarroca,carrocaInicial);

        return jogadorComMaiorCarroca;
    }

    /**
     * Pede pra um {@linkplain  JogadorWrapper jogador} {@linkplain
     * JogadorWrapper#joga() jogar} e valida que ele jogue  uma dada {@linkplain
     * Pedra#isCarroca() carroça}, levantando {@link BugDeJogadorException} caso
     * ele retorne {@code null} ou tente jogar uma outra {@linkplain Pedra
     * pedra}.
     *
     * @param jogador O {@linkplain  JogadorWrapper jogador} que deve
     * {@linkplain JogadorWrapper#joga() jogar}.
     *
     * @param carroca A {@linkplain Pedra#isCarroca()  carroca} que o jogador
     * deve jogar.
     *
     * @throws BugDeJogadorException Caso o jogador realize qualquer jogada que
     * não seja a da maior carroça da mesa (que está na mão dele).
     */
    private void primeiraJogada(
            final JogadorWrapper jogador,
            final Pedra carroca)
                throws BugDeJogadorException {

        final Jogada primeiraJogada = jogador.joga();

        if(primeiraJogada == null){
            throw new BugDeJogadorException(Falha.NAO_JOGOU_NEM_TOCOU, jogador);
        }

        if(primeiraJogada == Jogada.TOQUE){
            throw new BugDeJogadorException(Falha.TOCOU_TENDO,jogador);
        }

        final Pedra pedra = primeiraJogada.getPedra();
        final Lado lado = primeiraJogada.getLado();
        final int cadeira = jogador.getCadeira();

        this.eventListener.jogadorJogou(cadeira,lado,pedra);

        //agora erre, meu velho
        if(pedra != carroca){
            throw new BugDeJogadorException(
                Falha.JA_COMECOU_ERRANDO, jogador, pedra
            );
        }

        //limpeza
        jogador.getMao().remove(pedra);

        this.mesa.coloca(pedra,lado);
    }

    /**
     * Verifica se a partida vai parar subitamente, antes de qualquer um jogar,
     * por um dos dois motivos: (a) um dos {@linkplain JogadorWrapper jogadores}
     * recebeu 5 {@linkplain Pedra#isCarroca() carroças} na mão (e a partida
     * {@linkplain ResultadoPartida.Volta volta}) ou (b) um dos jogadores
     * recebeu 6 carroças na mão e sua {@linkplain Dupla dupla} {@linkplain
     * ResultadoPartida.Batida vence} imediatamente.
     *
     * @return Uma {@linkplain ResultadoPartida.Volta}, se um dos {@linkplain
     * JogadorWrapper jogadores} recebeu exatamente 5 {@linkplain
     * Pedra#isCarroca() carroças} na mão, ou uma {@link
     * ResultadoPartida.Batida} do tipo {@link Vitoria#SEIS_CARROCAS_NA_MAO
     * SEIS_CARROCAS_NA_MAO}, se um dos jogadores recebeu 6 carroças na mão, ou
     * {@code null} caso nenhuma das condições ocorrer.
     */
    private ResultadoPartida verificaMorteSubita() {

        ResultadoPartida resultado = null;
        int totalCarrocas = 0; //toltal que já vi na mão de jogador
        for (final JogadorWrapper jogador : mesa.getJogadores()) {
            //contagem pra esse jogador
            int quantasCarrocas = 0;
            int quantasNaoCarrocas = 0;
            for (final Pedra pedra : jogador.getMao()) {
                if((!pedra.isCarroca() || ++quantasCarrocas == -1)
                    && ++quantasNaoCarrocas == 2){
                    //jogador já tem 2 não-carrocas. nao vai ter 5 ou 6 carrocas
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

            //se já vi pelo menos 3 carrocas em mao de jogador, mais nenhum vai
            //ter 5 ou 6 mais.
            if((totalCarrocas += quantasCarrocas) >= 3) break;
        }
        return resultado;
    }

    /**
     * Métido auxiliar que anuncia o {@linkplain
     * OmniscientDominoEventListener#jogadorBateu(int, Vitoria) evento de que um
     * dado jogador bateu} (com um dado {@link Vitoria tipo de batida}) e cria e
     * retorna um {@link ResultadoPartida} equivalente a essa vitória.
     *
     * @param vencedor O {@linkplain JogadorWrapper jogador} que bateu.
     * @param tipoDeBatida O {@linkplain Vitoria tipo de batida}.
     * @return Um {@link ResultadoPartida} equivalente a essa vitória.
     */
    private ResultadoPartida batida(
            final JogadorWrapper vencedor, final Vitoria tipoDeBatida) {
        this.eventListener.jogadorBateu(vencedor.getCadeira(),tipoDeBatida);
        return new ResultadoPartida.Batida(tipoDeBatida, vencedor);
    }

    /**
     * Métido auxiliar que anuncia o {@linkplain
     * OmniscientDominoEventListener#partidaVoltou(int) evento de que a partida
     * vai voltar} pois um dado {@linkplain JogadorWrapper jogador} tinha 5
     * carroças na mão e cria e retorna um {@link ResultadoPartida} equivalente
     * a essa situação.
     *
     * @param vencedor O jogador que bateu.
     * @param tipoDeBatida O tipo de batida.
     * @return Uma {@link ResultadoPartida.Volta}.
     */
    private ResultadoPartida volta(final JogadorWrapper jogador) {
        this.eventListener.partidaVoltou(jogador.getCadeira());
        return new ResultadoPartida.Volta(jogador);
    }

    /**
     * Métido auxiliar que anuncia o {@linkplain
     * OmniscientDominoEventListener#partidaEmpatou() evento de que a partida
     * empatou} e retorna um {@link ResultadoPartida} equivalente a essa
     * situação.
     *
     * @return {@link ResultadoPartida#EMPATE}.
     */
    private ResultadoPartida empate() {
        eventListener.partidaEmpatou();
        return ResultadoPartida.EMPATE;
    }

    /**
     * Calcula qual deve ser a {@linkplain Pedra pedra} usada na primeira
     * {@linkplain Jogada jogada} da primeira partida: Deve ser a maior
     * {@linkplain Pedra#isCarroca() carroça} que não estiver no {@linkplain
     * MesaImpl#getDorme() dorme}.
     * @return A maior {@linkplain Pedra#isCarroca() carroça} que não estiver no
     * {@linkplain MesaImpl#getDorme() dorme}.
     */
    private Pedra getMaiorCarrocaForaDoDorme(){
        Pedra carrocaInicial = null;

        final EnumSet<Pedra> dorme = this.mesa.getDorme();

        for (final Pedra pedra : MAIORES_CARROCAS) {
            if(!dorme.contains(pedra)){
                carrocaInicial = pedra;
                break;
            }
        }
        return carrocaInicial;
    }

    /**
     * Retorna o indíce num array de ints que tenha um valor menor que todos os
     * outros, ou null caso tal índice não exista (por empate ou pelo array
     * estar vazio).
     *
     * @param ints um array de ints.
     * @return o índice do elemento de menor valor, caso exista.
     */
    private static Integer menorNoArray (final Integer ... ints ){
        int idx = 0;
        boolean empate = false;
        for (int i = 1; i < ints.length; i++)
            if((ints[i] < ints[idx] && !(empate = false))
                || ((empate |= ints[i].equals(ints[idx])) && false))
                    idx = i;
        return empate ? null : idx;
    }
}