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
     * Os {@link Jogador jogadores} e os {@link DominoEventListener 
     * eventListeners} informados no parâmetro {@code dominoConfig} serão 
     * instanciados. Se uma {@link RandomGoddess geradora de aleatoriedade} 
     * {@link DominoConfig#setRandomizadora(RandomGoddess) for informada}, será 
     * instanciada. Se não, os eventos aleatórios serão baseados em {@link 
     * java.util.Random}.
     * 
     * @param configuracao A configuração do jogo.
     * 
     * @throws DominoConfigException caso a configuração passada esteja inválida 
     * (deve conter 4 jogadores e todos os nomes de classe devem ser válidos, 
     * para classes com um construtor vazio). 
     * 
     */
    public Jogo(final DominoConfig configuracao) throws DominoConfigException{

        //pegando os 4 jogadores da configuracao
        final JogadorWrapper jogador1dupla1 = 
            configuracao.makeInstanciaJogador(1, 1);
        
        final JogadorWrapper jogador1dupla2 = 
            configuracao.makeInstanciaJogador(2, 1);

        final JogadorWrapper jogador2dupla1 = 
            configuracao.makeInstanciaJogador(1, 2);

        final JogadorWrapper jogador2dupla2 = 
            configuracao.makeInstanciaJogador(2, 2);
        
        //pegando os eventlisteners da configuracao
        final Collection<DominoEventListener> eventListeners = 
            configuracao.makeInstanciasListeners();
        
        //pegando a geradora de aleatoriedade (ou usando a default, se nao tiver
        this.fortuna = 
            configuracao.makeInstanciaRandomGoddess(DefaultRandomGoddess.class);
        
        //configuracao tava ok. vamos registrar tudo agora.
        
        //criando o divulgador de eventos
        this.eventBroadcaster = new DominoEventBroadcaster();

        //registrando os jogadores que forem eventlistenres
        jogadorAtento(jogador1dupla1);
        jogadorAtento(jogador2dupla1);
        jogadorAtento(jogador1dupla2);
        jogadorAtento(jogador2dupla2);
        
        //registrando os eventlisteners configurados
        this.eventBroadcaster.addEventListeners(eventListeners,true);

        //criando a mesa finalmente, com os jogadores sentados nela.
        this.mesa = 
            MesaImpl.criaMesa(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2, 
                this.fortuna,
                this.eventBroadcaster);
    }

    /**
     * Roda um jogo com os quatro jogadores, notificando o andamento aos 
     * {@link DominoEventListener}s passados no construtor.
     */
    public void jogar(){
        
        final Dupla dupla1 = mesa.getDupla1();
        final Dupla dupla2 = mesa.getDupla2();
        
        //lancando o evento...
        this.avisaQueJogoComecou(dupla1, dupla2);

        try {
            Dupla ultimaDuplaQueVenceu = null;
            int multiplicadorDobrada = 1;
            while(!alguemVenceu()){

                this.eventBroadcaster.partidaComecou(
                    dupla1.getPontos(), 
                    dupla2.getPontos(), 
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
                dupla1.getPontos(), 
                dupla2.getPontos());

        } catch (final BugDeJogadorException e) {
            this.avisaQueJogadorErrou(e);
        } catch (final JogadorWrapper.RuntimeBugDeJogadorException rte){
            //logar o erro de e.getCause()...
            this.eventBroadcaster
                .jogadorFaleceu(rte.getJogadorBuguento().getCadeira());
            
        }
    }

    /**
     * Faz broadcast do evento "{@link DominoEventListener#jogoComecou(String, 
     * String, String, String) jogoComecou}" pra os {@link DominoEventListener
     * eventListeners} registrados.
     * @param dupla1 A primeira dupla de jogadores.
     * @param dupla2 A segunda dupla de jogadores.
     */
    private void avisaQueJogoComecou(final Dupla dupla1, final Dupla dupla2) {
        
        final JogadorWrapper primeiroJogadorDaDupla1 = dupla1.getJogador1();
        final JogadorWrapper primeiroJogadorDaDupla2 = dupla2.getJogador1();
        final JogadorWrapper segundoJogadorDaDupla1 = dupla1.getJogador2();
        final JogadorWrapper segundoJogadorDaDupla2 = dupla2.getJogador2();
        
        eventBroadcaster.jogoComecou(
            primeiroJogadorDaDupla1.getNome(),
            primeiroJogadorDaDupla2.getNome(),
            segundoJogadorDaDupla1.getNome(),
            segundoJogadorDaDupla2.getNome());
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
     * @param multiplicadorDobrada Diz por quanto deve multiplicado o {@link 
     * Vitoria#getPontos() valor da vitória} (quando uma partida empata, a 
     * próxima vale dobrada).
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
        return mesa.getDupla1().venceu() || mesa.getDupla2().venceu();
    }

    /**
     * Dispara o evento correspondene a um {@link BugDeJogadorException bug de 
     * um dos jogadores}.
     * @param e A exceção levantada por um bug por parte de um {@link Jogador}.
     */
    private void avisaQueJogadorErrou(final BugDeJogadorException e) {
            
        final int cadeira = e.getJogadorBuguento().getCadeira();
        
        switch(e.getFalha()){
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
        }
    }
 }
