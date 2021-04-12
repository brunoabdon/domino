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

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.DominoEventListener;
import com.github.abdonia.domino.motor.BugDeJogadorException.Falha;

/**
 * Um jogo de dominó entre 4 {@link Jogador jogadores}, divididos em 2 duplas,
 * que vai ser extender por várias partidas, até que uma das duplas acumule seis
 * pontos.
 *
 * @author Bruno Abdon
 */
public class Jogo {

    private final MesaImpl mesa;
    private final DominoEventBroadcaster eventBroadcaster;
    private final RandomGoddess fortuna;

    /**
     * Cria um jogo de dominó de acordo com as configurações passadas:
     * Os {@linkplain Jogador jogadores} e os {@link DominoEventListener
     * eventListeners} informados no parâmetro {@code dominoConfig} serão
     * instanciados.
     *
     * @param configuracao A configuração do jogo.
     *
     * @throws DominoConfigException caso a configuração passada esteja inválida
     * (deve conter 4 jogadores e todos os nomes de classe devem ser válidos,
     * para classes com um construtor vazio).
     *
     */
    public Jogo(final DominoConfig configuracao) throws DominoConfigException{
        this(configuracao,new DefaultRandomGoddess());
    }

    /**
     * Cria um jogo de dominó de acordo com o descrito em {@link
     * #Jogo(DominoConfig)} mas onde os eventos "aleatórios" são criados por uma
     * {@link RandomGoddess} passada como parâmetro. Útil para testes
     * controlados.
     *
     * @param configuracao A configuração do jogo.
     * @param randomGoddess A geradora de aleatoriedade que vai ditar o jogo.
     *
     * @throws DominoConfigException caso a configuração passada esteja inválida
     * (deve conter 4 jogadores e todos os nomes de classe devem ser válidos,
     * para classes com um construtor vazio).
     */
    Jogo(final DominoConfig configuracao, final RandomGoddess randomGoddess)
            throws DominoConfigException{

        //pegando os 4 jogadores da configuracao
        final JogadorWrapper jogador0dupla0 =
            (JogadorWrapper) configuracao.getJogador0Dupla0();

        final JogadorWrapper jogador1dupla0 =
            (JogadorWrapper) configuracao.getJogador1Dupla0();

        final JogadorWrapper jogador0dupla1 =
            (JogadorWrapper) configuracao.getJogador0Dupla1();

        final JogadorWrapper jogador1dupla1 =
            (JogadorWrapper) configuracao.getJogador1Dupla1();

        //pegando os eventlisteners da configuracao
        final Collection<DominoEventListener> eventListeners =
            configuracao.getEventListeners();

        this.fortuna = randomGoddess;

        //configuracao tava ok. vamos registrar tudo agora.

        //criando o divulgador de eventos
        this.eventBroadcaster = new DominoEventBroadcaster();

        //registrando os jogadores que forem eventlistenres
        jogadorAtento(jogador0dupla0);
        jogadorAtento(jogador0dupla1);
        jogadorAtento(jogador1dupla0);
        jogadorAtento(jogador1dupla1);

        //registrando os eventlisteners configurados
        this.eventBroadcaster.addEventListeners(eventListeners,true);

        //criando a mesa finalmente, com os jogadores sentados nela.
        this.mesa =
            MesaImpl.criaMesa(
                jogador0dupla0,
                jogador0dupla1,
                jogador1dupla0,
                jogador1dupla1,
                this.fortuna,
                this.eventBroadcaster);
    }

    /**
     * Roda um jogo de acordo com as {@linkplain DominoConfig configurações}
     * passasdas no {@linkplain #Jogo(DominoConfig) construtor}.
     */
    public void jogar(){

        final Dupla dupla0 = mesa.getDupla(0);
        final Dupla dupla1 = mesa.getDupla(1);

        //lancando o evento...
        this.avisaQueJogoComecou(dupla0, dupla1);

        try {
            Dupla ultimaDuplaQueVenceu = null;
            int multiplicadorDobrada = 1;
            while(!alguemVenceu()){

                this.eventBroadcaster.partidaComecou(
                    dupla0.getPontos(),
                    dupla1.getPontos(),
                    multiplicadorDobrada != 1);

                final Partida partida =
                    new Partida(this.mesa, this.fortuna, this.eventBroadcaster);

                final ResultadoPartida resultado =
                    partida.jogar(ultimaDuplaQueVenceu);

                if(resultado == ResultadoPartida.EMPATE){
                    multiplicadorDobrada *=2;
                } else if(resultado instanceof ResultadoPartida.Batida){
                    final ResultadoPartida.Batida batida =
                        (ResultadoPartida.Batida) resultado;

                    ultimaDuplaQueVenceu = getDuplaVencedora(batida);

                    atualizaPlacar(
                        ultimaDuplaQueVenceu,
                        batida.getTipoDeVitoria(),
                        multiplicadorDobrada);

                    multiplicadorDobrada = 1;
                }
            }

            this.eventBroadcaster.jogoAcabou(
                dupla0.getPontos(),
                dupla1.getPontos());

        } catch (final BugDeJogadorException e) {
            this.avisaQueJogadorErrou(e);
        } catch (final JogadorWrapper.RuntimeBugDeJogadorException rte){
            //logar o erro de e.getCause()...
            this.eventBroadcaster
                .jogadorFaleceu(
                    rte.getJogadorBuguento().getCadeira(),
                    rte.getCause().toString());
        }
    }

