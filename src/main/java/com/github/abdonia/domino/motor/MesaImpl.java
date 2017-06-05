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

import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

final class MesaImpl implements Mesa{

    private final Dupla dupla1, dupla2;
    
    private final Deque<Pedra> listaDePedras;
    private final Cabeca cabecaEsquerda, cabecaDireita;
    
    private final List<JogadorWrapper> jogadores;

    private final OmniscientDominoEventListener eventListener;

    private final RandomGoddess fortuna;
    
    //usado no toString()
    private final static Collector<CharSequence, ?, String> JOINING = 
        Collectors.joining("","{","}");

    static MesaImpl criaMesa(
        final JogadorWrapper jogador1dupla1, 
        final JogadorWrapper jogador1dupla2, 
        final JogadorWrapper jogador2dupla1, 
        final JogadorWrapper jogador2dupla2,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener){
        
        //monta a mesa
        final MesaImpl mesa = 
            new MesaImpl(
                jogador1dupla1, 
                jogador1dupla2, 
                jogador2dupla1, 
                jogador2dupla2, 
                fortuna, 
                eventListener);
        
        //avisa aos jogadores que podem sentar
        jogador1dupla1.sentaNaMesa(mesa, 1);
        jogador1dupla2.sentaNaMesa(mesa, 2);
        jogador2dupla1.sentaNaMesa(mesa, 3);
        jogador2dupla2.sentaNaMesa(mesa, 4);
        
        return mesa;
    }
    
    private MesaImpl(
        final JogadorWrapper jogador1dupla1, 
        final JogadorWrapper jogador1dupla2, 
        final JogadorWrapper jogador2dupla1, 
        final JogadorWrapper jogador2dupla2,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener) {

        this.listaDePedras = new ArrayDeque<>(28-4-3); //max de pedras possivel.
        this.cabecaEsquerda = new Cabeca(this.listaDePedras::addFirst);
        this.cabecaDireita = new Cabeca(this.listaDePedras::addLast);
        
        this.dupla1 = new Dupla(jogador1dupla1, jogador2dupla1);
        this.dupla2 = new Dupla(jogador1dupla2, jogador2dupla2);
        
        this.jogadores = 
            Arrays.asList(
                jogador1dupla1, jogador1dupla2, 
                jogador2dupla1, jogador2dupla2);
        
        this.eventListener = eventListener;
        this.fortuna = fortuna; 
    }
    
