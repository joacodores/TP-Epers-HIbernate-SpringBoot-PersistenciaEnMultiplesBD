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

    @Test
    void manaNoPuedeSuperarAManaMax()
    {
        Medium tai = new Medium("Tai", 50, 70);
        Medium elNoba = new Medium("El Noba", 50, 50);

        assertEquals(50, tai.getMana());
        assertEquals(50, elNoba.getMana());
        assertEquals(elNoba.getMana(), tai.getMana());

    }

}
