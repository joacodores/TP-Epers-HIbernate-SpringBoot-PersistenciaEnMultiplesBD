package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        this.ubicacion = new Ubicacion("Ubicacion");

    }

    @Test
    void crearUbicacionTest() {
        assertNull(ubicacion.getId());
        service.crear(ubicacion);
        assertNotNull(ubicacion.getId());
    }



    @Test
    void recuperarUbicacionNoPersistidaDevuelveNullTest() {
        Long ubicacionID = service.crear(ubicacion).getId();

        assertTrue(service.recuperar(ubicacionID + 1).isEmpty());
    }


    @Test
    void recuperarUbicacionTest() {
        Long ubicacionID = service.crear(ubicacion).getId();
        assertEquals(ubicacion.getNombre(), service.recuperar(ubicacionID).get().getNombre());
    }

    @Test
    void actualizarUbicacionTest() {
        Long ubicacionID = service.crear(ubicacion).getId();
        ubicacion.setNombre("Berazategui");
        service.actualizar(ubicacion);
        assertEquals(ubicacion.getNombre(), service.recuperar(ubicacionID).get().getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunObjetoDevuelveListaVaciaTest() {
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosDevuelveLasUbicacionesEnOrdenAscendentePorNombreTest() {
        service.crear(ubicacion);
        service.crear(new Ubicacion("Bernal"));
        service.crear(new Ubicacion("Mordor"));
        assertEquals(List.of("Ubicacion","Bernal", "Mordor"), service.recuperarTodos().stream().map(Ubicacion::getNombre).toList());
    }

    @Test
    void eliminarUbicacionNoPersistidaNoLanzaExcepcionTest() {
        service.crear(ubicacion);
        Ubicacion ubicacion2 = new Ubicacion("Nueva Ubicación");
        assertDoesNotThrow(() -> service.eliminar(ubicacion2));
    }

    @Test
    void eliminarUbicacionTest() {
        service.crear(ubicacion);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(ubicacion);
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
        ubicacion.agregarEspiritu(espiritu);
        ubicacion.agregarEspiritu(demonio);
        service.crear(ubicacion);

        var espiritus = service.espiritusEn(ubicacion.getId());
        assertEquals(2, espiritus.size());
        assertEquals("Luffy", espiritus.get(0).getNombre());
        assertEquals("Zoro", espiritus.get(1).getNombre());
    }


    @Test
    void existeUnMediumSinEspirituEn() {
        service.crear(ubicacion);
        Ubicacion u2 = new Ubicacion("Templo de Jade");
        service.crear(u2);

        Medium tai = new Medium("Tai", 100, 10, ubicacion);
        Espiritu espiritu = new EspirituAngelical(50, "Maestro Shifu", u2);

        mediumService.crear(tai);
        espirituService.crear(espiritu);

        assertEquals(1, service.mediumsSinEspiritusEn(ubicacion.getId()).size());
    }

    @AfterEach
    void cleanup() {
        // espirituService.eliminarTodo();
        //mediumService.eliminarTodo();
        service.eliminarTodo();
    }



}