    /**
     * Faz broadcast do evento "{@link DominoEventListener#jogoComecou(String,
     * String, String, String) jogoComecou}" pra os {@link DominoEventListener
     * eventListeners} registrados.
     * @param dupla0 A primeira dupla de jogadores.
     * @param dupla1 A segunda dupla de jogadores.
     */
    private void avisaQueJogoComecou(final Dupla dupla0, final Dupla dupla1) {
        eventBroadcaster.jogoComecou(
            dupla0.getJogador(0).getNome(),
            dupla1.getJogador(0).getNome(),
            dupla0.getJogador(1).getNome(),
            dupla1.getJogador(1).getNome());
    }

    /**
     * Registra um {@link Jogador} como pra ouvir os eventos deste jogo caso ele
     * implemente também a interface {@link DominoEventListener}.
     *
     * @param jogadorWrapper O {@link JogadorWrapper wrapper} contendo o {@link
     * Jogador} a ser possívelmente registrado pra ouvir os eventos.
     */
    private void jogadorAtento(final JogadorWrapper jogadorWrapper) {

        final Jogador jogador = jogadorWrapper.getWrapped();
        if(jogador instanceof DominoEventListener){
            this.eventBroadcaster
                .addEventListener((DominoEventListener)jogador,false);
        }
    }

    /**
     * Dada uma {@link ResultadoPartida.Batida batida}, diz qual é a {@link
     * Dupla} do {@link Jogador} que bateu.
     *
     * @param batida Uma batida.
     * @return a {@link Dupla} do {@link Jogador} que bateu essa batida.
     */
    private Dupla getDuplaVencedora(final ResultadoPartida.Batida batida) {
        return this.mesa.getDuplaDoJogador(batida.getVencedor());
    }

    /**
     * Atualiza o placar desse jogo, dados quem foi a {@link Dupla dupla}
     * vencedora, qual foi o tipo de {@link Vitoria vitória} e se a {@link
     * Partida} estava valendo dobrada (ou quadruplicada, etc....).
     * .
     *
     * @param duplaDoVencedor A dupla vencedora.
     * @param tipoDeBatida Com foi a vitória.
     * @param multiplicadorDobrada Diz por quanto deve multiplicado o
     * {@linkplain Vitoria#getPontos() valor da vitória} (quando uma partida
     * {@linkplain ResultadoPartida#EMPATE empata}, a próxima vale dobrada).
     */
    private void atualizaPlacar(
        final Dupla duplaDoVencedor,
        final Vitoria tipoDeBatida,
        final int multiplicadorDobrada) {

        final int pontosDaPartida =
            tipoDeBatida.getPontos() * multiplicadorDobrada;

        duplaDoVencedor.adicionaPontos(pontosDaPartida);
    }

    /**
     * Diz se uma das duas {@link Dupla duplas} já atingiu seis {@link
     * Dupla#getPontos() pontos}.
     * @return {@code true} se e somente se uma das duas duplas já tiver,
     * pelo menos, seis pontos.
     */
    private boolean alguemVenceu() {
        return mesa.getDupla(0).venceu() || mesa.getDupla(1).venceu();
    }

    /**
     * Dispara o evento correspondene a um {@link BugDeJogadorException bug de
     * um dos jogadores}.
     *
     * @param e A exceção levantada por um bug por parte de um {@link Jogador}.
     */
    private void avisaQueJogadorErrou(final BugDeJogadorException e) {

        final int cadeira = e.getJogadorBuguento().getCadeira();

        final Falha falha = e.getFalha();
		switch(falha){
            case PEDRA_INVALIDA:
                this.eventBroadcaster
                    .jogadorJogouPedraInvalida(
                        cadeira,
                        e.getPedra(),
                        e.getNumero());
                break;
            case NAO_JOGOU_NEM_TOCOU:
                this.eventBroadcaster.jogadorJogouPedraNenhuma(cadeira);
                break;
            case JA_COMECOU_ERRANDO:
                this.eventBroadcaster.jogadorComecouErrando(cadeira);
                break;
            case TOCOU_TENDO:
                this.eventBroadcaster.jogadorTocouTendoPedraPraJogar(cadeira);
                break;
            case NAO_SABE_SE_COMECE:
                this.eventBroadcaster.jogadorErrouVontadeDeComeçar(cadeira);
                break;
            case TIROU_PEDRA_DO_BOLSO:
                this.eventBroadcaster
                    .jogadorJogouPedraQueNãoTinha(cadeira,e.getPedra());
                break;

            default:
            	throw new RuntimeException(
        			"Erro de programação: Enum novo " + falha
    			);
        }
    }
 }
