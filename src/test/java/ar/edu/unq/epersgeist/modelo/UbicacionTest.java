package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UbicacionTest {

    private Ubicacion quilmes;

    @BeforeEach
    void prepare() {
        quilmes = new Ubicacion("Quilmes");
    }

    @Test
    void crearUbicacionTest() {
        assertEquals("Quilmes", quilmes.getNombre());
    }

    @Test
    void crearUbicacionConNombreNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> new Ubicacion(null));
    }

    @Test
    void agregarMediumTest() {
        Medium cervecero = new Medium();
        quilmes.agregarMedium(cervecero);
        assertEquals(cervecero, quilmes.getMediums().getFirst());
    }

    @Test
    void agregarEspirituTest() {
        Espiritu pasion = new EspirituDemoniaco(100, "Cervecero, hoy hay que ganar", new Ubicacion("Lanús"));
        quilmes.agregarEspiritu(pasion);
        assertEquals(pasion, quilmes.getEspiritus().getFirst());
    }

    @Test
    void eliminarEspirituTest() {
        Espiritu pasion = new EspirituDemoniaco(100, "Cervecero, hoy hay que ganar", new Ubicacion("Lanús"));
        quilmes.agregarEspiritu(pasion);
        assertEquals(pasion, quilmes.getEspiritus().getFirst());
        quilmes.eliminarEspiritu(pasion);
        assertTrue(quilmes.getEspiritus().isEmpty());
    }
}
