package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EspirituServiceTest {

    @Autowired
    private EspirituService service;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private EntityManager em;



    private Ubicacion crearUbicacion(String nombre, int energia) {
        Set<Coordenada> coordsBA = Set.of(
                new Coordenada(-34.6037, -58.3816),
                new Coordenada(-34.6100, -58.3852),
                new Coordenada(-34.6075, -58.3748)
        );
        Ubicacion ubicacion = new Cementerio(nombre, energia, coordsBA);
        ubicacionService.crear(ubicacion);
        return ubicacion;
    }

    private Medium crearMedium(Ubicacion ubicacion) {
        Medium medium = new Medium("Sanji", 60, 30, ubicacion);
        mediumService.crear(medium);
        return medium;
    }

    private EspirituAngelical crearEspirituAngelical(int conexion, String nombre, Ubicacion ubicacion) {
        return new EspirituAngelical(conexion, nombre, ubicacion);
    }

    private EspirituDemoniaco crearEspirituDemoniaco(int conexion, String nombre, Ubicacion ubicacion) {
        return new EspirituDemoniaco(conexion, nombre, ubicacion);
    }

    @BeforeEach
    void beforeEach() {
        em.flush();
        em.clear();
    }

    @Test
    void crearEspirituTest() {
        Ubicacion ubi = crearUbicacion("East Blue", 10);
        EspirituAngelical angel = crearEspirituAngelical(58, "Luffy", ubi);
        assertNull(angel.getId());
        service.crear(angel);
        assertNotNull(angel.getId());
    }

    @Test
    void recuperarEspirituNoPersistidoDevuelveExceptionTest() {
        Ubicacion ubi = crearUbicacion("North Blue", 15);
        EspirituDemoniaco demonio = crearEspirituDemoniaco(36, "Zoro", ubi);
        Long demonioID = service.crear(demonio).getId();
        assertThrows(EspirituNoEncontradoException.class, () -> service.recuperar(demonioID + 1));
    }

    @Test
    void recuperarEspirituTest() {
        Ubicacion ubi = crearUbicacion("West Blue", 12);
        EspirituAngelical angel = crearEspirituAngelical(58, "Luffy", ubi);
        Long angelID = service.crear(angel).getId();
        Espiritu angelRecuperado = service.recuperar(angelID)
                .orElseThrow(() -> new AssertionError("espiritu no encontrado"));
        assertAll(
                () -> assertEquals(angel.getNombre(), angelRecuperado.getNombre()),
                () -> assertEquals(angel.getNivelDeConexion(), angelRecuperado.getNivelDeConexion())
        );
    }

    @Test
    void actualizarEspirituTest() {
        Ubicacion ubi = crearUbicacion("South Blue", 8);
        EspirituAngelical angel = crearEspirituAngelical(50, "Usopp", ubi);
        Long angelID = service.crear(angel).getId();
        angel.setNombre("Antonio");
        service.actualizar(angel);
        Espiritu angelRecuperado = service.recuperar(angelID)
                .orElseThrow(() -> new AssertionError("espiritu no encontrado"));
        assertEquals("Antonio", angelRecuperado.getNombre());
    }

    @Test
    void sePuedenPersistirVariosEspiritusConMismoNombreTest() {
        Ubicacion ubi = crearUbicacion("Grand Line", 20);
        EspirituAngelical a1 = crearEspirituAngelical(50, "Luffy", ubi);
        EspirituAngelical a2 = crearEspirituAngelical(50, "Luffy", ubi);
        EspirituDemoniaco d1 = crearEspirituDemoniaco(30, "Luffy", ubi);
        service.crear(a1);
        service.crear(a2);
        service.crear(d1);
        assertEquals(List.of("Luffy", "Luffy", "Luffy"),
                service.recuperarTodos().stream().map(Espiritu::getNombre).toList());
    }

    @Test
    void eliminarEspirituTest() {
        Ubicacion ubi = crearUbicacion("New World", 25);
        EspirituDemoniaco demonio = crearEspirituDemoniaco(36, "Zoro", ubi);
        service.crear(demonio);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(demonio.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    private void crearEspiritusDemoniacosParaPruebas(Ubicacion ubi, int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            Espiritu e = crearEspirituDemoniaco(30 + i, "Demonio" + i, ubi);
            service.crear(e);
        }
    }

    @Test
    void recuperarEspiritusDemoniacosOrdenadosAscendente() {
        Ubicacion ubi = crearUbicacion("Marijoa", 18);
        crearEspiritusDemoniacosParaPruebas(ubi, 20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.ASC, 1, 5);
        assertEquals(5, espiritusRecuperados.size());
        assertEquals("Demonio1", espiritusRecuperados.get(0).getNombre());
        assertEquals("Demonio5", espiritusRecuperados.get(4).getNombre());
    }

    @Test
    void recuperarEspiritusDemoniacosOrdenadosDescendente() {
        Ubicacion ubi = crearUbicacion("Skypiea", 22);
        crearEspiritusDemoniacosParaPruebas(ubi, 20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.DESC, 1, 5);
        assertEquals(5, espiritusRecuperados.size());
        assertEquals("Demonio20", espiritusRecuperados.get(0).getNombre());
        assertEquals("Demonio16", espiritusRecuperados.get(4).getNombre());
    }

    @Test
    void alRecuperarUnaPaginaFueraDeRangoEsVacia() {
        Ubicacion ubi = crearUbicacion("Dressrosa", 17);
        crearEspiritusDemoniacosParaPruebas(ubi, 20);
        List<Espiritu> espiritusRecuperados = service.espiritusDemoniacos(Sort.Direction.ASC, 5, 5);
        assertTrue(espiritusRecuperados.isEmpty(), "Fuera de rango, no existe la pagina");
    }

    @Test
    void recuperarUnaPaginaConIndiceNegativoArrojaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> service.espiritusDemoniacos(Sort.Direction.ASC, -1, 5));
    }

    @Test
    void EspiritusDemoniacosConPaginaIncompletaDevuelveSoloLasRestantes() {
        Ubicacion ubi = crearUbicacion("Alabasta", 14);
        crearEspiritusDemoniacosParaPruebas(ubi, 5);
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
        Ubicacion ubi = crearUbicacion("Sabaody", 21);
        Medium sanji = crearMedium(ubi);
        EspirituDemoniaco demonio = crearEspirituDemoniaco(36, "Zoro", ubi);
        Long zoroId = service.crear(demonio).getId();
        Long sanjiId = sanji.getId();
        service.conectar(zoroId, sanjiId);
        assertEquals("Zoro", mediumService.espiritus(sanjiId).getFirst().getNombre());
    }

    @AfterEach
    void afterEach() {
        service.eliminarTodo();
        mediumService.eliminarTodo();
        ubicacionService.eliminarTodo();
        em.flush();
        em.clear();
    }

}
