package com.github.abdonia.domino.motor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.abdonia.domino.Pedra;

class DefaultRandomGoddessTest {

    private RandomGoddess randomGoddes;
    
    @BeforeEach
    public void setup(){
        randomGoddes = new DefaultRandomGoddess();
    }
    
    @Test
    void testEmbaralha() {

        //acao
        final Pedra[] pedras = randomGoddes.embaralha();
        
        //verificacao
        assertThat(pedras,is(arrayContainingInAnyOrder(Pedra.values())));
    }

    @Test
    void testPrimeiroJogadorComeca() {
        //verificacao
        assertDoesNotThrow(() -> randomGoddes.primeiroJogadorComeca());
    }

}
