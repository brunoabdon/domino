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

import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.eventos.OmniscientDominoEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

final class MesaImpl implements Mesa {

    //sempre funciona: int dupla = cadeira&1; int jogadorNaDupla = cadeira>>1;
    private final Dupla[] duplas;
    
    private final Deque<Pedra> listaDePedras;
    private final Cabeca cabecaEsquerda, cabecaDireita;
    
    private final EnumSet<Pedra> dorme;

    private final List<Pedra> visaoDaListaDePedras;
    
    private final List<JogadorWrapper> jogadores;

    private final OmniscientDominoEventListener eventListener;

    private final RandomGoddess fortuna;
    
    //usado no toString()
    private final static Collector<CharSequence, ?, String> JOINING = 
        Collectors.joining("","{","}");

    static MesaImpl criaMesa(
        final JogadorWrapper jogador0dupla0, 
        final JogadorWrapper jogador0dupla1, 
        final JogadorWrapper jogador1dupla0, 
        final JogadorWrapper jogador1dupla1,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener){
        
        //monta a mesa
        final MesaImpl mesa = 
            new MesaImpl(
                jogador0dupla0, 
                jogador0dupla1, 
                jogador1dupla0, 
                jogador1dupla1, 
                fortuna, 
                eventListener);
        
        //avisa aos jogadores que podem sentar
        jogador0dupla0.sentaNaMesa(mesa, 0);
        jogador0dupla1.sentaNaMesa(mesa, 1);
        jogador1dupla0.sentaNaMesa(mesa, 2);
        jogador1dupla1.sentaNaMesa(mesa, 3);
        
        return mesa;
    }
    