    void embaralhaEdistribui() {

        //emborca as pedras...
        this.listaDePedras.clear();
        this.cabecaEsquerda.limpa(); 
        this.cabecaEsquerda.limpa();
        
        //embaralha...
        final Pedra pedras[] = fortuna.embaralha();
        
        //distribui as maos dos 4 jogadores
        for (int i = 0; i < 24;) {
            final JogadorWrapper jogadorDaVez = jogadorDaVez(i/6);
            final Pedra pedra1 = pedras[i++];
            final Pedra pedra2 = pedras[i++];
            final Pedra pedra3 = pedras[i++];
            final Pedra pedra4 = pedras[i++];
            final Pedra pedra5 = pedras[i++];
            final Pedra pedra6 = pedras[i++];
            
            this.entregaPedras(
                jogadorDaVez, pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
        }
        //separa o dorme
        this.eventListener
            .dormeDefinido(pedras[24],pedras[25],pedras[26],pedras[27]);
    }

    /**
     * Entrega uma coleçao de {@link Pedra}s a um {@link Jogador} e anuncia o
     * evento correspondente.
     * 
     * @param jogador O jogador que vai receber as pedras.
     * @param mao As pedras que o jogador vai receber.
     */
    private void entregaPedras(
            final JogadorWrapper jogador, 
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {

        jogador.recebeMao(pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
        
        this.eventListener.jogadorRecebeuPedras(
            jogador.getCadeira(),
            pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
    }
    
    @Override
    public int quantasPedrasOJogadoresTem(final int cadeira) {
        Validate.inclusiveBetween(1, 4,cadeira, "%d? São 4 jogadores.",cadeira);
        //jogador na caidera 1 joga na vez 0. cadeira 2, na vez 1...
        return this.jogadorDaVez(cadeira-1).getMao().size();
    }

    /**
     * Tenta colocar uma {@link Pedra} num dado {@link Lado} da {@link Mesa}, 
     * retornando um boolean dizendo se foi possível colocar. 
     * <p>É possível colocar qualquer pedra quando a mesa está {@link #taVazia() 
     * está vazia}. Quando existem pedras, a pedra sendo colocada deve {@link 
     * Pedra#temNumero(Numero) possuir o número} do lado sendo colocado.</p>
     * 
     * @param pedra A pedra que é pra colocar
     * @param lado Onde botar ela.
     * @return {@code true} se a pedra foi realmente colocada, ou {@code false} 
     * caso fosse uma pedra bêba, que não {@link Pedra#temNumero(Numero) tem o 
     * número} daquele lado da mesa.
     */
    boolean coloca(final Pedra pedra, final Lado lado) {
        return colocaNaMesaVazia(pedra)
                || (lado == Lado.ESQUERDO ? cabecaEsquerda : cabecaDireita)
                    .coloca(pedra);
    }
    
    /**
     * Coloca uma {@link Pedra} na mesa se (e somente se) ela {@linkplain 
     * #taVazia() estiver vazia}. Não faz nada (e retorna {@code false}) se a
     * mesa não estiver vazia.
     * @param pedra A {@link Pedra} pra iniciar a mesa.
     * @return {@code true} se a mesa estava vazia e a pedra foi colocada ou
     * {@code false} caso contrário.
     */
    private boolean colocaNaMesaVazia(final Pedra pedra){
       final boolean colocou;
       if(colocou = this.taVazia()){
            this.listaDePedras.addFirst(pedra);

            this.cabecaEsquerda.inicia(pedra.getPrimeiroNumero());
            this.cabecaDireita.inicia(pedra.getSegundoNumero());
       }
       return colocou;
    }

    JogadorWrapper jogadorDaVez(final int vez) {
        final Dupla dupla = (vez%2)==0 ? dupla1 : dupla2;
        return vez<2 ? dupla.getJogador1() : dupla.getJogador2();
    }
    
    Dupla getDupla1(){
        return this.dupla1;
    }

    Dupla getDupla2(){
        return this.dupla2;
    }
    
    Dupla getDuplaDoJogador(final JogadorWrapper jogador){
        return dupla1.contem(jogador) ? dupla1 : dupla2;
    }
    
    List<JogadorWrapper> getJogadores(){
        return this.jogadores;
    }
    
    Numero getNumero(final Lado lado) {
        return (lado == Lado.ESQUERDO?cabecaEsquerda:cabecaDireita).getNumero();
    }
    
    boolean taFechada(){
        return getNumeroEsquerda() == getNumeroDireita(); //mesmo null....
    }

    @Override
    public boolean taVazia(){
        return this.listaDePedras.isEmpty();
    }

    @Override
    public Numero getNumeroEsquerda() {
        return this.cabecaEsquerda.getNumero();
    }
    @Override
    public Numero getNumeroDireita() {
        return this.cabecaDireita.getNumero();
    }

    @Override
    public Iterator<Pedra> iteratorEsquedaPraDireita() {
        return new ReadOnlyIterator<>(listaDePedras.iterator());
    }

    @Override
    public Iterator<Pedra> iteratorDireitaPraEsquerda() {
        return new ReadOnlyIterator<>(listaDePedras.descendingIterator());
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
        return listaDePedras.stream().map(Object::toString).collect(JOINING);
    }

    private static class Cabeca{
        
        private Numero numero;
        private final Consumer<Pedra> adicionaNaLista;
        
        public Cabeca (final Consumer<Pedra> adicionaNaLista){
            this.adicionaNaLista = adicionaNaLista;
        }
        
        public Numero getNumero(){ return numero;}
        public void limpa(){ this.numero = null;}
        public void inicia(final Numero numero){ this.numero = numero;}
        
        public boolean coloca(final Pedra pedra){
            
            final boolean colocou;
            
            if(colocou = pedra.temNumero(this.numero)){
                final Numero primeiroNumeroPedra = pedra.getPrimeiroNumero();
                this.numero =
                    this.numero == primeiroNumeroPedra 
                        ? pedra.getSegundoNumero()
                        : primeiroNumeroPedra;
                this.adicionaNaLista.accept(pedra);
            }
            return colocou;
        }
    }
    
    private static class ReadOnlyIterator<E> implements Iterator<E> {

        private final Iterator<E> iterator;

        public ReadOnlyIterator(final Iterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override public boolean hasNext() { return iterator.hasNext(); }
        @Override public E next() { return iterator.next(); }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                        "Tentativa de remover pedra da mesa."
            );
        }
    }
}