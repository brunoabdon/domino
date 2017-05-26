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

import com.github.abdonia.domino.Pedra;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bruno Abdon
 */
public class JogadorMamaoTest extends JogadorTestAbstract {
    
    public JogadorMamaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of vontadeDeComecar method, of class JogadorMamao.
     */
    @Test
    public void testVontadeDeComecar() {
        super.testVontadeDeComecar(new JogadorMamao());
    }
    
    @Test 
    public void testaComecoDePartida(){
        super.testaComecoDePartida(new JogadorMamao());
    }

    /**
     * Test of sentaNaMesa method, of class JogadorMamao.
     */
    @Test
    public void testSentaNaMesa() {
        System.out.println("sentaNaMesa");
        final JogadorMamao jogador = new JogadorMamao();
        jogador.sentaNaMesa(DUMMY_MESA_VAZIA, 0);
        assertSame(jogador.mesa, DUMMY_MESA_VAZIA);
    }

    /**
     * Test of recebeMao method, of class JogadorMamao.
     */
    @Test
    public void testRecebeMao() {
        System.out.println("recebeMao");
        JogadorMamao jogador = new JogadorMamao();
        jogador.recebeMao(
            Pedra.DUQUE_SENA, 
            Pedra.LIMPO_QUADRA, 
            Pedra.CARROCA_DE_LIMPO, 
            Pedra.CARROCA_DE_PIO, 
            Pedra.CARROCA_DE_DUQUE, 
            Pedra.CARROCA_DE_TERNO);
        assertNotNull(jogador.mao);
        assertEquals(jogador.mao.size(), 6);
    }
    
    
    @Test
    public void testaMaiorCarroca(){
        super.testaMaiorCarroca(new JogadorMamao());
    }
}
