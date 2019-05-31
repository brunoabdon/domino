package com.github.abdonia.domino.exemplos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.github.abdonia.domino.JogadorTest;

class JogadorQueNaoGostaDeCarrocaTest
        extends JogadorTest<JogadorQueNaoGostaDeCarroca> {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected JogadorQueNaoGostaDeCarroca makeNewInstance() {
        return new JogadorQueNaoGostaDeCarroca();
    }

}
