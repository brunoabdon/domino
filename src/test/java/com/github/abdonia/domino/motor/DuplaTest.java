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

import org.junit.Test;
import static org.junit.Assert.*;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;

/**
 *
 * @author Bruno Abdon
 */
public class DuplaTest {
    
    /**
     * Test of getJogador1 method, of class Dupla.
     */
    @Test
    public void testGetJogador1e2() {
        System.out.println("getJogador1");
        final JogadorWrapper bruno = UtilsTests.makeJogador("bruno");
        final JogadorWrapper igor = UtilsTests.makeJogador("igor");
        final Dupla dupla = new Dupla(bruno,igor);
        assertEquals(dupla.getJogador1(), bruno);
        assertEquals(dupla.getJogador2(), igor);
    }

    /**
     * Test of getPontos method, of class Dupla.
     */
    @Test
    public void testGetPontos() {
        System.out.println("getPontos");
        final Dupla dupla = makeDupla();
        assertEquals(dupla.getPontos(), 0);
    }

    /**
     * Test of adicionaPontos method, of class Dupla.
     */
    @Test
    public void testAdicionaPontos() {
        System.out.println("adicionaPontos");
        final Dupla dupla = makeDupla();
        int pontos = 0;
        for (int i = 0; i < 11; i+=3) {
            dupla.adicionaPontos(i);
            pontos+=i;
            assertEquals(dupla.getPontos(), pontos);
        }
    }

    /**
     * Test of contem method, of class Dupla.
     */
    @Test
    public void testContem() {
        System.out.println("contem");
        final JogadorWrapper bruno = UtilsTests.makeJogador("bruno");
        final JogadorWrapper igor = UtilsTests.makeJogador("igor");
        final Dupla dupla = new Dupla(bruno,igor);
        
        assertTrue(dupla.contem(igor));
        assertTrue(dupla.contem(bruno));

        final JogadorWrapper alfredo = UtilsTests.makeJogador("alfredo");

        assertFalse(dupla.contem(alfredo));
    }

    /**
     * Test of venceu method, of class Dupla.
     */
    @Test
    public void testVenceu() {
        System.out.println("venceu");
        final Dupla dupla = makeDupla();
        for (int i = 0; i < 12; i++) {
            assertEquals(dupla.venceu(),i>=6);
            dupla.adicionaPontos(1);
        }
    }

    /**
     * Test of toString method, of class Dupla.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        final Dupla dupla = makeDupla();
        assertNotNull(dupla.toString());
        assertTrue(dupla.toString().length() > 0);
    }

    @Test
    public void testQuemComeca() throws BugDeJogadorException{
        
        final JogadorInfluenciavel bruno = new JogadorInfluenciavel();
        final JogadorInfluenciavel igor = new JogadorInfluenciavel();
        
        final Dupla dupla = 
            new Dupla(
                new JogadorWrapper(bruno,"bruno"),
                new JogadorWrapper(igor,"igor")
        );
        
        for (final Vontade vontadeBruno : Vontade.values()) {
            bruno.vontade = vontadeBruno;
            for (final Vontade vontadeIgor : Vontade.values()) {
                igor.vontade = vontadeIgor;
                    int quemComeca;
                    quemComeca = dupla.quemComeca();
                    assertTrue(
                        (quemComeca == 0 && vontadeBruno == vontadeIgor)
                        || ((quemComeca > 0) == (vontadeBruno
                                                .compareTo(vontadeIgor) > 0))
                    );
                
            }
        }
    }
    
    private Dupla makeDupla() {
        JogadorWrapper bruno = UtilsTests.makeJogador("bruno");
        JogadorWrapper igor = UtilsTests.makeJogador("igor");
        return new Dupla(bruno,igor);
    }
    
    private static class JogadorInfluenciavel implements Jogador {

            Vontade vontade;
            
            @Override
            public void sentaNaMesa(
                final Mesa mesa, final int cadeiraQueSentou){}

            @Override
            public void recebeMao(
                final Pedra pedra1, final Pedra pedra2, final Pedra pedra3, 
                final Pedra pedra4, final Pedra pedra5, final Pedra pedra6) {}

            @Override
            public Jogada joga() { return null; }

            @Override
            public Vontade getVontadeDeComecar() {
                return vontade;
            }
        } 

}
