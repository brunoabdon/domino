package com.github.abdonia.domino.exemplos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.abdonia.domino.JogadorTest;

class JogadorMamaoTest extends JogadorTest<JogadorMamao> {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected JogadorMamao makeNewInstance() {
        return new JogadorMamao();
    }

}
