package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {
    private Espiritu zorro;
    private Medium naruto;

    @BeforeEach
    void crearModelo() {
        zorro = new Espiritu("fuego", 0, "zorro");
        naruto = new Medium("Naruto", 100, 100);
    }

    @Test
    void crearEspirituConNivelDeConexionInvalidoLanzaExcepcion(){
        assertThrows(RuntimeException.class, () -> new Espiritu("fuego", -1, "zorro"));
        assertDoesNotThrow(() -> new Espiritu("fuego", 0, "zorro"));
        assertDoesNotThrow(() -> new Espiritu("fuego", 100, "zorro"));
        assertThrows(RuntimeException.class, () -> new Espiritu("fuego", 101, "zorro"));
    }

    @Test
    void aumentarConexionYaEstandoAlMaximoNoHaceNada() {
        Espiritu espirituMaximo = new Espiritu("divinidad", 100, "espirituMaximo");
        espirituMaximo.aumentarConexion(naruto);
        assertEquals(100, espirituMaximo.getNivelDeConexion());

        Espiritu espirituPotencial = new Espiritu("potencialDivinidad", 95, "espirituPotencial");
        espirituPotencial.aumentarConexion(naruto);
        assertEquals(100, espirituPotencial.getNivelDeConexion());
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
