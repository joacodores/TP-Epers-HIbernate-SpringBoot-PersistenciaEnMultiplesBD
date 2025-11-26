package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.NightBringerNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.NightBringer;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
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
public class NightBringerServiceTest {
    @Autowired
    private NightBringerService service;

    private NightBringer nightBringer;

    @Autowired
    private UbicacionService ubicacionService;
    private Ubicacion ubicacion;
    private Set<Coordenada> coordsBA;

    @BeforeEach
    void prepare() {
        coordsBA = Set.of(
                new Coordenada(-34.6037, -58.3816),
                new Coordenada(-34.6100, -58.3852),
                new Coordenada(-34.6075, -58.3748)
        );
        this.ubicacion = new Santuario("Ubicacion", 10, coordsBA);
        nightBringer = new NightBringer("Zoro");
    }

    @Test
    void crearNightBringerTest() {
        assertNull(nightBringer.getId());
        service.crear(nightBringer);
        assertNotNull(nightBringer.getId());
    }

    @Test
    void recuperarNightBringerNoExistenteLanzaExcepcion() {
        Long nightBringerId = service.crear(nightBringer).getId();
        assertThrows(NightBringerNoEncontradoException.class, () -> service.recuperar(nightBringerId + 1));
    }

    @Test
    void recuperarNightBringerTest() {
        Long nightBringerId = service.crear(nightBringer).getId();
        NightBringer nightBringerRecuperado = service.recuperar(nightBringerId);
        assertEquals(nightBringer.getNombre(), nightBringerRecuperado.getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunNightBringerDevuelveListaVaciaTest() {
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosDevuelveLosNightBringersTest() {
        service.crear(nightBringer);
        service.crear(new NightBringer("Sanji"));
        service.crear(new NightBringer("Jinbe"));
        assertEquals(List.of("Zoro", "Sanji", "Jinbe"), service.recuperarTodos().stream().map(NightBringer::getNombre).toList());
    }

    @Test
    void actualizarNightBringerTest() {
        Long nightBringerId = service.crear(nightBringer).getId();
        nightBringer.setNombre("Sanji");
        service.actualizar(nightBringer);
        NightBringer nightBringerRecuperado = service.recuperar(nightBringerId);
        assertEquals(nightBringer.getNombre(), nightBringerRecuperado.getNombre());
    }

    @Test
    void eliminarNightBringerTest() {
        service.crear(nightBringer);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(nightBringer.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void unNightBringerNoTieneEspiritusSpawneadosTest() {
        NightBringer nightBringerCreado = service.crear(nightBringer);
        assertTrue(nightBringerCreado.getEspiritus().isEmpty());
    }

    @Test
    void unNightBringerTieneEspiritusSpawneadosTest() {
        Long ubicacionId = ubicacionService.crear(ubicacion).getId();
        Long nightBringerId = service.crear(nightBringer).getId();
        service.spawnearEspirituEnUbicacion(nightBringerId, ubicacionId, "Kaido");
        NightBringer nightBringerRecuperado = service.recuperar(nightBringerId);
        assertEquals(nightBringerRecuperado.getEspiritus().size(), 1);
    }

    @AfterEach
    void cleanup() {
        service.eliminarTodo();
    }
}
