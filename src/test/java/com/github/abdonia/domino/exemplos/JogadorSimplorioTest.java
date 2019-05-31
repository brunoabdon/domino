package com.github.abdonia.domino.exemplos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
