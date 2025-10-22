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
        this.ubicacion = new Santuario("Ubicacion", 10);
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
        service.crear(new Santuario("Bernal", 10));
        service.crear(new Cementerio("Mordor", 10));
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
        Ubicacion u2 = new Santuario("Templo de Jade", 30);
        service.crear(u2);
        Medium tai = new Medium("Tai", 100, 10, ubicacion);
        Espiritu espiritu = new EspirituAngelical(50, "Maestro Shifu", u2);
        mediumService.crear(tai);
        espirituService.crear(espiritu);
        assertEquals(1, service.mediumsSinEspiritusEn(ubicacion.getId()).size());
    }

    @Test
    void unaUbicacionSeConectaConOtraYApareceEnSusConexionesTest() {
        Ubicacion ubi =service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));

        assertEquals(ubi.getConexiones().size(), 0);

        service.conectar(ubi.getId(), ubi2.getId(), 10L);

        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(ubiConectada.getConexiones().size(), 1);
    }

    @Test
    void unaUbicacionSeConectaConOtraBidireccionalmenteYAparecenEnSusConexionesTest() {
        Ubicacion ubi =service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));

        assertEquals(ubi.getConexiones().size(), 0);
        assertEquals(ubi2.getConexiones().size(), 0);

        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        service.conectar(ubi2.getId(), ubi.getId(), 10L);

        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));

        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(ubiConectada2.getConexiones().stream().findFirst().get().getDestino().getId(), ubi.getId());
    }


    @Test
    void unaUbicacionSeConectaConOtraUnidireccionalmenteYLaSegundaNoTieneConexionesTest() {
        Ubicacion ubi =service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));

        assertEquals(ubi.getConexiones().size(), 0);
        assertEquals(ubi2.getConexiones().size(), 0);

        service.conectar(ubi.getId(), ubi2.getId(), 10L);

        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));

        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(ubiConectada2.getConexiones().size(), 0);
    }


    @Test
    void unaUbicacionSeConectaConOtraYEstanConectadasTest() {
        Ubicacion ubi =service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));

        service.conectar(ubi.getId(), ubi2.getId(), 10L);

        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));

        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());

        assertTrue(service.estanConectadas(ubiConectada.getId(), ubi2.getId()));
    }

    @Test
    void unaUbicacionNoEstaConectadaConOtraTest() {
        Ubicacion ubi =service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));

        assertEquals(ubi.getConexiones().size(), 0);
        assertFalse(service.estanConectadas(ubi.getId(), ubi2.getId()));
    }


    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        service.eliminarTodo();
    }

}
