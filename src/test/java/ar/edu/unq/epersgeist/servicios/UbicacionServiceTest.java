package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.exceptions.UbicacionesNoConectadasException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private Set<Coordenada> coordsBA;
    private Set<Coordenada> coordsCBA;
    private Set<Coordenada> coordsMZA;
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
        coordsMZA = Set.of(
                new Coordenada(-32.8894, -68.8458),
                new Coordenada(-32.8850, -68.8420),
                new Coordenada(-32.8905, -68.8365)
        );
        this.ubicacion = new Santuario("Ubicacion", 10, coordsBA);
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
        service.crear(new Santuario("Bernal", 10, coordsBA));
        service.crear(new Cementerio("Mordor", 10, coordsCBA));
        assertEquals(List.of("Ubicacion", "Bernal", "Mordor"), service.recuperarTodos().stream().map(Ubicacion::getNombre).toList());
    }

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
        Espiritu espiritu = new EspirituAngelical(50, "Luffy", ubicacion);
        Espiritu demonio = new EspirituDemoniaco(45, "Zoro", ubicacion);
        espirituService.crear(espiritu);
        espirituService.crear(demonio);
        
        var espiritus = service.espiritusEn(ubicacion.getId());
        assertEquals(2, espiritus.size());
        assertEquals("Luffy", espiritus.get(0).getNombre());
        assertEquals("Zoro", espiritus.get(1).getNombre());
    }

    @Test
    void existeUnMediumSinEspirituEn() {
        service.crear(ubicacion);
        Ubicacion u2 = new Santuario("Templo de Jade", 30, coordsMZA);
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
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10, coordsBA));
        assertEquals(0, ubi.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        ConexionPsionica primeraConexion = ubiConectada.getConexiones().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("No hay conexiones"));
        assertEquals(ubi2.getId(), primeraConexion.getDestino().getId());
        assertEquals(1, ubiConectada.getConexiones().size());
    }

    @Test
    void unaUbicacionSeConectaConOtraBidireccionalmenteYAparecenEnSusConexionesTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10, coordsBA));
        assertEquals(0, ubi.getConexiones().size());
        assertEquals(0, ubi2.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        service.conectar(ubi2.getId(), ubi.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        ConexionPsionica conexionUbi = ubiConectada.getConexiones().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Ubi no tiene conexiones"));
        ConexionPsionica conexionUbi2 = ubiConectada2.getConexiones().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Ubi2 no tiene conexiones"));
        assertEquals(ubi2.getId(), conexionUbi.getDestino().getId());
        assertEquals(ubi.getId(), conexionUbi2.getDestino().getId());
    }

    @Test
    void unaUbicacionSeConectaConOtraUnidireccionalmenteYLaSegundaNoTieneConexionesTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10, coordsBA));
        assertEquals(0, ubi.getConexiones().size());
        assertEquals(0, ubi2.getConexiones().size());
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        Ubicacion ubiConectada2 = service.recuperar(ubi2.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        ConexionPsionica conexionUbi = ubiConectada.getConexiones().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Ubi no tiene conexiones"));
        assertEquals(ubi2.getId(), conexionUbi.getDestino().getId());
        assertEquals(0, ubiConectada2.getConexiones().size());
    }

    @Test
    void unaUbicacionSeConectaConOtraYEstanConectadasTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10, coordsBA));
        service.conectar(ubi.getId(), ubi2.getId(), 10L);
        Ubicacion ubiConectada = service.recuperar(ubi.getId()).orElseThrow(() -> new AssertionError("La ubicacion no existe"));
        ConexionPsionica conexionUbi = ubiConectada.getConexiones().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Ubi no tiene conexiones"));
        assertEquals(ubi2.getId(), conexionUbi.getDestino().getId());
        assertTrue(service.estanConectadas(ubiConectada.getId(), ubi2.getId()));
    }

    @Test
    void unaUbicacionNoEstaConectadaConOtraTest() {
        Ubicacion ubi = service.crear(ubicacion);
        Ubicacion ubi2 = service.crear(new Santuario("Bernal", 10, coordsBA));
        assertEquals(0, ubi.getConexiones().size());
        assertFalse(service.estanConectadas(ubi.getId(), ubi2.getId()));
    }

    @Test
    void caminoMasCortoTest() {
        Ubicacion quilmesEste = service.crear(new Santuario("Quilmes Este", 10, coordsBA));
        Ubicacion quilmesOeste = service.crear(new Santuario("Quilmes Oeste", 10, coordsBA));
        Ubicacion bernal = service.crear(new Santuario("Bernal", 10, coordsBA));
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
        Ubicacion quilmesEste = service.crear(new Santuario("Quilmes Este", 10, coordsBA));
        Ubicacion quilmesOeste = service.crear(new Santuario("Quilmes Oeste", 10, coordsBA));
        Ubicacion bernal = service.crear(new Santuario("Bernal", 10, coordsBA));
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
        Ubicacion bernalEste = service.crear(new Santuario("Bernal Este", 10, coordsBA));
        Ubicacion bernalOeste = service.crear(new Santuario("Bernal Oeste", 10, coordsBA));
        Ubicacion wilde = service.crear(new Santuario("Wilde", 10, coordsBA));
        Ubicacion quilmes = service.crear(new Santuario("Quilmes", 10, coordsBA));
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
        Ubicacion u = service.crear(new Santuario("U", 5, coordsBA));
        Ubicacion v = service.crear(new Santuario("V", 5, coordsBA));
        Ubicacion w = service.crear(new Santuario("W", 5, coordsBA));
        service.conectar(u.getId(), v.getId(), 5L);
        service.conectar(v.getId(), w.getId(), 5L);
        service.conectar(w.getId(), u.getId(), 5L);
        assertEquals(
                List.of(u.getId(), v.getId(), w.getId()),
                service.caminoMasCorto(u.getId(), w.getId())
                        .stream()
                        .map(Ubicacion::getId)
                        .toList()
        );
    }

    @Test
    void caminoMasCortoConOrigenIgualADestinoDevuelveUnaListaConSoloElOrigenTest() {
        Ubicacion origen = service.crear(new Santuario("Origen", 5,coordsBA));
        assertEquals(
                List.of(origen.getId()),
                service.caminoMasCorto(origen.getId(), origen.getId())
                        .stream()
                        .map(Ubicacion::getId)
                        .toList()
        );
    }

    @Test
    void caminoMasRentableTest() {
        /*
            Grafo (pesos):
            A -> D (3)
            D -> F (5)
            D -> B (2)
            B -> F (2)

            Dos caminos A-D-F (3+5=8) y A-D-B-F (3+2+2=7) -> se espera A-D-B-F
        */
        Ubicacion a = service.crear(new Santuario("A", 100, coordsBA));
        Ubicacion d = service.crear(new Santuario("D", 100, coordsBA));
        Ubicacion b = service.crear(new Santuario("B", 100, coordsBA));
        Ubicacion f = service.crear(new Santuario("F", 100, coordsBA));
        service.conectar(a.getId(), d.getId(), 3L);
        service.conectar(d.getId(), f.getId(), 5L);
        service.conectar(d.getId(), b.getId(), 2L);
        service.conectar(b.getId(), f.getId(), 2L);
        List<Long> resultado = service.caminoMasRentable(a.getId(), f.getId())
                .stream()
                .map(Ubicacion::getId)
                .toList();
        assertEquals(List.of(a.getId(), d.getId(), b.getId(), f.getId()), resultado);
    }

    @Test
    void caminoMasRentableCuandoNoHayCaminoLanzaExcepcionTest() {
        Ubicacion g = service.crear(new Santuario("G", 50, coordsBA));
        Ubicacion h = service.crear(new Santuario("H", 50, coordsBA));
        Ubicacion i = service.crear(new Santuario("I", 50, coordsBA));
        service.conectar(g.getId(), h.getId(), 10L);
        assertThrows(UbicacionesNoConectadasException.class,
                () -> service.caminoMasRentable(h.getId(), i.getId()));
        assertThrows(UbicacionesNoConectadasException.class,
                () -> service.caminoMasRentable(g.getId(), i.getId()));
    }

    @Test
    void caminoMasRentableOrigenIgualADestinoDevuelveListaConSoloEseNodoTest() {
        Ubicacion x = service.crear(new Santuario("X", 20, coordsBA));
        List<Long> resultado = service.caminoMasRentable(x.getId(), x.getId())
                .stream()
                .map(Ubicacion::getId)
                .toList();
        assertEquals(List.of(x.getId()), resultado);
    }

    @Test
    void ubicacionesConEnergiaSuperiorAlUmbralTest() {
        service.crear(new Santuario("Templo de Luz", 80, coordsBA));
        service.crear(new Cementerio("Cementerio Sombrío", 70, coordsBA));
        service.crear(new Santuario("Bosque Espectral", 10, coordsBA));
        List<Ubicacion> resultado = service.ubicacionesSobrecargadas(20);
        assertEquals(2, resultado.size());
        List<Ubicacion> resultado2 = service.ubicacionesSobrecargadas(75);
        assertEquals(1, resultado2.size());
    }

    @Test
    void noHayUbicacionesConEnergiaSuperiorAlUmbralTest() {
        service.crear(new Santuario("Templo de Luz", 80, coordsBA));
        service.crear(new Cementerio("Cementerio Sombrío", 70, coordsBA));
        service.crear(new Santuario("Bosque Espectral", 10, coordsBA));
        List<Ubicacion> resultado = service.ubicacionesSobrecargadas(90);
        assertEquals(0, resultado.size());
    }

    @Test
    void unaCoordenadaEstaDentroDeUnaUbicacionTest(){
        Coordenada puntoDentro = new Coordenada(-34.6070667, -58.3805333); // CENTROIDE
        Ubicacion ubi = ubicacionRepository.crear(ubicacion);
        assertTrue(ubicacionRepository.estaDentroDe(ubi.getId(), puntoDentro));
    }


    @Test
    void unaCoordenadaNoEstaDentroDeUnaUbicacionTest() {
        Coordenada puntoDentro = new Coordenada(-4.6070667, -5.3805333); // CENTROIDE
        Ubicacion ubi = ubicacionRepository.crear(ubicacion);
        assertFalse(ubicacionRepository.estaDentroDe(ubi.getId(), puntoDentro));
    }


    @AfterEach
    void cleanup() {
        espirituService.eliminarTodo();
        mediumService.eliminarTodo();
        service.eliminarTodo();
    }



}
