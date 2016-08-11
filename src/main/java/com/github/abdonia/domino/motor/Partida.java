package com.github.abdonia.domino.motor;

import java.util.Collection;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vitoria;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

class Partida {

    private final RandomGoddess fortuna;

    private final MesaImpl mesa;

    private final OmniscientDominoEventListener eventListener;
    
    private static final Function<Integer[],BiFunction<Integer,Integer,Integer>> 
        MENOR_NO_ARRAY = (arr) -> (i,j) -> {return arr[i] <= arr[j] ? i : j;};
    
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

            jogadorDaVez = this.mesa.jogadorDaVez(vez);

            final int cadeira = jogadorDaVez.getCadeira();

            final Collection<Pedra> maoDoJogadorDaVez = jogadorDaVez.getMao();

            final Jogada jogada = jogadorDaVez.joga();

            if(jogada == null){
                throw new BugDeJogadorException(
                    "Qual é a jogada? Nenhuma?", jogadorDaVez);
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
                        "Tocou tendo pedra pra jogar!", jogadorDaVez);
                }

                trancou = ++numeroDeToquesSeguidos == 4;

            } else {
                final Lado lado = jogada.getLado();
                pedra = jogada.getPedra();

                this.eventListener.jogadorJogou(cadeira,lado,pedra);
                
                //o jogador tinha mesmo essa pedra, ou tirou do bolso?
                if(!maoDoJogadorDaVez.contains(pedra)){
                    throw new BugDeJogadorException(
                        "Jogando pedra que não tinha!", 
                        jogadorDaVez, pedra);
                }

                maoDoJogadorDaVez.remove(pedra);

                boolean colocouMesmo = this.mesa.coloca(pedra, lado);
                if(!colocouMesmo){
                    throw new PedraBebaException(jogadorDaVez, pedra);
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

        final Vitoria tipoDaBatida;
        
        final boolean carroca = pedra.isCarroca();
        final boolean laELo = 
            mesa.getNumeroEsquerda() == mesa.getNumeroDireita();

        if(carroca && laELo){
            tipoDaBatida = Vitoria.CRUZADA;
        } else if(laELo){
            tipoDaBatida = Vitoria.LA_E_LO;
        } else if (carroca) {
            tipoDaBatida = Vitoria.CARROCA;
        } else {
            tipoDaBatida = Vitoria.BATIDA_SIMPLES;
        }
        return tipoDaBatida;
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

        final Dupla dupla1 = mesa.getDupla1();
        final Dupla dupla2 = mesa.getDupla2();

        final Integer pontos[] = new Integer[]{
            dupla1.getJogador1().getNumeroDePontosNaMao(),
            dupla2.getJogador1().getNumeroDePontosNaMao(),
            dupla1.getJogador2().getNumeroDePontosNaMao(),
            dupla2.getJogador2().getNumeroDePontosNaMao(),
        };
        
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
                this.mesa.jogadorDaVez(melhorIdx);
            
            resultado = batida(
                jogadorComMenosPontosNaMao,
                Vitoria.CONTAGEM_DE_PONTOS);
        }
        
        return resultado;
    }

    private int decideDeQuemDosDoisVaiComecar(final Dupla duplaQueComeca) 
            throws BugDeJogadorException {
            
        final int quemDaDuplaComeca = duplaQueComeca.quemComeca();
        
        final boolean houveConsenso = quemDaDuplaComeca != 0;

        final boolean comecaOJogador1 = 
            houveConsenso ? quemDaDuplaComeca < 0: fortuna.jogador1Comeca();
        
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
        for (int i = 6; i >= 2; i--) {
            final Pedra carroca = Pedra.carrocas[i];
            for (vez = 0; vez < 4 ; vez++) {

                final JogadorWrapper jogador = mesa.jogadorDaVez(vez);
                
                final Collection<Pedra> mao = jogador.getMao();
                if(mao.contains(carroca)){

                    final Jogada primeiraJogada = jogador.joga();
                    
                    if(primeiraJogada == null){
                        throw new BugDeJogadorException(
                            "Vai jogar não?", jogador);
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
                            "Errou a saída, meu velho. Erra pra ser " + carroca,
                            jogador);
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

    private int avanca(int vez){
        return (vez+1)%4;
    }

    private ResultadoPartida verificaMorteSubita() {
        
        ResultadoPartida resultado = null;
        
        final Collection<JogadorWrapper>jogadores = mesa.getJogadores();
        for (JogadorWrapper jogador : jogadores) {
            int quantasNaoCarrocas = 0;
            for (Pedra pedra : jogador.getMao()) {
                if(!pedra.isCarroca() && ++quantasNaoCarrocas == 2){
                    break;
                }
            }
            
            if(quantasNaoCarrocas <= 1){
                if(quantasNaoCarrocas == 1){
                    //partida voltou! 5 carrocas na mao!
                    this.eventListener.partidaVoltou(jogador.getCadeira());
                    resultado = new ResultadoPartidaVolta(jogador);

                } else if (quantasNaoCarrocas == 0){
                    //batida imediata! 6 carrocas na mao!
                    resultado = batida(jogador, Vitoria.SEIS_CARROCAS_NA_MAO);
                }
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
        return new Batida(tipoDeBatida, vencedor);
    }
}