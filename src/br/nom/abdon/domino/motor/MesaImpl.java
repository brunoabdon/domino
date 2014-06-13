package br.nom.abdon.domino.motor;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import br.nom.abdon.domino.eventos.OmniscientDominoEventListener;
import br.nom.abdon.domino.motor.util.IteratorReadOnly;

class MesaImpl implements Mesa{

    private final Dupla dupla1, dupla2;
    
    private final Deque<Pedra> listaDePedras;
    private Numero numeroEsquerda, numeroDireita;
    
    private final List<JogadorWrapper> jogadores;

    private final OmniscientDominoEventListener eventListener;

    //usado no toString()
    private final static Collector<CharSequence, ?, String> joining = 
        Collectors.joining("","{","}");

    MesaImpl(
            final JogadorWrapper jogador1dupla1, 
            final JogadorWrapper jogador1dupla2, 
            final JogadorWrapper jogador2dupla1, 
            final JogadorWrapper jogador2dupla2,
            final OmniscientDominoEventListener eventListener) {

        this.listaDePedras = new ArrayDeque<>(28-4);
        this.dupla1 = new Dupla(jogador1dupla1, jogador2dupla1);
        this.dupla2 = new Dupla(jogador1dupla2, jogador2dupla2);
        
        this.jogadores = 
                Arrays.asList(
                        jogador1dupla1, jogador2dupla1, 
                        jogador2dupla1, jogador2dupla2);
        
        this.eventListener = eventListener;
        
        jogador1dupla1.sentaNaMesa(this, 1);
        jogador1dupla2.sentaNaMesa(this, 2);
        jogador2dupla1.sentaNaMesa(this, 3);
        jogador2dupla2.sentaNaMesa(this, 4);
    }

    void embaralhaEdistribui() {

        //emborca as pedras...
        this.listaDePedras.clear();
        
        //embaralha...
        final List<Pedra> pedras = Arrays.asList(Pedra.values());
        Collections.shuffle(pedras);

        //distribui as maos dos 4 jogadores
        for (int i = 0, idx = 0; i < 4; i++) {
            final Collection<Pedra> mao = pedras.subList(idx, idx+=6); //imutavel
            this.entregaPedras(jogadorDaVez(i), mao);
        }
        //separa o dorme
        this.eventListener.dormeDefinido(pedras.subList(24, 28));
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
    
    
    @Override
    public boolean taVazia(){
            return this.listaDePedras.isEmpty();
    }

    private boolean podeJogar(final Pedra pedra, final Lado lado){
        Numero cabeca = lado == Lado.ESQUERDO ? numeroEsquerda : numeroDireita; 
        return pedra.temNumero(cabeca);
    }

    @Override
    public int quantasPedrasOJogadoresTem(int qualJogador) {
        if(qualJogador < 1 || qualJogador > 4 ) 
            throw new IllegalArgumentException("Dominó se joga com 4.");
        //jogador 1 joga na vez 0. jogadror 2, na vez 1...
        return jogadorDaVez(qualJogador-1).getMao().size();
    }

    /**
     * Coloca uma {@link Pedra} num dado {@link Lado} da {@link Mesa}, 
     * lenvantando uma exceção se nao puder colocar.
     * 
     * @param pedra A pedra que é pra colocar
     * @param lado Onde botar ela.
     * @return <code>true</code> se a pedra foi realmente colocada, ou 
     * <code>false</code> caso fosse uma pedra bêba.
     */
    boolean coloca(final Pedra pedra, Lado lado) {

        final boolean podeColocar;
        
        if(taVazia()){
            podeColocar = true;
            
            this.listaDePedras.addFirst(pedra);
            this.numeroEsquerda = pedra.getPrimeiroNumero();
            this.numeroDireita = pedra.getSegundoNumero();
        } else {

            final Lado ladoQueVaiColocar = 
                lado == null && (numeroEsquerda == numeroDireita)
                    ? Lado.ESQUERDO
                    : lado;

            podeColocar = podeJogar(pedra, ladoQueVaiColocar);
            
            if(podeColocar){
                if(ladoQueVaiColocar == Lado.ESQUERDO){
                        listaDePedras.addFirst(pedra);
                        numeroEsquerda = novaCabeca(numeroEsquerda, pedra);
                } else {
                        listaDePedras.addLast(pedra);
                        numeroDireita = novaCabeca(numeroDireita, pedra);
                }
            }
        }
        return podeColocar;
    }

    private Numero novaCabeca(final Numero cabecaAtual, final Pedra pedraQueFoiJogada){
            Numero primeiroNumeroDaPedra = pedraQueFoiJogada.getPrimeiroNumero();

            return primeiroNumeroDaPedra == cabecaAtual 
                    ? pedraQueFoiJogada.getSegundoNumero() 
                    : primeiroNumeroDaPedra;
    }

    JogadorWrapper jogadorDaVez(int vez) {
        final Dupla dupla = (vez%2)==0?dupla1:dupla2;
        return vez<2?dupla.getJogador1():dupla.getJogador2();
    }
    
    Dupla getDupla1(){
        return this.dupla1;
    }

    Dupla getDupla2(){
        return this.dupla2;
    }
    
    Dupla getDuplaDoJogador(JogadorWrapper jogador){
        return dupla1.contem(jogador)?dupla1:dupla2;
    }
    
    List<JogadorWrapper> getJogadores(){
        return this.jogadores;
    }

    @Override
    public Numero getNumeroEsquerda() {
            return numeroEsquerda;
    }
    @Override
    public Numero getNumeroDireita() {
            return numeroDireita;
    }

    @Override
    public Iterator<Pedra> iteratorEsquedaPraDireita() {
            return new IteratorReadOnly<>(listaDePedras.iterator());
    }

    @Override
    public Iterator<Pedra> iteratorDireitaPraEsquerda() {
            return new IteratorReadOnly<>(listaDePedras.descendingIterator());
    }

    @Override
    public int quantasPecas(){
            return this.listaDePedras.size();
    }

    @Override
    public Pedra[] toArray(){
            return this.listaDePedras.toArray(new Pedra[this.listaDePedras.size()]);
    }

    @Override
    public String toString() {
        return listaDePedras.stream()
                .map(Object::toString)
                .collect(joining);
    }
}