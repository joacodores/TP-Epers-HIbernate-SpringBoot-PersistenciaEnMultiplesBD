package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UbicacionServiceTest {

    private UbicacionService service;
    private UbicacionDAO dao;
    private Ubicacion ubicacion;

    @BeforeEach
    void prepare() {
        this.ubicacion = new Ubicacion("Ubicacion");
        this.dao = new HibernateUbicacionDAO();
        this.service = new UbicacionServiceImpl(dao);
    }

    @Test
    void crearUbicacionTest(){
        assertNull(ubicacion.getId());
        service.crear(ubicacion);
        assertNotNull(ubicacion.getId());
    }

    @Test
    void crearDosUbicacionesConElMismoNombreLanzaExcepción() {
        service.crear(ubicacion);
        assertThrows(RuntimeException.class, () -> {
            service.crear(new Ubicacion("Ubicacion"));
        });
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
    void recuperarTodosTest(){
        service.crear(ubicacion);
        service.crear(new Ubicacion("Bernal"));
        service.crear(new Ubicacion("Mordor"));
        assertEquals(Set.of("Ubicacion", "Bernal", "Mordor"), new HashSet<>(service.recuperarTodos().stream().map(Ubicacion::getNombre).toList()));
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
