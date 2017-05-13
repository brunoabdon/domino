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

import com.github.abdonia.domino.exemplos.JogadorAlheio;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Bruno Abdon
 */
public class JogoTest {
    
    public JogoTest() {
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

    @Test
    public void testJogo() {
    
        final JogadorWrapper jw1 = mkJogador("Bruno");
        final JogadorWrapper jw2 = mkJogador("Ronaldo");
        final JogadorWrapper jw3 = mkJogador("Igor");
        final JogadorWrapper jw4 = mkJogador("Eudes");
        
        
        final int[][] ordemPedras;
        
//        final RandomGoddess viciado = new ExMachina(ordemPedras, sorteios);
        
    
    }

    private JogadorWrapper mkJogador(final String nome) {
        return new JogadorWrapper(new JogadorAlheio(),nome);
    }
}
