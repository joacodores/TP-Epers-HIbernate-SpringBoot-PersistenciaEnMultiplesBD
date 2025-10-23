package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.servicios.exceptions.UbicacionesNoConectadasException;
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
        assertThrows(UbicacionNoEncontradaException.class, () -> service.recuperar(ubicacionID + 1));
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
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));
        assertEquals(0, ubi.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(1, ubiConectada.getConexiones().size());
    }

    @Test
    void unaUbicacionSeConectaConOtraBidireccionalmenteYAparecenEnSusConexionesTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));
        assertEquals(0, ubi.getConexiones().size());
        assertEquals(0, ubi2.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        service.conectar(ubi2.getId(), ubi.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(ubiConectada2.getConexiones().stream().findFirst().get().getDestino().getId(), ubi.getId());
    }

    @Test
    void unaUbicacionSeConectaConOtraUnidireccionalmenteYLaSegundaNoTieneConexionesTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));
        assertEquals(0, ubi.getConexiones().size());
        assertEquals(0, ubi2.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertEquals(0, ubiConectada2.getConexiones().size());
    }

    @Test
    void unaUbicacionSeConectaConOtraYEstanConectadasTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        assertEquals(ubiConectada.getConexiones().stream().findFirst().get().getDestino().getId(), ubi2.getId());
        assertTrue(service.estanConectadas(ubiConectada.getId(), ubi2.getId()));
    }

    @Test
    void unaUbicacionNoEstaConectadaConOtraTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10));
        assertEquals(0, ubi.getConexiones().size());
        assertFalse(service.estanConectadas(ubi.getId(), ubi2.getId()));
    }

    @Test
    void caminoMasCortoTest() {
        Ubicacion quilmesEste = service.crear(new Santuario("Quilmes Este", 10));
        Ubicacion quilmesOeste = service.crear(new Santuario("Quilmes Oeste", 10));
        Ubicacion bernal = service.crear(new Santuario("Bernal", 10));
        service.conectar(quilmesEste.getId(), quilmesOeste.getId(), 10L);
        service.conectar(quilmesOeste.getId(), bernal.getId(), 10L);
        service.conectar(quilmesEste.getId(), bernal.getId(), 10L);
        /*
        Quilmes Este -------------> Bernal
                   \               /
                    \             /
                     Quilmes Oeste
        */
        assertEquals(List.of(quilmesEste.getId(), bernal.getId()),
                service.caminoMasCorto(quilmesEste.getId(), bernal.getId())
                        .stream()
                        .map(Ubicacion::getId)
                        .toList());
    }

    @Test
    void caminoMasCortoCuandoNoEstanConectadasLanzaExcepcionTest() {
        Ubicacion quilmesEste = service.crear(new Santuario("Quilmes Este", 10));
        Ubicacion quilmesOeste = service.crear(new Santuario("Quilmes Oeste", 10));
        Ubicacion bernal = service.crear(new Santuario("Bernal", 10));
        service.conectar(quilmesEste.getId(), quilmesOeste.getId(), 10L);
        service.conectar(quilmesEste.getId(), bernal.getId(), 10L);

        /*
        Quilmes Este --------------> Bernal
                   \
                    \
                     Quilmes Oeste
        */
        assertThrows(UbicacionesNoConectadasException.class, () -> service.caminoMasCorto(quilmesOeste.getId(), bernal.getId()));
    }

    @Test
    void caminoMasCortoConDosCaminosDeIgualDistanciaDevuelveCualquieraDeEllosTest() {
        Ubicacion bernalEste = service.crear(new Santuario("Bernal Este", 10));
        Ubicacion bernalOeste = service.crear(new Santuario("Bernal Oeste", 10));
        Ubicacion wilde = service.crear(new Santuario("Wilde", 10));
        Ubicacion quilmes = service.crear(new Santuario("Quilmes", 10));
        service.conectar(bernalEste.getId(), wilde.getId(), 10L);
        service.conectar(bernalEste.getId(), quilmes.getId(), 10L);
        service.conectar(wilde.getId(), bernalOeste.getId(), 10L);
        service.conectar(quilmes.getId(), bernalOeste.getId(), 10L);
        /*

                         Wilde
                     /           \
                    /             \
        Bernal Este                 Bernal Oeste
                   \               /
                    \             /
                        Quilmes
        */
        List<Long> resultado = service.caminoMasCorto(bernalEste.getId(), bernalOeste.getId())
                .stream()
                .map(Ubicacion::getId)
                .toList();
        List<List<Long>> caminosValidos = List.of(
                List.of(bernalEste.getId(), quilmes.getId(), bernalOeste.getId()),
                List.of(bernalEste.getId(), wilde.getId(), bernalOeste.getId())
        );
        assertTrue(caminosValidos.stream().anyMatch(camino -> camino.equals(resultado)));
    }

    @Test
    void caminoMasCortoConCicloNoSeEnredaTest() {
        Ubicacion a = service.crear(new Santuario("A", 5));
        Ubicacion b = service.crear(new Santuario("B", 5));
        Ubicacion c = service.crear(new Santuario("C", 5));
        service.conectar(a.getId(), b.getId(), 5L);
        service.conectar(b.getId(), c.getId(), 5L);
        service.conectar(c.getId(), a.getId(), 5L);
        assertEquals(
                List.of(a.getId(), b.getId(), c.getId()),
                service.caminoMasCorto(a.getId(), c.getId())
                        .stream()
                        .map(Ubicacion::getId)
                        .toList()
        );
    }

    @Test
    void caminoMasCortoConOrigenIgualADestinoTest() {
        Ubicacion a = service.crear(new Santuario("A", 5));
        assertEquals(
                List.of(a.getId()),
                service.caminoMasCorto(a.getId(), a.getId())
                        .stream()
                        .map(Ubicacion::getId)
                        .toList()
        );
    }

    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        service.eliminarTodo();
    }

}
