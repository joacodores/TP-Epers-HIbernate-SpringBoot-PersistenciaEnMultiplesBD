package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {

    private Espiritu rika;
    private Medium yuta;

    @BeforeEach
    void crearModelo() {
        rika = new EspirituAngelical(60, "Rika");
        yuta = new Medium("Yuta", 100, 100);
    }

    @Test
    void crearEspirituConNivelDeConexionInvalidoLanzaExcepcion(){
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituAngelical(-1, "Aura Negativa"));
        assertDoesNotThrow(() -> new EspirituAngelical(0, "Chill guy"));
        assertDoesNotThrow(() -> new EspirituDemoniaco(100, "Monster Ultra"));
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituDemoniaco(101, "Faker"));
    }

    @Test
    void aumentarConexionYaEstandoAlMaximoNoHaceNada() {
        Espiritu sukuna = new EspirituDemoniaco(100, "Sukuna");
        sukuna.aumentarConexion(yuta, 10);
        assertEquals(100, sukuna.getNivelDeConexion());

        Espiritu mahoraga = new EspirituDemoniaco(95, "Mahoraga");
        mahoraga.aumentarConexion(yuta, 10);
        assertEquals(100, mahoraga.getNivelDeConexion());
    }

    @Test
    void conexionConMediumTest(){
        assertEquals(60, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().contains(rika));
    }

    @Test
    void disminuirConexionTest(){
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(10);
        assertEquals(60, rika.getNivelDeConexion());
    }

    @Test
    void disminuirCeroConexionNoHaceNadaTest(){
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(0);
        assertEquals(70, rika.getNivelDeConexion());
    }

    @Test
    void disminuirConexionACeroDesvinculaDeMediumTest(){
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(70);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void seCapeaACeroElNivelDeConexionAlDisminuirConexionMuchoTest(){
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(1000);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void angelesPuedenExorcizarTest() {
        assertTrue(new EspirituAngelical(100, "Galaiel").puedeExorcizar());
    }

    @Test
    void demoniosNoPuedenExorcizarTest() {
        assertFalse(new EspirituDemoniaco(100, "Baal").puedeExorcizar());
    }
}
