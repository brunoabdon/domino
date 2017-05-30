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
package com.github.abdonia.domino.exemplos;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;

/**
 * {@link Jogador} que dá prioridade a jogar as {@link Pedra#isCarroca() 
 * carroças}. Não tendo carroça, joga a primeira {@link Pedra} encontrar que 
 * caiba na {@link Mesa}.
 *
 * @author Bruno Abdon
 *
 */
public class JogadorQueNaoGostaDeCarroca implements Jogador {
   
    /**
     * Dada uma {@link Pedra} e uma {@link Mesa}, diz como seria uma {@link 
     * Jogada} ideal dessa pedra nessa mesa, considerando que uma jogada na 
     * {@link Mesa#getNumeroEsquerda() esquerda} é preferível (arbitrário) e que
     * a única jogada possível pode ser simplesmene {@link Jogada#TOQUE tocar}.
     */
    private static final BiFunction<Mesa,Pedra,Jogada> JOGADA_MESA = 
        (m, p) -> 
            m.taVazia() || p.temNumero(m.getNumeroEsquerda())
                ? Jogada.jogada(p, Lado.ESQUERDO)
                : p.temNumero(m.getNumeroDireita())
                    ? Jogada.jogada(p, Lado.DIREITO)
                    : Jogada.TOQUE;

    /**
     * {@link Comparator} de {@link Pedra} que: (1) diz que qualquer {@link 
     * Pedra#isCarroca() carroça} é maior que uma não-carroça e (2) carroças 
     * entre si e não-carroças entre si se comparam pelo {@link 
     * Pedra#compareTo(java.lang.Enum) comparador natura de pedras}.
     */
    private static final Comparator<Pedra> COMP_PREFERE_CARROCA =
        (p1,p2) -> {
            final boolean p1EhCarroca = p1.isCarroca();
            return 
                p1EhCarroca == p2.isCarroca()
                    ? p1.compareTo(p2)
                    : p1EhCarroca ? 1 : -1;
        };
    
    /**
     * {@link Comparator} de {@link Jogada} que diz que: (1) Qualquer jogada é 
     * maior que {@link Jogada#TOQUE} e (2) Duas jogadas que não são 
     * <code>TOQUE</code> se diferenciam apenas pelas suas {@link Pedra pedras} 
     * (ignorando o {@link Lado}) de acordo com o comparador de pedras {@link 
     * #COMP_PREFERE_CARROCA}.
     */
    private static final Comparator<Jogada> COMP_PREFERE_JOGAR_CARROCA =
        (jog1, jog2) -> 
            jog1 == jog2
                ? 0
                : jog1 == Jogada.TOQUE
                    ? -1
                    : jog2 == Jogada.TOQUE
                        ? 1
                        : COMP_PREFERE_CARROCA
                            .compare(jog1.getPedra(), jog2.getPedra())
    ;
                    
    /**
     * As {@link Pedra pedras} na mão desse {@link Jogador}.
     */
    protected List<Pedra> mao;

    /**
     * Dada uma {@link Pedra}, diz como seria uma {@link Jogada} dessa pedra na
     * {@link #sentaNaMesa(Mesa, int) mesa em que estou sentado} de acordo com 
     * os critérios definidos por {@link #JOGADA_MESA}.
     */
    private Function<Pedra,Jogada> jogadaNaMesa;

    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        
        this.mao = 
            JogadorUtils.fazMao(pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);
    }
    
    /**
     * Joga a maior carroça que puder jogar. Se não tiver carroça que dê pra 
     * jogar, joga como {@link JogadorMamao mamão} memso.
     * 
     * @return Uma {@link Pedra}, de preferência carroça.
     */
    @Override
    public Jogada joga() {
        
        final Jogada jogada =
            mao.parallelStream() //pra cada pedra na minha mao...
            .map(jogadaNaMesa) //...vê como seria uma jogada com ela...
            .max(COMP_PREFERE_JOGAR_CARROCA) //...e escolhe a melhor.
            .get();
        
        //mesmo se for TOQUE, nao tem problema. TOQUE.getPedra() = null;
        this.mao.remove(jogada.getPedra());
        
        return jogada;
    }

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeiraQueSentou) {
        this.jogadaNaMesa = p -> JOGADA_MESA.apply(mesa, p);
    }

    /**
     * Colabora com um possível parceiro da mesma classe: a vonta só empata
     * caso os dois tenham o memso número de carroças na mão.
     * 
     * @return a vontade de começar a jogar, que vai aumentando de acordo com o
     * número de carroças na mão.
     */
    @Override
    public Vontade vontadeDeComecar() {
        final Vontade vontade;
        switch((int)this.mao.parallelStream().filter(Pedra::isCarroca).count()){
            case 0:         vontade = Vontade.NAO_QUERO_MESMO;  break;
            case 1:         vontade = Vontade.NAO_QUERO;        break;
            case 2:         vontade = Vontade.TANTO_FAZ;        break;
            case 3:         vontade = Vontade.QUERO;            break;
            case 4: case 5: case 6: default: //sou o jogador com mais carrocas
                            vontade = Vontade.QUERO_MUITO;
        }
        return vontade;
    }
}