package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediumTest {

    @Test
    void constructorAndGettersTest() {
        Medium juan = new Medium("Juan", 100, 20);
        assertEquals("Juan", juan.getNombre());
        assertEquals(100, juan.getManaMax());
        assertEquals(20, juan.getMana());
    }

}
