package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionLejanaException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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
    private Ubicacion cementerioMadero;
    private Set<Coordenada> coordsBA;
    private Set<Coordenada> coordsCBA;
    private Set<Coordenada> coordPuertoMadero;
    private Coordenada coordPruebaBA;
    private Coordenada coordPruebaCBA;
    private Coordenada coordPruebaMZA;
    @Autowired
    private MediumRepository mediumRepository;
    @Autowired
    private UbicacionRepository ubicacionRepository;


    @BeforeEach
    void prepare() {
        coordsBA = Set.of(
                new Coordenada(-34.6037, -58.3816),
                new Coordenada(-34.6100, -58.3852),
                new Coordenada(-34.6075, -58.3748)
        );
        coordsCBA = Set.of(
                new Coordenada(-31.4201, -64.1888),
                new Coordenada(-31.4259, -64.1905),
                new Coordenada(-31.4172, -64.1810)
        );
        coordPuertoMadero = Set.of(
                new Coordenada(-34.6055, -58.3705),
                new Coordenada(-34.6105, -58.3725),
                new Coordenada(-34.6070, -58.3650)
        );
        santuario = ubicacionService.crear(new Santuario("Santuario base", 10, coordsBA));
        cementerio = ubicacionService.crear(new Cementerio("Cementerio base", 20, coordsCBA));
        cementerioMadero = ubicacionService.crear(new Cementerio("Cementerio Madero", 20, coordPuertoMadero));
        coordPruebaBA = new Coordenada(-34.60707, -58.38053);
        coordPruebaCBA = new Coordenada(-31.42107, -64.18677);
        coordPruebaMZA = new Coordenada(-32.8883, -68.84143);
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
        Espiritu e2 = espirituService.crear(new EspirituAngelical(40, "angel", santuario));
        service.invocar(medium.getId(), e2.getId());
        espirituService.conectar(e2.getId(), medium.getId());
        List<Espiritu> espiritus = service.espiritus(medium.getId());
        assertEquals(1, espiritus.size());
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
        Espiritu e1 = espirituService.crear(new EspirituAngelical(100, "angel", santuario));
        medium.aumentarMana(30);
        service.invocar(medium.getId(), e1.getId());
        Espiritu actualizado = espirituService.recuperar(e1.getId()).orElseThrow();
        assertEquals(santuario.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void moverTest() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion santuarioActualizado = ubicacionService.recuperar(santuario.getId()).get();
        Medium medium = service.crear(new Medium("Thiago", 50, 30, santuarioActualizado));
        service.mover(medium.getId(),-34.6078, -58.3690);
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(cementerioMadero.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void moverMediumMueveTodosSusEspiritus() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacion = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Thiago", 50, 30, ubicacion));
        Espiritu e = espirituService.crear(new EspirituAngelical(100, "angel", ubicacion));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(),-34.6078, -58.3690);
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(cementerioMadero.getId(), actualizado.getUbicacion().getId());
    }

    @Test
    void espirituAngelicalDisminuyeNivelDeConexionPorLlegarAUnCementerio() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacion = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacion));
        Espiritu e = espirituService.crear(new EspirituAngelical(100, "angel", ubicacion));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(),-34.6078, -58.3690);
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(95, actualizado.getNivelDeConexion());
    }

    @Test
    void espirituAngelicalSeDesvinculaAlLlegarANivelCero() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Thiago", 30, 20, ubicacionRecuperada));
        Espiritu angel = espirituService.crear(new EspirituAngelical(1, "angel", ubicacionRecuperada));
        espirituService.conectar(angel.getId(), medium.getId());
        service.mover(medium.getId(),-34.6078, -58.3690);
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(0, actualizado.getEspiritus().size());
    }




    @Test
    void espirituDemoniacoDisminuyeNivelDeConexionPorLlegarAUnSantuario() {
        ubicacionService.conectar(cementerioMadero.getId(), santuario.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(cementerioMadero.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        Espiritu e = espirituService.crear(new EspirituDemoniaco(100, "demonio", ubicacionRecuperada));
        espirituService.conectar(e.getId(), medium.getId());
        service.mover(medium.getId(), -34.6070667, -58.3805333);
        Espiritu actualizado = espirituService.recuperar(e.getId()).orElseThrow();
        assertEquals(90, actualizado.getNivelDeConexion());
    }

    @Test
    void espirituDemoniacoSeDesvinculaAlLlegarANivelCero() {
        ubicacionService.conectar(cementerioMadero.getId(), santuario.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(cementerioMadero.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        Espiritu demonio = espirituService.crear(new EspirituDemoniaco(1, "demonio", ubicacionRecuperada));
        espirituService.conectar(demonio.getId(), medium.getId());
        service.mover(medium.getId(), -34.6070667, -58.3805333);
        Medium actualizado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(0, actualizado.getEspiritus().size());
    }

    @Test
    void moverAUnaUbicacionNoConectadaLanzaExcepcionTest() {
        Medium medium = service.crear(new Medium("Juan", 30, 20, santuario));
        assertThrows(UbicacionLejanaException.class, () -> service.mover(medium.getId(), -31.4259, -64.1905));
    }

    @Test
    void moverAUnaUbicacionConectadaNoLanzaExcepcionTest() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 30, 20, ubicacionRecuperada));
        service.mover(medium.getId(),-34.6078, -58.3690);
        Medium mediumRecuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(mediumRecuperado.getUbicacion().getId(), cementerioMadero.getId());
    }

    @Test
    void mediumEsEliminadoAlQuedarseSinManaAlMoverseAUnaUbicacionTest() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 20, 5, ubicacionRecuperada));
        service.mover(medium.getId(),-34.6078, -58.3690);
        assertThrows(MediumNoEncontradoException.class, () -> service.recuperar(medium.getId()));
    }

    @Test
    void mediumNoEsEliminadoAlNoQuedarseSinManaLuegoDeMoverseTest() {
        ubicacionService.conectar(santuario.getId(), cementerioMadero.getId(), 10L);
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(santuario.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        Medium medium = service.crear(new Medium("Juan", 50, 20, ubicacionRecuperada));
        Long mediumId = medium.getId();
        service.mover(medium.getId(),-34.6078, -58.3690);
        Medium mediumRecuperado = service.recuperar(medium.getId()).orElseThrow();
        assertEquals(mediumRecuperado.getId(), mediumId);
    }

    @Test
    void mediumSeCreaEnCoordenadaRandomDeUbicacion(){
        Medium m = mediumRepository.crear(new Medium("Juan", 30, 20, santuario));

        assertTrue(ubicacionRepository.estaDentroDe(m.getUbicacion().getId(), m.getCoordenada()));
    }

    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        service.eliminarTodo();
        ubicacionService.eliminarTodo();
    }

}
