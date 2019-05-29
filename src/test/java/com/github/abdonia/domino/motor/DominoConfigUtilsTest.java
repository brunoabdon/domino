package com.github.abdonia.domino.motor;

import static com.github.abdonia.domino.motor.DominoConfigUtils.instancia;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DominoConfigUtilsTest {

    @Test
    void testInstanciaClassOfKString() throws DominoConfigException {
        instancia(String.class);
    }

    @Test
    void testInstanciaClassSemConstrutor() throws DominoConfigException {
        assertThrows(
            DominoConfigException.class,
            () -> instancia(Boolean.class)
        );
    }

    @Test
    void testInstanciaNull() throws DominoConfigException {
        assertThrows(
            NullPointerException.class,
            () -> instancia(null)
        );
    }
    
    @Test
    void testInstanciaClassPorPai() throws DominoConfigException {
        instancia(Object.class, "java.lang.String");
    }
    
    @Test
    void testInstanciaClassPorPaiNomeErrado() {
        assertThrows(
            DominoConfigException.class,
            () -> instancia(Object.class, "java.lang.Foolean")
        );
    }
    @Test
    void testInstanciaClassPorPaiSemConstrutor() {
        assertThrows(
            DominoConfigException.class,
            () -> instancia(Object.class, "java.lang.Boolean")
        );
    }
    
    @Test
    void testInstanciaClassPorPaiSemConstrutorPublic() {
        assertThrows(
            DominoConfigException.class,
            () -> instancia(Object.class, "java.lang.Compiler")
        );
    }
    

    @Test
    void testInstanciaClassPorPaiErradp() throws DominoConfigException {
        assertThrows(
            DominoConfigException.class,
            () -> instancia(Boolean.class, "java.lang.String")
        );
    }
}
