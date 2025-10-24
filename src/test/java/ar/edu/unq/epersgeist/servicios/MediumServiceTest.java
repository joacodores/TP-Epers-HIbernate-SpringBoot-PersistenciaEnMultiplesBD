package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionLejanaException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MediumServiceTest {

    @Autowired
    private MediumService service;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;
    private Ubicacion santuario;
    private Ubicacion cementerio;

    @BeforeEach
    void prepare() {
        santuario = ubicacionService.crear(new Santuario("Santuario base", 10));
        cementerio = ubicacionService.crear(new Cementerio("Cementerio base", 20));
    }

    @Test
    void crearMediumTest() {
        Medium medium = new Medium("Thiago", 50, 30, santuario);
        assertNull(medium.getId());
        service.crear(medium);
        assertNotNull(medium.getId());
    }

    @Test
    void recuperarMediumNoPersistidoLanzaExcepcionTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        assertThrows(MediumNoEncontradoException.class, () -> service.recuperar(medium.getId() + 1));
    }

    @Test
    void recuperarMediumTest() {
        Medium medium = new Medium("Thiago", 50, 30, santuario);
        Long id = service.crear(medium).getId();
        Medium recuperado = service.recuperar(id).orElseThrow();
        assertAll(
                () -> assertEquals(medium.getNombre(), recuperado.getNombre()),
                () -> assertEquals(medium.getMana(), recuperado.getMana()),
                () -> assertEquals(medium.getManaMax(), recuperado.getManaMax())
        );
    }

    @Test
    void actualizarMediumTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        medium.setNombre("Churrito");
        service.actualizar(medium);
        Medium recuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals("Churrito", recuperado.getNombre());
    }

    @Test
    void recuperarTodosTest() {
        service.crear(new Medium("Thiago", 50, 30, santuario));
        service.crear(new Medium("Juan", 80, 20, santuario));
        service.crear(new Medium("Jorge", 20, 10, santuario));
        assertEquals(
                List.of("Thiago", "Juan", "Jorge"),
                service.recuperarTodos().stream().map(Medium::getNombre).toList()
        );
    }

    @Test
    void sePuedenPersistirVariosMediumConMismoNombreTest() {
        service.crear(new Medium("Thiago", 50, 30, santuario));
        service.crear(new Medium("Thiago", 80, 20, santuario));
        service.crear(new Medium("Thiago", 20, 10, santuario));
        assertEquals(
                List.of("Thiago", "Thiago", "Thiago"),
                service.recuperarTodos().stream().map(Medium::getNombre).toList()
        );
    }

    @Test
    void eliminarMediumTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(medium.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void unMediumNoTieneNingunEspirituTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        assertTrue(service.espiritus(medium.getId()).isEmpty());
    }

    @Test
    void unMediumTieneEspiritusTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        Espiritu e1 = espirituService.crear(new EspirituAngelical(100, "angel", cementerio));
        Espiritu e2 = espirituService.crear(new EspirituAngelical(40, "angel", santuario));
        service.invocar(medium.getId(), e1.getId());
        service.invocar(medium.getId(), e2.getId());
        espirituService.conectar(e1.getId(), medium.getId());
        espirituService.conectar(e2.getId(), medium.getId());
        List<Espiritu> espiritus = service.espiritus(medium.getId());
        assertEquals(2, espiritus.size());
    }

    @Test
    void exorcizarConExorcistaSinEspiritusAngelicalesLanzaExcepcionTest() {
        Medium exorcista = service.crear(new Medium("Thiago", 50, 30, santuario));
        Medium poseido = service.crear(new Medium("Ozzy", 100, 100, santuario));
        assertThrows(ExorcistaSinAngelesException.class, () ->
                service.exorcizar(exorcista.getId(), poseido.getId())
        );
    }

    @Test
    void exorcizarTest() {
        Medium tai = service.crear(new Medium("Tai", 100, 80, santuario));
        Medium elNoba = service.crear(new Medium("El Noba", 100, 100, santuario));
        EspirituAngelical angel = new EspirituAngelical(100, "angel", santuario);
        EspirituDemoniaco demonio = new EspirituDemoniaco(0, "demonio", santuario);
        RandomizerFalso randomizer = new RandomizerFalso();
        angel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);
        Long angelId = espirituService.crear(angel).getId();
        Long demonioId = espirituService.crear(demonio).getId();
        randomizer.setSecuenciaDeAtaques(10);
        randomizer.setSecuenciaDeDefensas(0);
        espirituService.conectar(angelId, tai.getId());
        espirituService.conectar(demonioId, elNoba.getId());
        service.exorcizar(tai.getId(), elNoba.getId());
        assertTrue(service.espiritus(elNoba.getId()).isEmpty());
    }

    @Test
    void descansarTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        service.descansar(medium.getId());
        Medium recuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(45, recuperado.getMana());
    }

    @Test
    void invocarTest() {
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuario));
        Espiritu e1 = espirituService.crear(new EspirituAngelical(100, "angel", cementerio));
        medium.aumentarMana(30);
        service.invocar(medium.getId(), e1.getId());
        Espiritu actualizado = espirituService.recuperar(e1.getId()).orElseThrow();
        assertEquals(santuario.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void moverTest() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacion = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Thiago", 50, 30, ubicacion));
        service.mover(medium.getId(), cementerio.getId());
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(cementerio.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void moverMediumMueveTodosSusEspiritus() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacion = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Thiago", 50, 30, ubicacion));
        Espiritu e = espirituService.crear(new EspirituAngelical(100, "angel", ubicacion));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(), cementerio.getId());
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(cementerio.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void espirituAngelicalDisminuyeNivelDeConexionPorLlegarAUnCementerio() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacion = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacion));
        Espiritu e = espirituService.crear(new EspirituAngelical(100, "angel", ubicacion));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(), cementerio.getId());
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(95, actualizado.getNivelDeConexion());
    }

    @Test
    void espirituAngelicalSeDesvinculaAlLlegarANivelCero() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Thiago", 30, 20, ubicacionRecuperada));
        Espiritu angel = espirituService.crear(new EspirituAngelical(1, "angel", ubicacionRecuperada));
        espirituService.conectar(angel.getId(), medium.getId());
        service.mover(medium.getId(), cementerio.getId());
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(0, actualizado.getEspiritus().size());
    }

    @Test
    void espirituDemoniacoDisminuyeNivelDeConexionPorLlegarAUnSantuario() {
        ubicacionService.conectar(cementerio.getId(), santuario.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(cementerio.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        Espiritu e = espirituService.crear(new EspirituDemoniaco(100, "demonio", ubicacionRecuperada));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(), santuario.getId());
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(90, actualizado.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesvinculaAlLlegarANivelCero() {
        ubicacionService.conectar(cementerio.getId(), santuario.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(cementerio.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        Espiritu demonio = espirituService.crear(new EspirituDemoniaco(1, "demonio", ubicacionRecuperada));
        espirituService.conectar(demonio.getId(), medium.getId());
        service.mover(medium.getId(), santuario.getId());
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(0, actualizado.getEspiritus().size());
    }

    @Test
    void moverAUnaUbicacionNoConectadaLanzaExcepcionTest() {
        Medium medium = service.crear(new Medium("Juan", 30, 20, santuario));
        assertThrows(UbicacionLejanaException.class, () -> service.mover(medium.getId(), cementerio.getId()));
    }

    @Test
    void moverAUnaUbicacionConectadaNoLanzaExcepcionTest() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        service.mover(medium.getId(), cementerio.getId());
        Medium mediumRecuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(mediumRecuperado.getUbicacion().getId(), cementerio.getId());
    }

    @Test
    void mediumEsEliminadoAlQuedarseSinManaAlMoverseAUnaUbicacionTest() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 20, 5, ubicacionRecuperada));
        service.mover(medium.getId(), cementerio.getId());
        assertThrows(MediumNoEncontradoException.class, () -> service.recuperar(medium.getId()));
    }

    @Test
    void mediumNoEsEliminadoAlNoQuedarseSinManaLuegoDeMoverseTest() {
        ubicacionService.conectar(santuario.getId(), cementerio.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 50, 20, ubicacionRecuperada));
        Long mediumId = medium.getId();
        service.mover(medium.getId(), cementerio.getId());
        Medium mediumRecuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(mediumRecuperado.getId(), mediumId);
    }

    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        service.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
