package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EspirituServiceTest {
    private EspirituService service;
    private EspirituAngelical angel;
    private EspirituDemoniaco demonio;

    @BeforeEach
    void prepare() {
        this.angel = new EspirituAngelical(58, "Luffy");
        this.demonio = new EspirituDemoniaco(36, "Zoro");
        EspirituDAO dao = new HibernateEspirituDAO();
        this.service = new EspirituServiceImpl(dao);
    }

    @Test
    void crearEspirituTest(){
        assertNull(angel.getId());
        service.crear(angel);
        assertNotNull(angel.getId());
    }

    @Test
    void recuperarEspirituNoPersistidoDevuelveNullTest(){
        Long demonioID = service.crear(demonio).getId();
        assertNull(service.recuperar(demonioID + 1));
    }

    @Test
    void recuperarEspirituTest(){
        Long angelID = service.crear(angel).getId();
        assertEquals(angel.getNombre(), service.recuperar(angelID).getNombre());
        assertEquals(angel.getNivelDeConexion(), service.recuperar(angelID).getNivelDeConexion());
    }

    @Test
    void sePuedenPersistirVariosEspiritusConMismoNombreTest(){
        service.crear(angel);
        service.crear(new EspirituAngelical(50, "Luffy"));
        service.crear(new EspirituDemoniaco(30, "Luffy"));
        assertEquals(List.of("Luffy", "Luffy", "Luffy"), service.recuperarTodos().stream().map(Espiritu::getNombre).toList());
    }

    @Test
    void eliminarEspirituNoPersistidoNoLanzaExcepcionTest(){
        service.crear(angel);
        assertDoesNotThrow(() -> service.eliminar(demonio));
    }

    @Test
    void eliminarEspirituTest(){
        service.crear(demonio);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(demonio);
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @AfterEach
    void cleanup(){
        service.eliminarTodo();
    }
}
