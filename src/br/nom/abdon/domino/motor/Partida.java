package br.nom.abdon.domino.motor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.Vitoria;
import br.nom.abdon.domino.eventos.OmniscientDominoEventListener;

class Partida {

    private final Dupla dupla1, dupla2;
    private final MesaImpl mesa;
    

    private final OmniscientDominoEventListener eventListener;

    Partida(
        Dupla dupla1, Dupla dupla2, 
        OmniscientDominoEventListener eventListener) {

        this.dupla1 = dupla1;
        this.dupla2 = dupla2;
        
        this.eventListener = eventListener;
        this.mesa = embaralhaEdistribui();
    }

    protected ResultadoPartida jogar(Dupla duplaQueGanhouApartidaAnterior) 
        throws BugDeJogadorException{

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

        final Collection<Pedra>[] maos = mesa.getMaos();

        while(!(alguemBateu || trancou)){

            jogadorDaVez = jogadorDaVez(vez);
            
            final int cadeira = jogadorDaVez.getCadeira();

            Collection<Pedra> maoDoJogadorDaVez = maos[vez];

            Jogada jogada = jogadorDaVez.joga(mesa);

            if(jogada == null){
                throw new BugDeJogadorException(
                        "Qual é a jogada? Nenhuma?", jogadorDaVez);
            } else if(jogada == Jogada.TOQUE){
                
                this.eventListener.jogadorTocou(cadeira);
                
                //tocou mesmo?
                boolean tinhaPedraPraJogar = 
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
                //se livrando logo do objeto Jogada, que veio do jogador.
                final Lado lado = jogada.getLado();
                pedra = jogada.getPedra();

                this.eventListener.jogadorJogou(cadeira,lado,pedra);
                
                validaJogada(jogadorDaVez,maoDoJogadorDaVez,pedra,lado);

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
            Vitoria tipoDaBatida = veOTipoDaBatida(pedra);
            resultadoPartida = batida(jogadorDaVez,tipoDaBatida);
        }

        return resultadoPartida;
    }

    private Vitoria veOTipoDaBatida(Pedra pedra) {

            Vitoria tipoDaBatida;
            boolean carroca = pedra.isCarroca();
            boolean laELo = mesa.getNumeroEsquerda() == mesa.getNumeroDireita();

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

    private static final Function<Integer[],BiFunction<Integer,Integer,Integer>> 
            menorNoArray = (arr) -> (i,j) -> {return arr[i] <= arr[j] ? i : j;};
    
    /**
     * Conta quantos pontos cada jogador tem na mão, definindo quem ganha numa
     * mesa travada. Lança o evento correspondete os resultado, que pode ser 
     * avisar sobre um empate ou sobre uma vitória por pontos.
     * @return O resultado da partida, que vai ser ou um empate ou uma vitória
     * por pontos (pelo Jogador que tiver menos pontos).
     */
    private ResultadoPartida contaPontos() {

        final ResultadoPartida resultado;

        final Collection<Pedra>[] maos = mesa.getMaos();
        final Integer pontos[] = new Integer[4];

        for (int i = 0; i < maos.length; i++) {
            pontos[i] = maos[i].stream().collect(
                    Collectors.summingInt(p -> p.getNumeroDePontos()));
        }
        
        final BiFunction<Integer,Integer, Integer> menor = 
                menorNoArray.apply(pontos);
        
        final int melhorIdxDupla1 = menor.apply(0,2);
        final int melhorIdxDupla2 = menor.apply(1,3);
        
        if(pontos[melhorIdxDupla1] == pontos[melhorIdxDupla2]){
            resultado = ResultadoPartida.EMPATE;
            eventListener.partidaEmpatou();
        } else {
            final int melhorIdx = menor.apply(melhorIdxDupla1, melhorIdxDupla2);
            final JogadorWrapper jogadorComMenosPontosNaMao = 
                    jogadorDaVez(melhorIdx);
            
            resultado = batida(
                    jogadorComMenosPontosNaMao,
                    Vitoria.CONTAGEM_DE_PONTOS);
        }
        
        return resultado;
    }

    private void validaJogada(
        Jogador jogadorQueJogou, 
        Collection<Pedra> maoDoJogadorQueJogou, 
        Pedra pedra, Lado lado) throws BugDeJogadorException {
        
        if(pedra == null){
            throw new BugDeJogadorException("Cadê a pedra?", jogadorQueJogou);
        }

        if(lado == null 
            && !mesa.taVazia() 
            && mesa.getNumeroEsquerda() != mesa.getNumeroDireita()){

            throw new BugDeJogadorException(
                "De que lado é pra botar essa pedra?", 
                jogadorQueJogou);
        }

        if(!maoDoJogadorQueJogou.contains(pedra)){
            throw new BugDeJogadorException(
                "Jogando pedra que não tinha! ", 
                jogadorQueJogou, pedra);
        }
    }

    private MesaImpl embaralhaEdistribui() {
        //embaralha
        List<Pedra> pedras = Arrays.asList(Pedra.values());
        Collections.shuffle(pedras);

        //distribui
        final Collection<Pedra>[] maos = new Collection[4];
        //mao dos 4 jogadores
        for (int i = 0, idx = 0; i < 4; i++) {

            Collection<Pedra> mao = pedras.subList(idx, idx+=6); //imutavel
            maos[i] = new ArrayList<>(mao);
            
            entregaPedras(jogadorDaVez(i), mao);
        }
        //dorme
        this.eventListener.dormeDefinido(pedras.subList(24, 28)); //imutavel

        //criando e retornando a mesa zerada.
        return new MesaImpl(maos);
    }

    /**
     * Entrega uma coleçao de {@link Pedra}s a um {@link Jogador} e anuncia o
     * evento correspondente.
     * 
     * @param jogador O jogador que vai receber as pedras.
     * @param mao As pedras que o jogador vai receber.
     */
    private void entregaPedras(
            final JogadorWrapper jogador, final Collection<Pedra> mao) {

        jogador.recebeMao(mao.toArray(new Pedra[6]));
        
        this.eventListener.jogadorRecebeuPedras(
                jogador.getCadeira(),
                Collections.unmodifiableCollection(mao));
    }


    private int decideDeQuemDosDoisVaiComecar(Dupla duplaQueComeca) 
            throws BugDeJogadorException {
            
        final int quemDaDuplaComeca = duplaQueComeca.quemComeca();
        return duplaQueComeca == dupla1 
                ? quemDaDuplaComeca * 2 
                : ((quemDaDuplaComeca * 2) + 1);    
    }

    /**
     * Realiza a primeira rodada da primeira partida, que deve ser do {@link 
     * Jogador} que tiver a maior {@link Pedra#isCarroca() carroça} na mão.
     * 
     * O jogador será definido e será 
     * {@link Jogador#joga(br.nom.abdon.domino.Mesa) chamado a jogar}. Sua 
     * {@link Jogada} será validada, devendo ser obrigatoriamente a maior
     * carroça.
     * 
     * 
     * @return A vez do próximo jogador a jogar.
     * 
     * @throws BugDeJogadorException Caso o jogador realize qualquer jogada que
     * não seja a da maior carroça da mesa (que está na mão dele).
     */
    private int primeiraJogada() throws BugDeJogadorException{

        int vez = -1;

        final Collection<Pedra>[] maos = mesa.getMaos();
        loopProcurarMaiorCarroca: 
        for (int i = 6; i >= 2; i--) {
            final Pedra carroca = Pedra.carrocas[i];
            for (int j = 0; j < 4; j++) {
                if(maos[j].contains(carroca)){
                    vez = j;
                    final JogadorWrapper jogadorQueComeca = jogadorDaVez(vez);

                    final Jogada primeiraJogada = jogadorQueComeca.joga(mesa);

                    final Pedra pedra = primeiraJogada.getPedra();

                    this.eventListener.jogadorJogou(
                            jogadorQueComeca.getCadeira(),
                            null,
                            pedra);
                    
                    //agora erre, meu velho
                    if(pedra != carroca){
                        throw new BugDeJogadorException(
                            "Errou a saída, meu velho. Erra pra ser " + carroca,
                            jogadorQueComeca);
                    }
                    //limpeza
                    maos[j].remove(pedra);
                    this.mesa.coloca(pedra,primeiraJogada.getLado());

                    break loopProcurarMaiorCarroca;
                }
            }
        }
        return avanca(vez);
    }

    private JogadorWrapper jogadorDaVez(int vez) {
            final Dupla dupla = (vez%2)==0?dupla1:dupla2;
            return vez<2?dupla.getJogador1():dupla.getJogador2();
    }

    private int avanca(int vez){
            return (vez+1)%4;
    }

    private ResultadoPartida verificaMorteSubita() {
        
        ResultadoPartida resultado = null;
        
        final Collection<Pedra>[] maos = mesa.getMaos();
        for (int i = 0; i < maos.length; i++) {
            int quantasNaoCarrocas = 0;
            for (Pedra pedra : maos[i]) {
                if(!pedra.isCarroca() && ++quantasNaoCarrocas == 2){
                    break;
                }
            }
            
            if(quantasNaoCarrocas <= 1){
                final JogadorWrapper jogador = jogadorDaVez(i);
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
            JogadorWrapper vencedor, Vitoria tipoDeBatida) {
        
        this.eventListener.jogadorBateu(vencedor.getCadeira(),tipoDeBatida);
        return new Batida(tipoDeBatida, vencedor);
    }
}