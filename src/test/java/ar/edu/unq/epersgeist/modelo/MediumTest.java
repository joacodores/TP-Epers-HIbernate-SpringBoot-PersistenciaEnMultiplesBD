package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoEsLibreException;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoPuedeConectarException;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {
    private Ubicacion fuerteApache;
    private Ubicacion bocaPredio;
    private Medium juan;
    private Medium palermo;
    private Espiritu carlitos;
    private Medium tai;
    private Medium elNoba;
    private EspirituAngelical angel;
    private RandomizerFalso randomizer;

    @BeforeEach
    void prepare() {
        fuerteApache = new Santuario("Fuerte Apache",50);
        bocaPredio = new Santuario("Boca Predio",10);
        palermo = new Medium("Martin Palermo", 100, 25, fuerteApache);
        juan = new Medium("Juan", 100, 20, bocaPredio);
        carlitos = new EspirituAngelical(80, "Carlitos", fuerteApache);
        randomizer = new RandomizerFalso();
        tai = new Medium("Tai", 100, 80, fuerteApache);
        elNoba = new Medium("El Noba", 100, 100, fuerteApache);
        angel = new EspirituAngelical(70, "angel", fuerteApache);
    }

    @Test
    void constructorAndGettersTest() {
        assertEquals("Juan", juan.getNombre());
        assertEquals(100, juan.getManaMax());
        assertEquals(20, juan.getMana());
    }

    @Test
    void manaNoPuedeSuperarAManaMaxTest() {
        Medium pablo = new Medium("Pablo", 100, 120, fuerteApache);
        assertEquals(100, pablo.getMana());
    }

    @Test
    void conectarseAEspirituLoAgregaALaListaDeEspiritusTest() {
        assertTrue(tai.getEspiritus().isEmpty());
        tai.conectarseAEspiritu(angel);
        assertFalse(tai.getEspiritus().isEmpty());
    }

    @Test
    void desvincularEspirituLoEliminaDeLaListaDeEspiritusTest() {
        tai.conectarseAEspiritu(angel);
        assertFalse(tai.getEspiritus().isEmpty());
        tai.desvincularEspiritu(angel);
        assertTrue(tai.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarConExorcistaSinEspiritusAngelicalesLanzaExcepcionTest() {
        tai.conectarseAEspiritu(new EspirituDemoniaco(100, "Lucifer, el angel caído", fuerteApache));
        elNoba.conectarseAEspiritu(new EspirituDemoniaco(100, "Belphegor", fuerteApache));
        assertThrows(ExorcistaSinAngelesException.class, () -> tai.exorcizar(elNoba));
    }

    @Test
    void exorcizarAExorcizadoSinEspiritusDemoniacosNoLanzaExcepcionTest() {
        tai.conectarseAEspiritu(angel);
        elNoba.conectarseAEspiritu(new EspirituAngelical(90, "Uriel", fuerteApache));
        assertDoesNotThrow(() -> tai.exorcizar(elNoba));
    }

    @Test
    void exorcizarGanaAngelYDesvinculaAlDemonioTest() {
        EspirituAngelical uriel = new EspirituAngelical(60, "angel", fuerteApache);
        EspirituDemoniaco demonio = new EspirituDemoniaco(10, "demonio", fuerteApache);
        uriel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        tai.conectarseAEspiritu(uriel);
        elNoba.conectarseAEspiritu(demonio);

        /*
            el angel tiene 76 de nivel de conexión, por lo que debería dar 5 + 76 = 81
            demonio tiene mala suerte asi que se va a ir obliterado

            El aftermath debería ser:
             - demonio con nivelDeConexion = 0
             - se desvincula de elNoba
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(6);
        tai.exorcizar(elNoba);
        assertEquals(0, demonio.getNivelDeConexion());
        assertTrue(elNoba.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarGanaDemonioYDesvinculaAlAngelTest() {
        EspirituAngelical angelito = new EspirituAngelical(4, "angelito", fuerteApache);
        tai.conectarseAEspiritu(angelito);
        angelito.disminuirConexion(16);
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio", fuerteApache);
        elNoba.conectarseAEspiritu(demonio);
        angelito.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);

        /*
            angelito, debilitado, tiene 4 de nivelDeConexion, por lo tanto 16 + 4 = 20(queda en 4)
            demonio saca una defensa de 10, gana con lo justo.

            El aftermath debería ser:
             - angel con nivelDeConexion = 0
             - se desvincula de tai
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(10);
        tai.exorcizar(elNoba);
        assertEquals(0, angelito.getNivelDeConexion());
        assertTrue(tai.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarGanaDemonioTest() {
        EspirituAngelical angelito = new EspirituAngelical(4, "angelito", fuerteApache);
        tai.conectarseAEspiritu(angelito);
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio", fuerteApache);
        elNoba.conectarseAEspiritu(demonio);
        angelito.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);

        /*
            angelito, debilitado, tiene 4 de nivelDeConexion, por lo tanto 5 + 20 = 25
            demonio saca una defensa de 30, gana con lo justo.

            El aftermath debería ser:
             - angel con nivelDeConexion = 14 - 5 = 9
             - NO se desvincula de tai
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(30);
        tai.exorcizar(elNoba);
        assertEquals(15, angelito.getNivelDeConexion());
        assertFalse(tai.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarGanaAngelTest() {
        EspirituAngelical uriel = new EspirituAngelical(60, "angel", fuerteApache);
        EspirituDemoniaco demonio = new EspirituDemoniaco(30, "demonio", fuerteApache);
        uriel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        tai.conectarseAEspiritu(uriel);
        elNoba.conectarseAEspiritu(demonio);

        /*
            angel tiene 76 de nivel de conexión, por lo que debería dar 5 + 76 = 81
            demonio tiene mala suerte asi que se va a ir obliterado

            El aftermath debería ser:
             - demonio con nivelDeConexion = 50 - (76 / 2) = 50 - 38 = 12
             - NO se desvincula de elNoba
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(30);
        tai.exorcizar(elNoba);
        assertEquals(12, demonio.getNivelDeConexion());
        assertFalse(elNoba.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarVariosDemoniosTest() {
        EspirituAngelical rika = new EspirituAngelical(44, "Rika", fuerteApache);
        EspirituAngelical ivaar = new EspirituAngelical(64, "Ivaar", fuerteApache);
        EspirituAngelical hana = new EspirituAngelical(0, "Hana", fuerteApache);
        tai.conectarseAEspiritu(rika);
        tai.conectarseAEspiritu(ivaar);
        tai.conectarseAEspiritu(hana);
        hana.disminuirConexion(11);
        rika.setCustomRandomizer(randomizer);
        ivaar.setCustomRandomizer(randomizer);
        hana.setCustomRandomizer(randomizer);
        EspirituDemoniaco jaeger = new EspirituDemoniaco(30, "Jaeger", fuerteApache);
        EspirituDemoniaco noroi = new EspirituDemoniaco(46, "Noroi", fuerteApache);
        jaeger.setCustomRandomizer(randomizer);
        noroi.setCustomRandomizer(randomizer);
        elNoba.conectarseAEspiritu(jaeger);
        elNoba.conectarseAEspiritu(noroi);
        randomizer.setSecuenciaDeAtaques(10, 10, 0);
        randomizer.setSecuenciaDeDefensas(1, 1, 100);
        tai.exorcizar(elNoba);
        assertEquals(0, jaeger.getNivelDeConexion());
        assertEquals(0, hana.getNivelDeConexion());
        assertEquals(List.of(rika, ivaar), tai.getEspiritus());
        assertEquals(List.of(noroi), elNoba.getEspiritus());
    }

    @Test
    void exorcizarUnicamenteExorcizaDemoniosYNoAngelesTest() {
        EspirituAngelical rika = new EspirituAngelical(44, "Rika", fuerteApache);
        EspirituAngelical ivaar = new EspirituAngelical(64, "Ivaar", fuerteApache);
        EspirituAngelical hana = new EspirituAngelical(0, "Hana", fuerteApache);
        tai.conectarseAEspiritu(rika);
        tai.conectarseAEspiritu(ivaar);
        tai.conectarseAEspiritu(hana);
        hana.disminuirConexion(11);
        rika.setCustomRandomizer(randomizer);
        ivaar.setCustomRandomizer(randomizer);
        hana.setCustomRandomizer(randomizer);
        EspirituDemoniaco jaeger = new EspirituDemoniaco(30, "Jaeger", fuerteApache);
        EspirituDemoniaco noroi = new EspirituDemoniaco(46, "Noroi", fuerteApache);
        EspirituAngelical hanyuu = new EspirituAngelical(90, "Hanyuu", fuerteApache);
        hanyuu.setCustomRandomizer(randomizer);
        jaeger.setCustomRandomizer(randomizer);
        noroi.setCustomRandomizer(randomizer);
        elNoba.conectarseAEspiritu(hanyuu);
        elNoba.conectarseAEspiritu(jaeger);
        elNoba.conectarseAEspiritu(noroi);
        randomizer.setSecuenciaDeAtaques(10, 10, 0);
        randomizer.setSecuenciaDeDefensas(1, 1, 100);
        tai.exorcizar(elNoba);
        assertEquals(0, jaeger.getNivelDeConexion());
        assertEquals(0, hana.getNivelDeConexion());
        assertEquals(List.of(rika, ivaar), tai.getEspiritus());
        assertEquals(List.of(hanyuu, noroi), elNoba.getEspiritus());
    }
/*modificar con logica de descansar e invocar nueva*/
    @Test
    void mediumDescansaTieneMasManaYSusEspiritusMasEnergia() {
        Medium tai = new Medium("Tai", 100, 10, fuerteApache);
        Espiritu espirituAngelical = new EspirituAngelical(0, "zorro", fuerteApache);
        tai.conectarseAEspiritu(espirituAngelical);
        tai.descansar();
        assertEquals(85, tai.getMana());
        assertEquals(52, espirituAngelical.getNivelDeConexion());
    }

    @Test
    void mediumInvocaEspirituASuUbicacion() {
        assertEquals(fuerteApache, carlitos.getUbicacion());
        assertEquals(bocaPredio, juan.getUbicacion());
        juan.invocar(carlitos);
        assertEquals(bocaPredio, carlitos.getUbicacion());
        assertTrue(bocaPredio.getEspiritus().contains(carlitos));
    }

    @Test
    void invocarSinManaNoCambiaNada() {
        juan.disminuirMana(11);
        assertEquals(9, juan.getMana());
        juan.invocar(carlitos);
        //no pasa nada
        assertEquals(9, juan.getMana());
        assertEquals(fuerteApache, carlitos.getUbicacion());
    }

    @Test
    void espirituConectaConMedium() {
        assertTrue(palermo.getEspiritus().isEmpty());
        assertNull(carlitos.getOwner());
        palermo.conectarseAEspiritu(carlitos);
        assertTrue(palermo.getEspiritus().contains(carlitos));
        assertEquals(carlitos.getOwner(), palermo);
    }

    @Test
    void mediumNoPuedeInvocarEspirituNoLibre() {
        palermo.conectarseAEspiritu(carlitos);
        assertThrows(EspirituNoEsLibreException.class, () -> juan.invocar(carlitos));
    }

    @Test
    void espirituNoPuedeConectarConMediumEnOtraUbicacion() {
        assertEquals(fuerteApache, carlitos.getUbicacion());
        assertEquals(bocaPredio, juan.getUbicacion());
        assertThrows(EspirituNoPuedeConectarException.class, () -> juan.conectarseAEspiritu(carlitos));
    }

    @Test
    void invocarDescuenta10YMueveYActualizaListas() {
        int mana = juan.getMana(); // 20
        assertTrue(fuerteApache.getEspiritus().contains(carlitos));
        juan.invocar(carlitos);
        assertEquals(mana - 10, juan.getMana());
        assertEquals(bocaPredio, carlitos.getUbicacion());
        assertFalse(fuerteApache.getEspiritus().contains(carlitos));
        assertTrue(bocaPredio.getEspiritus().contains(carlitos));
    }

    @Test
    void invocarConManaExacto10FuncionaYQuedaEnCero() {
        juan.disminuirMana(10);
        juan.invocar(carlitos);
        assertEquals(0, juan.getMana());
        assertEquals(bocaPredio, carlitos.getUbicacion());
    }

    @Test
    void invocarCuandoYaEstaEnLaMismaUbicacion_Cobra10() {
        // mismo lugar antes de invocar
        carlitos.cambiarUbicacion(bocaPredio);
        int mana0 = juan.getMana();
        juan.invocar(carlitos);
        assertEquals(mana0 - 10, juan.getMana());           // cobra igual
        assertEquals(bocaPredio, carlitos.getUbicacion());  // no cambia lugar
    }

    @Test
    void conectarSubeNivelConexion20Porciento() {
        // nos aseguramos misma ubicación primero
        juan.invocar(carlitos); // descuenta 10 acá
        double nivel0 = carlitos.getNivelDeConexion();
        juan.conectarseAEspiritu(carlitos);
        double esperado = (juan.getMana() * 0.20);
        assertEquals(nivel0 + esperado, carlitos.getNivelDeConexion());
        assertTrue(juan.getEspiritus().contains(carlitos));
    }

    @Test
    void noPuedeConectarDosVecesPorqueNoEstaLibre() {
        juan.invocar(carlitos);
        juan.conectarseAEspiritu(carlitos);
        assertThrows(EspirituNoPuedeConectarException.class, () -> juan.conectarseAEspiritu(carlitos));
    }
}
