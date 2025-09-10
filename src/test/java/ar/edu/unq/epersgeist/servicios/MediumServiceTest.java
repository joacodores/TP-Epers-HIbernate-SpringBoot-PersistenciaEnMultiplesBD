package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.MediumServiceImpl;
import ar.edu.unq.epersgeist.servicios.impl.UbicacionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MediumServiceTest {

    private MediumService service;
    private Medium medium;
    private Ubicacion ubi;

    @BeforeEach
    void prepare() {
        ubi = new Ubicacion("ubi");
        this.medium = new Medium("Thiago", 50, 30, ubi);
        MediumDAO dao = new HibernateMediumDAO();
        EspirituDAO daoEsp = new HibernateEspirituDAO();
        UbicacionDAO daoUbi = new HibernateUbicacionDAO();
        this.service = new MediumServiceImpl(dao,  daoEsp, daoUbi);
    }

    @Test
    void crearMediumTest(){
        assertNull(medium.getId());
        service.crear(medium);
        assertNotNull(medium.getId());
    }


    @Test
    void recuperarMediumNoPersistidoDevuelveNullTest(){
        Long mediumID = service.crear(medium).getId();
        assertNull(service.recuperar(mediumID + 1));
    }


    @Test
    void recuperarMediumTest(){
        Long mediumID = service.crear(medium).getId();
        assertEquals(medium.getNombre(), service.recuperar(mediumID).getNombre());
        assertEquals(medium.getMana(), service.recuperar(mediumID).getMana());
        assertEquals(medium.getManaMax(), service.recuperar(mediumID).getManaMax());
    }

    @Test
    void actualizarMediumTest(){
        Long mediumID = service.crear(medium).getId();
        medium.setNombre("Churrito");
        service.actualizar(medium);
        assertEquals(medium.getNombre(), service.recuperar(mediumID).getNombre());
    }

    @Test
    void recuperarTodosCuandoNoSePersistioNingunObjetoDevuelveListaVaciaTest(){
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void recuperarTodosTest(){
        service.crear(medium);
        service.crear(new Medium("Juan", 80, 20, ubi));
        service.crear(new Medium("Jorge", 20, 10, ubi));
        assertEquals(List.of("Thiago", "Juan", "Jorge"), service.recuperarTodos().stream().map(Medium::getNombre).toList());
    }

    @Test
    void sePuedenPersistirVariosMediumConMismoNombreTest(){
        service.crear(medium);
        service.crear(new Medium("Thiago", 80, 20, ubi));
        service.crear(new Medium("Thiago", 20, 10, ubi));
        assertEquals(List.of("Thiago", "Thiago", "Thiago"), service.recuperarTodos().stream().map(Medium::getNombre).toList());
    }


    @Test
    void eliminarMediumNoPersistidoNoLanzaExcepcionTest(){
        service.crear(medium);
        Medium medium2 = new Medium("Doble Tonka", 60, 30, ubi);
        assertDoesNotThrow(() -> service.eliminar(medium2));
    }

    @Test
    void eliminarMediumTest(){
        service.crear(medium);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(medium);
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup(){
        service.eliminarTodo();
    }
}
