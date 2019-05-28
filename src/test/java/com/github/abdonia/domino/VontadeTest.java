package com.github.abdonia.domino;

import static com.github.abdonia.domino.Vontade.NAO_QUERO;
import static com.github.abdonia.domino.Vontade.NAO_QUERO_MESMO;
import static com.github.abdonia.domino.Vontade.QUERO;
import static com.github.abdonia.domino.Vontade.QUERO_MUITO;
import static com.github.abdonia.domino.Vontade.TANTO_FAZ;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VontadeTest {

    @Test
    void testOrdem() {
        assertThat(QUERO_MUITO, is(greaterThanOrEqualTo(QUERO)));
        assertThat(QUERO,is(greaterThanOrEqualTo(TANTO_FAZ)));
        assertThat(TANTO_FAZ, is(greaterThanOrEqualTo(NAO_QUERO)));
        assertThat(NAO_QUERO, is(greaterThanOrEqualTo(NAO_QUERO_MESMO)));
    }

    @Test
    void testQuantidade() {
        final int quantosPossiveis = Vontade.values().length;
        assertEquals(quantosPossiveis, 5);
    }

}
