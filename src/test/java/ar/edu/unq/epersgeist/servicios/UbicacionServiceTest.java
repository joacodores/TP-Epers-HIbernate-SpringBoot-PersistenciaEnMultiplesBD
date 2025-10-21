package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
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
public class UbicacionServiceTest {

    @Autowired
    private UbicacionService service;
    private Ubicacion ubicacion;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private EspirituService espirituService;

    @BeforeEach
    void prepare() {
        this.ubicacion = new Santuario("Ubicacion", 10, 10L);
    }

    @Test
    void crearUbicacionTest() {
        assertNull(ubicacion.getId());
        service.crear(ubicacion);
        assertNotNull(ubicacion.getId());
    }

    @Test
    void recuperarUbicacionNoExistenteLanzaExcepcion() {
        Long ubicacionID = service.crear(ubicacion).getId();
        assertThrows(UbicacionNoEncontradaException.class, () -> {
            service.recuperar(ubicacionID + 1);
        });
    }


    @Test
    void recuperarUbicacionTest() {
        Long ubicacionID = service.crear(ubicacion).getId();
        Ubicacion ubicacionRecuperada = service.recuperar(ubicacionID)
                .orElseThrow(() -> new AssertionError("El ubicacion no existe"));
        assertEquals(ubicacion.getNombre(), ubicacionRecuperada.getNombre());
    }

    @Test
    void actualizarUbicacionTest() {
        Long ubicacionID = service.crear(ubicacion).getId();
        ubicacion.setNombre("Berazategui");
        service.actualizar(ubicacion);
        Ubicacion ubicacionRecuperada = service.recuperar(ubicacionID)
                .orElseThrow(() -> new AssertionError("El ubicacion no existe"));
        assertEquals(ubicacion.getNombre(), ubicacionRecuperada.getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunObjetoDevuelveListaVaciaTest() {
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosDevuelveLasUbicacionesEnOrdenAscendentePorNombreTest() {
        service.crear(ubicacion);
        service.crear(new Santuario("Bernal", 10, 10L));
        service.crear(new Cementerio("Mordor", 10, 10L));
        assertEquals(List.of("Ubicacion", "Bernal", "Mordor"), service.recuperarTodos().stream().map(Ubicacion::getNombre).toList());
    }

    /*
        @Test
        void eliminarUbicacionNoPersistidaNoLanzaExcepcionTest() {
            service.crear(ubicacion);
            Ubicacion ubicacion2 = new Cementerio("Nueva Ubicación", 50);
            assertDoesNotThrow(() -> service.eliminar(ubicacion2.getId()));
        }
    */
    @Test
    void eliminarUbicacionTest() {
        service.crear(ubicacion);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(ubicacion.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void noHayEspiritusEnUnaUbicacionDadaTest() {
        service.crear(ubicacion);
        assertTrue(service.espiritusEn(ubicacion.getId()).isEmpty());
    }

    @Test
    void existenEspiritusEnUnaUbicacionDadaTest() {
        Espiritu espiritu = new EspirituAngelical(50, "Luffy", ubicacion);
        Espiritu demonio = new EspirituDemoniaco(45, "Zoro", ubicacion);
        service.crear(ubicacion);
        var espiritus = service.espiritusEn(ubicacion.getId());
        assertEquals(2, espiritus.size());
        assertEquals("Luffy", espiritus.get(0).getNombre());
        assertEquals("Zoro", espiritus.get(1).getNombre());
    }

    @Test
    void existeUnMediumSinEspirituEn() {
        service.crear(ubicacion);
        Ubicacion u2 = new Santuario("Templo de Jade", 30, 10L);
        service.crear(u2);
        Medium tai = new Medium("Tai", 100, 10, ubicacion);
        Espiritu espiritu = new EspirituAngelical(50, "Maestro Shifu", u2);
        mediumService.crear(tai);
        espirituService.crear(espiritu);
        assertEquals(1, service.mediumsSinEspiritusEn(ubicacion.getId()).size());
    }

    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        service.eliminarTodo();
    }

}
