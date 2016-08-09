/*
 * Copyright (C) 2016 bamonteiro
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

import com.github.abdonia.domino.Vitoria;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bruno Abdon
 */
public class BatidaTest {

    /**
     * Test of getVencedor method, of class Batida.
     */
    @Test
    public void testGetVencedor() {
        System.out.println("getVencedor");
        JogadorWrapper vencedor = UtilsTests.makeJogador();
        Batida batida = new Batida(Vitoria.BATIDA_SIMPLES, vencedor);
        assertEquals(batida.getVencedor(), vencedor);
    }

    /**
     * Test of getTipoDeVitoria method, of class Batida.
     */
    @Test
    public void testGetTipoDeVitoria() {
        System.out.println("getTipoDeVitoria");
        for (Vitoria vitoria : Vitoria.values()) {
            System.out.println("\t"+vitoria);
            JogadorWrapper vencedor = UtilsTests.makeJogador();
            Batida batida = new Batida(vitoria, vencedor);
            assertEquals(batida.getTipoDeVitoria(), vitoria);
        }
    }
}
