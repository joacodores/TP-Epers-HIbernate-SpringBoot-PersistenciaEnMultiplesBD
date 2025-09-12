package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {

    private Espiritu rika;
    private Medium yuta;
    private Ubicacion puebloPaleta;

    @BeforeEach
    void crearModelo() {
        puebloPaleta = new Ubicacion("Pueblo Paleta");
        rika = new EspirituAngelical(50, "Rika", puebloPaleta);
        yuta = new Medium("Yuta", 100, 100, puebloPaleta);
    }

    @Test
    void crearEspirituConNivelDeConexionInvalidoLanzaExcepcion() {
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituAngelical(-1, "Aura Negativa", puebloPaleta));
        assertDoesNotThrow(() -> new EspirituAngelical(0, "Chill guy", puebloPaleta));
        assertDoesNotThrow(() -> new EspirituDemoniaco(100, "Monster Ultra", puebloPaleta));
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituDemoniaco(101, "Faker", puebloPaleta));
    }

    @Test
    void crearEspirituConUbicacionNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> new EspirituAngelical(10, "hola", null));
    }

    @Test
    void crearEspirituConNombreNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> new EspirituAngelical(10, null, puebloPaleta));
    }

    @Test
    void aumentarConexionYaEstandoAlMaximoNoHaceNada() {
        Espiritu sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        sukuna.aumentarConexion(yuta);
        assertEquals(100, sukuna.getNivelDeConexion());

        Espiritu mahoraga = new EspirituDemoniaco(95, "Mahoraga", puebloPaleta);
        mahoraga.aumentarConexion(yuta);
        assertEquals(100, mahoraga.getNivelDeConexion());
    }

    @Test
    void conexionConMediumTest() {
        assertEquals(50, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().contains(rika));
    }

    @Test
    void disminuirConexionTest() {
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(10);
        assertEquals(60, rika.getNivelDeConexion());
    }

    @Test
    void disminuirCeroConexionNoHaceNadaTest() {
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(0);
        assertEquals(70, rika.getNivelDeConexion());
    }

    @Test
    void disminuirConexionACeroDesvinculaDeMediumTest() {
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(70);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void seCapeaACeroElNivelDeConexionAlDisminuirConexionMuchoTest() {
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(1000);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void angelesPuedenExorcizarTest() {
        assertTrue(new EspirituAngelical(100, "Galaiel", puebloPaleta).puedeExorcizar());
    }

    @Test
    void demoniosNoPuedenExorcizarTest() {
        assertFalse(new EspirituDemoniaco(100, "Baal", puebloPaleta).puedeExorcizar());
    }

    @Test
    void demonioAtacarNoHaceNada() {
        EspirituDemoniaco sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        sukuna.atacar(rika);
        assertEquals(50, rika.getNivelDeConexion());
        assertEquals(100, sukuna.getNivelDeConexion());
    }

    @Test
    void angelRecibirAtaqueNoHaceNada() {
        EspirituDemoniaco sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        rika.recibirAtaque(100, sukuna);
        assertEquals(50, rika.getNivelDeConexion());
        assertEquals(100, sukuna.getNivelDeConexion());
    }
}
