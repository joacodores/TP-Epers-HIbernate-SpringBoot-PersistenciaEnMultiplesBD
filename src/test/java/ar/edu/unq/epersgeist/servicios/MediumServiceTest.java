package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MediumServiceTest {
    private Ubicacion ubi;
    private Ubicacion ubi2;

    @Autowired
    private MediumService service;
    private Medium medium;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;
    private Espiritu e1;

    @BeforeEach
    void prepare() {
        ubi = new Santuario("ubi", 10);
        ubicacionService.crear(ubi);
        ubi2 = new Cementerio("Ubicación 2", 20);
        ubicacionService.crear(ubi2);
        e1 = new EspirituAngelical(100, "angel", ubi2);
        espirituService.crear(e1);
        this.medium = new Medium("Thiago", 50, 30, ubi);
    }

    @Test
    void crearMediumTest() {
        assertNull(medium.getId());
        service.crear(medium);
        assertNotNull(medium.getId());
    }

    @Test
    void recuperarMediumNoPersistidoDevuelveNullTest() {
        Long mediumID = service.crear(medium).getId();
        assertTrue(service.recuperar(mediumID + 1).isEmpty());
    }

    @Test
    void recuperarMediumTest() {
        Long mediumID = service.crear(medium).getId();
        Medium mediumRecuperado = service.recuperar(mediumID)
                .orElseThrow(() -> new AssertionError("El medium no existe"));
        assertAll(
                () -> assertEquals(medium.getNombre(), mediumRecuperado.getNombre()),
                () -> assertEquals(medium.getMana(), mediumRecuperado.getMana()),
                () -> assertEquals(medium.getManaMax(), mediumRecuperado.getManaMax())
        );
    }

    @Test
    void actualizarMediumTest() {
        Long mediumID = service.crear(medium).getId();
        medium.setNombre("Churrito");
        service.actualizar(medium);
        Medium mediumRecuperado = service.recuperar(mediumID)
                .orElseThrow(() -> new AssertionError("El medium no existe"));
        assertEquals(medium.getNombre(), mediumRecuperado.getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunObjetoDevuelveListaVaciaTest() {
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosTest() {
        service.crear(medium);
        service.crear(new Medium("Juan", 80, 20, ubi));
        service.crear(new Medium("Jorge", 20, 10, ubi));
        assertEquals(List.of("Thiago", "Juan", "Jorge"), service.recuperarTodos().stream().map(Medium::getNombre).toList());
    }

    @Test
    void sePuedenPersistirVariosMediumConMismoNombreTest() {
        service.crear(medium);
        service.crear(new Medium("Thiago", 80, 20, ubi));
        service.crear(new Medium("Thiago", 20, 10, ubi));
        assertEquals(List.of("Thiago", "Thiago", "Thiago"), service.recuperarTodos().stream().map(Medium::getNombre).toList());
    }

    /*@Test
    void eliminarMediumNoPersistidoNoLanzaExcepcionTest() {
        service.crear(medium);
        Medium medium2 = new Medium("Doble Tonka", 60, 30, ubi);
        assertDoesNotThrow(() -> service.eliminar(medium2.getId()));
    }

    @Test
    void eliminarMediumTest() {
        service.crear(medium);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(medium.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }
*/
    @Test
    void unMediumNoTieneNingunEspirituTest() {
        service.crear(medium);
        assertTrue(service.espiritus(medium.getId()).isEmpty());
    }

    @Test
    void unMediumTieneEspiritusTest() {
        Medium m1 = service.crear(medium);
        Espiritu e2 = new EspirituAngelical(40, "angel", ubi);
        espirituService.crear(e2);
        service.invocar(m1.getId(), e1.getId());
        service.invocar(m1.getId(), e2.getId());
        espirituService.conectar(e1.getId(), m1.getId());
        espirituService.conectar(e2.getId(), m1.getId());
        List<Espiritu> espiritus = service.espiritus(medium.getId());
        assertEquals(2, espiritus.size());
    }

    @Test
    void exorcizarConExorcistaSinEspiritusAngelicalesLanzaExcepcionTest() {
        Medium ozzy = new Medium("ozzy", 100, 100, ubi);
        Long exorcistaId = service.crear(medium).getId();
        Long ozzyId = service.crear(ozzy).getId();
        assertThrows(ExorcistaSinAngelesException.class, () -> service.exorcizar(exorcistaId, ozzyId));
    }

    @Test
    void exorcizarTest() {
        Medium tai = new Medium("Tai", 100, 80, ubi);
        Medium elNoba = new Medium("El Noba", 100, 100, ubi);
        Long taiId = service.crear(tai).getId();
        Long elNobaId = service.crear(elNoba).getId();
        EspirituAngelical angel = new EspirituAngelical(100, "angel", ubi);
        EspirituDemoniaco demonio = new EspirituDemoniaco(0, "demonio", ubi);
        RandomizerFalso randomizer = new RandomizerFalso();
        angel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        Long angelId = espirituService.crear(angel).getId();
        Long demonioId = espirituService.crear(demonio).getId();
        randomizer.setSecuenciaDeAtaques(10);
        randomizer.setSecuenciaDeDefensas(0);
        espirituService.conectar(angelId, taiId);
        espirituService.conectar(demonioId, elNobaId);
        service.exorcizar(taiId, elNobaId);
        assertTrue(service.espiritus(elNobaId).isEmpty());
    }

    @Test
    void descansarTest() {
        Medium m1 = service.crear(medium);
        Long mediumID = m1.getId();
        service.descansar(mediumID);
        Medium m2 = service.recuperar(mediumID)
                .orElseThrow(() -> new AssertionError("Medium no encontrado"));
        assertEquals(45, m2.getMana());
    }

    @Test
    void invocarTest() {
        Medium m1 = service.crear(medium);
        m1.aumentarMana(30);
        Long mediumID = m1.getId();
        Long espirituId = e1.getId();
        assertEquals(e1.getUbicacion(), ubi2);
        e1 = service.invocar(mediumID, espirituId);
        assertEquals(e1.getUbicacion().getId(), ubi.getId());
    }

    @AfterEach
    void cleanup() {
        //espirituService.eliminarTodo();
        service.eliminarTodo();
        ubicacionService.eliminarTodo();
    }
}