    private MesaImpl(
        final JogadorWrapper jogador0dupla0, 
        final JogadorWrapper jogador0dupla1, 
        final JogadorWrapper jogador1dupla0, 
        final JogadorWrapper jogador1dupla1,
        final RandomGoddess fortuna,
        final OmniscientDominoEventListener eventListener) {
        
        final LinkedList pedras = new LinkedList();
        this.listaDePedras = pedras;
        this.visaoDaListaDePedras = Collections.unmodifiableList(pedras);
        
        this.dorme = EnumSet.noneOf(Pedra.class);

        this.cabecaEsquerda = new Cabeca(this.listaDePedras::addFirst);
        this.cabecaDireita = new Cabeca(this.listaDePedras::addLast);
        
        this.duplas = 
            new Dupla[]{
                new Dupla(jogador0dupla0, jogador1dupla0),
                new Dupla(jogador0dupla1, jogador1dupla1)
            };
        
        this.jogadores = 
            Arrays.asList(
                jogador0dupla0, jogador0dupla1, 
                jogador1dupla0, jogador1dupla1);
        
        this.eventListener = eventListener;
        this.fortuna = fortuna; 
    }
    /**
     * {@linkplain RandomGoddess#embaralha() Embaralha} as {@linkplain Pedra 
     * pedras}, {@linkplain JogadorWrapper#recebeMao(Pedra,Pedra,Pedra,Pedra,
     * Pedra,Pedra) entrega aos jogadores} (pedras de 0 a 6 ao primeiro, de 7 a 
     * 12 ao segundo...) e {@linkplain #separaODorme(Pedra,
     * Pedra,Pedra,Pedra) separa} as quatro do {@linkplain #getDorme() dorme}.
     */
    void embaralhaEdistribui() {

        //emborca as pedras...
        this.listaDePedras.clear();
        this.dorme.clear();
        this.cabecaEsquerda.limpa(); 
        this.cabecaDireita.limpa();
        
        //embaralha...
        final Pedra pedras[] = fortuna.embaralha();
        
        //distribui as maos dos 4 jogadores
        int i = 0;
        for(final JogadorWrapper jogador : jogadores){
            final Pedra pedra1 = pedras[i++];
            final Pedra pedra2 = pedras[i++];
            final Pedra pedra3 = pedras[i++];
            final Pedra pedra4 = pedras[i++];
            final Pedra pedra5 = pedras[i++];
            final Pedra pedra6 = pedras[i++];

            this.entregaPedras(
                jogador, pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
        }

        this.separaODorme(pedras[24],pedras[25],pedras[26],pedras[27]);
    }

    /**
     * Adiciona 4 {@linkplain Pedra pedras} ao {@linkplain #getDorme() dorme} e 
     * {@linkplain OmniscientDominoEventListener#dormeDefinido(Pedra,Pedra,
     * Pedra,Pedra) anuncia o evento correspondente}.
     * 
     * @param pedra1 A primeira {@linkplain Pedra pedra} do dorme.
     * @param pedra2 A segunda {@linkplain Pedra pedra} do dorme.
     * @param pedra3 A terceira {@linkplain Pedra pedra} do dorme.
     * @param pedra4 A última {@linkplain Pedra pedra} do dorme.
     */
    private void separaODorme(
            final Pedra pedra1, 
            final Pedra pedra2, 
            final Pedra pedra3, 
            final Pedra pedra4) {

        this.dorme.add(pedra1);
        this.dorme.add(pedra2);
        this.dorme.add(pedra3);
        this.dorme.add(pedra4);

        this.eventListener.dormeDefinido(pedra1,pedra2,pedra3,pedra4);
    }

    /**
     * Entrega 6 pedras de {@linkplain Pedra pedras} a um {@linkplain 
     * JogadorWrapper jogador} e anuncia o {@linkplain 
     * OmniscientDominoEventListener#jogadorRecebeuPedras(int,Pedra,Pedra,Pedra,
     * Pedra,Pedra,Pedra) evento correspondente}.
     * 
     * @param jogador O jogador que vai receber as pedras.
     * @param pedra1 A primeira {@linkplain Pedra pedra} da mão.
     * @param pedra2 A segunda {@linkplain Pedra pedra} da mão.
     * @param pedra3 A terceira {@linkplain Pedra pedra} da mão.
     * @param pedra4 A quarta {@linkplain Pedra pedra} da mão.
     * @param pedra5 A quinta {@linkplain Pedra pedra} da mão.
     * @param pedra6 A última {@linkplain Pedra pedra} da mão.
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
    public int getQuantidadeDePedrasDoJogador(final int cadeira) {
        Validate.inclusiveBetween(0, 3, cadeira, "%d? É de 0 a 3.", cadeira);
        return this.jogadorNaCadeira(cadeira).getMao().size();
    }

    /**
     * Tenta colocar uma {@link Pedra} num dado {@link Lado} desta mesa, 
     * retornando um boolean dizendo se foi possível colocar. 
     * <p>É possível colocar qualquer pedra quando ainda não existe nenhuma 
     * {@linkplain #getPedras() pedra na mesa}. Quando existem pedras, a pedra 
     * sendo colocada deve {@linkplain Pedra#temNumero(Numero) possuir o número}
     * do lado sendo colocado.</p>
     * 
     * @param pedra A pedra que é pra colocar.
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
     * Coloca uma {@link Pedra} na mesa se (e somente se) ainda não existir 
     * nenhuma {@linkplain #getPedras() pedra na mesa}, {@linkplain 
     * Cabeca#inicia(Numero) iniciando as cabeças} da {@linkplain 
     * #cabecaEsquerda esquerda} e da {@linkplain #cabecaDireita direita}. Não 
     * faz nada (e retorna {@code false}) se a mesa não estiver vazia.
     * 
     * @param pedra A {@link Pedra} pra iniciar a mesa.
     * @return {@code true} se a mesa estava vazia e a pedra foi colocada ou
     * {@code false} caso contrário.
     */
    private boolean colocaNaMesaVazia(final Pedra pedra){
       final boolean colocou;
       if(colocou = this.getPedras().isEmpty()){
            this.listaDePedras.addFirst(pedra);

            this.cabecaEsquerda.inicia(pedra.getPrimeiroNumero());
            this.cabecaDireita.inicia(pedra.getSegundoNumero());
       }
       return colocou;
    }

    /**
     * Retorna o {@linkplain JogadorWrapper jogador} {@linkplain 
     * JogadorWrapper#sentaNaMesa(Mesa, int) sentado numa dada cadeira}.
     * 
     * @param cadeira A cadeira que o {@linkplain JogadorWrapper jogador} está 
     * sentado.
     * 
     * @return O {@linkplain JogadorWrapper jogador} {@linkplain 
     * JogadorWrapper#sentaNaMesa(Mesa, int) sentado na cadeira}.
     */
    JogadorWrapper jogadorNaCadeira(final int cadeira) {
        //sempre funciona: dupla = cadeira&1; jogadorNaDupla = cadeira>>1;
        return duplas[cadeira&1].getJogador(cadeira>>1);
    }
    
    /**
     * Retorna a primeira ou segunda {@linkplain Dupla dupla} de {@linkplain 
     * JogadorWrapper jogadores}.
     * 
     * @param idxDupla 0 pra a primeira dupla, ou 1 para a segunda.
     * @return Uma das duas {@linkplain Dupla duplas}.
     */
    Dupla getDupla(final int idxDupla){
        return this.duplas[idxDupla];
    }


    /**
     * Retorna a {@linkplain Dupla dupla} a que um dado {@linkplain 
     * JogadorWrapper jogador} pertence.
     * <p>Tem um comportamento indeterminado caso o jogador passado não pertença
     * a nenhuma das duplas.</p>
     * @param jogador O {@linkplain JogadorWrapper jogador} que se quer saber a
     * que {@linkplain Dupla dupla} pertence.
     * @return A {@linkplain Dupla dupla} á qual o {@linkplain JogadorWrapper 
     * jogador} pertence.
     */
    Dupla getDuplaDoJogador(final JogadorWrapper jogador){
        return duplas[jogador.getCadeira()&1];
    }
    
    /**
     * Retorna uma lista com os {@linkplain JogadorWrapper jogadores} na mesa,
     * por ordem da {@linkplain JogadorWrapper#getCadeira() cadeira} em que 
     * estão sentados.
     * 
     * <p>Essa lista não pode ser modificada, mas isto não é validado.</p>
     * @return A lista de {@linkplain JogadorWrapper jogadores} na mesa.
     */
    List<JogadorWrapper> getJogadores(){
        return this.jogadores;
    }
    
    /**
     * Diz qual é o {@linkplain Numero número} de um dado {@linkplain Lado lado}
     * dessa mesa, podendo ser {@code null} se a mesa estiver vazia. 
     * 
     * @param lado O {@linkplain Lado lado} da mesa que se quer saber o 
     * {@linkplain Numero número}.
     * 
     * @return O {@linkplain Numero número} desse {@linkplain Lado lado} da 
     * mesa, ou {@code null} se a mesa estiver vazia.
     */
    Numero getNumero(final Lado lado) {
        return (lado == Lado.ESQUERDO?cabecaEsquerda:cabecaDireita).getNumero();
    }

    /**
     * Diz se {@linkplain Numero número} de um dado {@linkplain Lado lado} dessa
     * mesa (possivelmente {@code null}) é igual ao número do outro lado.
     * @return {@code true} se essa mesa tiver o mesmo valor pra os {@linkplain 
     * Numero números} dos dois {@linkplain Lado lados}.
     */
    boolean taFechada(){
        return getNumeroEsquerda() == getNumeroDireita(); //mesmo null....
    }

    /**
     * Retorna o conjunto das 4 {@linkplain Pedra pedras} que estão atualmente
     * no dorme. Vai estar vazio antes da primeira {@linkplain Partida partida}
     * começar e durante alguns instantes enquanto {@linkplain 
     * #embaralhaEdistribui() as pedras estão sendo embaralhadas e distribuidas} 
     * no início de uma nova partida.
     * 
     * @return O conjunto das {@linkplain Pedra pedras} no dorme.
     */
    EnumSet<Pedra> getDorme(){
        return this.dorme;
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
    public List<Pedra> getPedras(){
        return this.visaoDaListaDePedras;
    }
    
    @Override
    public String toString() {
        return listaDePedras.stream().map(Object::toString).collect(JOINING);
    }

    /**
     * Uma "ponta" da fila de {@linkplain Pedra pedras} na mesa.
     */
    private static class Cabeca {
        
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
}