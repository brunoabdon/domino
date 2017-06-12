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

import com.github.abdonia.domino.Vitoria;

/**
 *
 * @author Bruno Abdon
 */
public class ResultadoPartidaTest {
    
    /**
     * Test of getJogadorRelevante method, of class ResultadoPartida.
     */
    @Test
    public void testEmpate() {
        System.out.println("empate");
        assertNotNull(ResultadoPartida.EMPATE);
        assertNull(ResultadoPartida.EMPATE.getJogadorRelevante());
    }

    /**
     * Test of getJogadorRelevante method, of class ResultadoPartida.
     */
    @Test
    public void testBatida() {
        System.out.println("batida");
        final JogadorWrapper vencedor = UtilsTests.makeJogador();
        
        for (final Vitoria vitoria : Vitoria.values()) {
            final ResultadoPartida.Batida batida = 
                new ResultadoPartida.Batida(vitoria,vencedor);
            
            assertNotNull(batida);
            
            assertSame(batida.getJogadorRelevante(),vencedor);
            assertSame(batida.getVencedor(),vencedor);
            assertSame(batida.getTipoDeVitoria(),vitoria);
        }
    }
    
    @Test
    public void testVolta(){
        System.out.println("volta");
        final JogadorWrapper jogador = UtilsTests.makeJogador();
        
        final ResultadoPartida.Volta volta = 
            new ResultadoPartida.Volta(jogador);
        
        assertNotNull(volta);
        assertSame(volta.getJogadorRelevante(),jogador);
    }
}
