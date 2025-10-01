package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceTest {
    @Autowired
    private EspirituService service;
    private EspirituAngelical angel;
    private EspirituDemoniaco demonio;

    @Autowired
    private UbicacionService ubicacionService;
    private Ubicacion eastblue;

    @Autowired
    private MediumService mediumService;
    private Medium sanji;

    @BeforeEach
    void prepare() {
        eastblue = new Ubicacion("East Blue");
        ubicacionService.crear(eastblue);
        sanji = new Medium("Sanji", 60, 30, eastblue);
        mediumService.crear(sanji);
        this.angel = new EspirituAngelical(58, "Luffy", eastblue);
        this.demonio = new EspirituDemoniaco(36, "Zoro", eastblue);
    }

    @Test
    void crearEspirituTest() {
        assertNull(angel.getId());
        service.crear(angel);
        assertNotNull(angel.getId());
    }

    @Test
    void recuperarEspirituNoPersistidoDevuelveNullTest() {
        Long demonioID = service.crear(demonio).getId();
        assertTrue(service.recuperar(demonioID + 1).isEmpty());
    }

    @Test
    void recuperarEspirituTest() {
        Long angelID = service.crear(angel).getId();
        assertEquals(angel.getNombre(), service.recuperar(angelID).get().getNombre());
        assertEquals(angel.getNivelDeConexion(), service.recuperar(angelID).get().getNivelDeConexion());
    }

    @Test
    void actualizarEspirituTest() {
        Long angelID = service.crear(angel).getId();
        angel.setNombre("Antonio");
        service.actualizar(angel);
        assertEquals("Antonio", service.recuperar(angelID).get().getNombre());
    }

    @Test
    void sePuedenPersistirVariosEspiritusConMismoNombreTest() {
        service.crear(angel);
        service.crear(new EspirituAngelical(50, "Luffy", eastblue));
        service.crear(new EspirituDemoniaco(30, "Luffy", eastblue));
        assertEquals(List.of("Luffy", "Luffy", "Luffy"), service.recuperarTodos().stream().map(Espiritu::getNombre).toList());
    }

    @Test
    void eliminarEspirituNoPersistidoNoLanzaExcepcionTest() {
        service.crear(angel);
        assertDoesNotThrow(() -> service.eliminar(demonio.getId()));
    }

    @Test
    void eliminarEspirituTest() {
        service.crear(demonio);
        System.out.println("Demonio ID: " + demonio.getId());
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(demonio.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    private void crearEspiritusDemoniacosParaPruebas(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            Espiritu e = new EspirituDemoniaco(30 + i, "Demonio" + i, eastblue);
            service.crear(e);
        }
    }

    @Test
    void recuperarEspiritusDemoniacosOrdenadosAscendiente() {
        crearEspiritusDemoniacosParaPruebas(20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.ASC, 1, 5);
        assertEquals(5, espiritusRecuperados.size());
        assertEquals("Demonio1", espiritusRecuperados.get(0).getNombre());
        assertEquals("Demonio5", espiritusRecuperados.get(4).getNombre());
    }

    @Test
    void recuperarEspiritusDemoniacosOrdenadosDescendente() {
        crearEspiritusDemoniacosParaPruebas(20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.DESC, 1, 5);
        assertEquals(5, espiritusRecuperados.size());
        assertEquals("Demonio20", espiritusRecuperados.get(0).getNombre());
        assertEquals("Demonio16", espiritusRecuperados.get(4).getNombre());
    }

    @Test
    void alRecuperarUnaPaginaFueraDeRangoEsVacia() {
        crearEspiritusDemoniacosParaPruebas(20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.ASC, 5, 5);
        assertTrue(espiritusRecuperados.isEmpty(), "Fuera de rango , no existe la pagina");
    }

    @Test
    void recuperarUnaPaginaConIndiceNegativoArrojaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.espiritusDemoniacos(Sort.Direction.ASC, -1, 5));
    }

    @Test
    void EspiritusDemoniacosConPaginaIncompletaDevuelveSoloLasRestantes() {
        crearEspiritusDemoniacosParaPruebas(5);
        List<Espiritu> pagina2 = service.espiritusDemoniacos(Sort.Direction.ASC, 2, 3);
        assertEquals(2, pagina2.size());
    }

    @Test
    void SinEspiritusDemoniacosDevuelveListaVacia() {
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.ASC, 1, 5);
        assertTrue(espiritusRecuperados.isEmpty());
    }

    @Test
    void conectarTest() {
        Long zoroId = service.crear(demonio).getId();
        Long sanjiId = sanji.getId();
        service.conectar(zoroId, sanjiId);
        assertEquals("Zoro", mediumService.espiritus(sanjiId).getFirst().getNombre());
    }

    @AfterEach
    void cleanup() {
        service.eliminarTodo();
        mediumService.eliminarTodo();
        ubicacionService.eliminarTodo();
    }
}
