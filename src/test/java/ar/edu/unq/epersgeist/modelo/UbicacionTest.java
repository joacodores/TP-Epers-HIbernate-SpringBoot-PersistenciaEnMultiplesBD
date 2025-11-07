package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UbicacionTest {

    private Ubicacion quilmes;
    private Set<Coordenada> coordsBA;
    @BeforeEach
    void prepare() {
        coordsBA = Set.of(
                new Coordenada(-34.6037, -58.3816),
                new Coordenada(-34.6100, -58.3852),
                new Coordenada(-34.6075, -58.3748)
        );
        quilmes = new Santuario("Quilmes", 10, coordsBA);
    }

    @Test
    void crearUbicacionTest() {
        assertEquals("Quilmes", quilmes.getNombre());
    }
    //@Test
    //void crearUbicacionConNombreNullLanzaExcepcion() {
    //    assertThrows(NullPointerException.class, () -> new Santuario(null,0));
    //}

    @Test
    void agregarMediumTest() {
        Medium cervecero = new Medium();
        quilmes.agregarMedium(cervecero);
        assertEquals(cervecero, quilmes.getMediums().getFirst());
    }

    @Test
    void agregarEspirituTest() {
        Espiritu pasion = new EspirituDemoniaco(100, "Cervecero, hoy hay que ganar", new Santuario("Lanús", 1, coordsBA));
        quilmes.agregarEspiritu(pasion);
        assertEquals(pasion, quilmes.getEspiritus().getFirst());
    }

    @Test
    void eliminarEspirituTest() {
        Espiritu pasion = new EspirituDemoniaco(100, "Cervecero, hoy hay que ganar", new Santuario("Lanús", 1, coordsBA));
        quilmes.agregarEspiritu(pasion);
        assertEquals(pasion, quilmes.getEspiritus().getFirst());
        quilmes.eliminarEspiritu(pasion);
        assertTrue(quilmes.getEspiritus().isEmpty());
    }

    @Test
    void unaCoordenadaEstaDentroDeUnaUbicacionTest(){
        Coordenada puntoDentro = new Coordenada(-34.6070667, -58.3805333); // CENTROIDE
        assertTrue(quilmes.estaDentro(puntoDentro));
    }

    @Test
    void unaCoordenadaNoEstaDentroDeUnaUbicacionTest() {
        Coordenada puntoDentro = new Coordenada(-34.62, -58.40); // CENTROIDE
        assertFalse(quilmes.estaDentro(puntoDentro));
    }

    @Test
    void unaUbicaciongeneraUnaCoordenadaRandomDentroDeElla(){
        Coordenada generada = quilmes.generarCoordenadaAleatoria();
        assertNotNull(generada);
        assertTrue(quilmes.estaDentro(generada));
    }
}
