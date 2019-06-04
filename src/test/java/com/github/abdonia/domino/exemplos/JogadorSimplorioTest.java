package com.github.abdonia.domino.exemplos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.github.abdonia.domino.JogadorTest;

class JogadorSimplorioTest extends JogadorTest<JogadorSimplorio> {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected JogadorSimplorio makeNewInstance() {
        return new JogadorSimplorio();
    }

}
