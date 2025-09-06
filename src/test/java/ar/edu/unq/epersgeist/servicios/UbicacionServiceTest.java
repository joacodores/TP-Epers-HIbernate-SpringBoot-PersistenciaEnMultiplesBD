package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UbicacionServiceTest {

    private UbicacionService service;
    private Ubicacion ubicacion;

    @BeforeEach
    void prepare() {
        this.ubicacion = new Ubicacion("Ubicacion");
        UbicacionDAO dao = new HibernateUbicacionDAO();
        this.service = new UbicacionServiceImpl(dao);
    }

    @Test
    void crearUbicacionTest(){
        assertNull(ubicacion.getId());
        service.crear(ubicacion);
        assertNotNull(ubicacion.getId());
    }

    @Test
    void recuperarUbicacionNoPersistidaDevuelveNullTest(){
        Long ubicacionID = service.crear(ubicacion).getId();
        assertNull(service.recuperar(ubicacionID + 1));
    }

    @Test
    void recuperarUbicacionTest(){
        Long ubicacionID = service.crear(ubicacion).getId();
        assertEquals(ubicacion.getNombre(), service.recuperar(ubicacionID).getNombre());
    }

    @Test
    void actualizarUbicacionTest(){
        Long ubicacionID = service.crear(ubicacion).getId();
        ubicacion.setNombre("Berazategui");
        service.actualizar(ubicacion);
        assertEquals(ubicacion.getNombre(), service.recuperar(ubicacionID).getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunObjetoDevuelveListaVaciaTest(){
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosDevuelveLasUbicacionesEnOrdenAscendentePorNombreTest(){
        service.crear(ubicacion);
        service.crear(new Ubicacion("Bernal"));
        service.crear(new Ubicacion("Mordor"));
        assertEquals(List.of("Bernal", "Mordor", "Ubicacion"), service.recuperarTodos().stream().map(Ubicacion::getNombre).toList());
    }

    @Test
    void eliminarUbicacionNoPersistidaNoLanzaExcepcionTest(){
        service.crear(ubicacion);
        Ubicacion ubicacion2 = new Ubicacion("Nueva Ubicación");
        assertDoesNotThrow(() -> service.eliminar(ubicacion2));
    }

    @Test
    void eliminarUbicacionTest(){
        service.crear(ubicacion);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(ubicacion);
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup(){
        service.eliminarTodo();
    }

}
