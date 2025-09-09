package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {

    private Medium tai;
    private Medium elNoba;
    private EspirituAngelical angel;
    private RandomizerFalso randomizer;

    @BeforeEach
    void prepare(){
        randomizer = new RandomizerFalso();
        tai = new Medium("Tai", 100, 80);
        elNoba = new Medium("El Noba", 100, 100);
        angel = new EspirituAngelical(70, "angel");
    }

    @Test
    void constructorAndGettersTest() {
        assertEquals("Tai", tai.getNombre());
        assertEquals(100, tai.getManaMax());
        assertEquals(80, tai.getMana());
    }

    @Test
    void manaNoPuedeSuperarAManaMaxTest() {
        Medium juan = new Medium("Juan", 100, 120);
        assertEquals(100, juan.getMana());
        assertEquals(100, elNoba.getMana());
        assertEquals(elNoba.getMana(), juan.getMana());
    }

    @Test
    void conectarseAEspirituLoAgregaALaListaDeEspiritusTest() {
        assertTrue(tai.getEspiritus().isEmpty());
        tai.conectarseAEspiritu(angel);
        assertFalse(tai.getEspiritus().isEmpty());
    }

    @Test
    void conectarseAlMismoEspirituVariasVecesNoVuelveAAgregarloTest() {
        assertTrue(tai.getEspiritus().isEmpty());
        tai.conectarseAEspiritu(angel);
        assertEquals(1, tai.getEspiritus().size());
        tai.conectarseAEspiritu(angel);
        assertEquals(1, tai.getEspiritus().size());
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
        tai.conectarseAEspiritu(new EspirituDemoniaco(100, "Lucifer, el angel caído"));
        elNoba.conectarseAEspiritu(new EspirituDemoniaco(100, "Belphegor"));
        assertThrows(ExorcistaSinAngelesException.class, () -> tai.exorcizar(elNoba));
    }

    @Test
    void exorcizarAExorcizadoSinEspiritusDemoniacosNoLanzaExcepcionTest() {
        tai.conectarseAEspiritu(angel);
        elNoba.conectarseAEspiritu(new EspirituAngelical(90, "Uriel"));
        assertDoesNotThrow(() -> tai.exorcizar(elNoba));
    }

    @Test
    void exorcizarGanaAngelYDesvinculaAlDemonioTest() {
        EspirituAngelical uriel = new EspirituAngelical(60, "angel");
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio");
        uriel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        tai.conectarseAEspiritu(uriel);
        elNoba.conectarseAEspiritu(demonio);

        /*
            el angel tiene 70 de nivel de conexión, por lo que debería dar 5 + 70 = 75
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
        EspirituAngelical angelito = new EspirituAngelical(4, "angelito");
        tai.conectarseAEspiritu(angelito);
        angelito.disminuirConexion(10);
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio");
        elNoba.conectarseAEspiritu(demonio);
        angelito.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);

        /*
            angelito, debilitado, tiene 4 de nivelDeConexion, por lo tanto 5 + 4 = 9
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
        EspirituAngelical angelito = new EspirituAngelical(4, "angelito");
        tai.conectarseAEspiritu(angelito);
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio");
        elNoba.conectarseAEspiritu(demonio);
        angelito.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);

        /*
            angelito, debilitado, tiene 4 de nivelDeConexion, por lo tanto 5 + 14 = 19
            demonio saca una defensa de 20, gana con lo justo.

            El aftermath debería ser:
             - angel con nivelDeConexion = 14 - 5 = 9
             - NO se desvincula de tai
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(20);
        tai.exorcizar(elNoba);

        assertEquals(9, angelito.getNivelDeConexion());
        assertFalse(tai.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarGanaAngelTest() {
        EspirituAngelical uriel = new EspirituAngelical(60, "angel");
        EspirituDemoniaco demonio = new EspirituDemoniaco(30, "demonio");
        uriel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        tai.conectarseAEspiritu(uriel);
        elNoba.conectarseAEspiritu(demonio);

        /*
            angel tiene 70 de nivel de conexión, por lo que debería dar 5 + 70 = 75
            demonio tiene mala suerte asi que se va a ir obliterado

            El aftermath debería ser:
             - demonio con nivelDeConexion = 40 - (70 / 2) = 40 - 35 = 5
             - NO se desvincula de elNoba
         */
        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(30);
        tai.exorcizar(elNoba);

        assertEquals(5, demonio.getNivelDeConexion());
        assertFalse(elNoba.getEspiritus().isEmpty());
    }

    @Test
    void exorcizarVariosDemoniosTest() {
        EspirituAngelical rika = new EspirituAngelical(50, "Rika");
        EspirituAngelical ivaar = new EspirituAngelical(70, "Ivaar");
        EspirituAngelical hana = new EspirituAngelical(5, "Hana");
        tai.conectarseAEspiritu(rika);
        tai.conectarseAEspiritu(ivaar);
        tai.conectarseAEspiritu(hana);
        hana.disminuirConexion(10);

        rika.setCustomRandomizer(randomizer);
        ivaar.setCustomRandomizer(randomizer);
        hana.setCustomRandomizer(randomizer);

        EspirituDemoniaco jaeger = new EspirituDemoniaco(40, "Jaeger");
        EspirituDemoniaco noroi = new EspirituDemoniaco(56, "Noroi");

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

        EspirituAngelical rika = new EspirituAngelical(50, "Rika");
        EspirituAngelical ivaar = new EspirituAngelical(70, "Ivaar");
        EspirituAngelical hana = new EspirituAngelical(5, "Hana");
        tai.conectarseAEspiritu(rika);
        tai.conectarseAEspiritu(ivaar);
        tai.conectarseAEspiritu(hana);
        hana.disminuirConexion(10);

        rika.setCustomRandomizer(randomizer);
        ivaar.setCustomRandomizer(randomizer);
        hana.setCustomRandomizer(randomizer);

        EspirituDemoniaco jaeger = new EspirituDemoniaco(40, "Jaeger");
        EspirituDemoniaco noroi = new EspirituDemoniaco(56, "Noroi");
        EspirituAngelical hanyuu = new EspirituAngelical(100, "Hanyuu");

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

    @Test
    void mediumDescansaTieneMasManaYSusEspiritusMasEnergia()
    {
        Medium tai = new Medium("Tai", 100, 10);
        Espiritu espirituDem = new EspirituDemoniaco(0, "zorro");

        // Paso a paso, que hace la implementacion de descansar()
        tai.conectarseAEspiritu(espirituDem);
        tai.aumentarMana(15);
        tai.aumentarNivelDeConexionATodosLosEspiritus(5);

        assertEquals(25, tai.getMana());
        assertEquals(15, espirituDem.getNivelDeConexion());
    }

}
