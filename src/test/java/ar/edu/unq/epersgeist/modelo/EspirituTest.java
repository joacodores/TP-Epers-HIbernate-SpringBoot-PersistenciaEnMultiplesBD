package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EspirituTest {
    private Espiritu zorro;
    private Medium naruto;

    @BeforeEach
    void crearModelo() {
        zorro = new Espiritu("fuego", 0, "zorro");
        naruto = new Medium("Naruto", 100, 100);
    }

    @Test
    void conexionConMediumTest(){
        assertEquals(0, zorro.getNivelDeConexion());
        assertTrue(naruto.getEspiritus().isEmpty());
        naruto.conectarseAEspiritu(zorro);
        assertEquals(10, zorro.getNivelDeConexion());
        assertTrue(naruto.getEspiritus().contains(zorro));
    }
}
