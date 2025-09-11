package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.helpers.RandomizerFalso;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
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

    private Ubicacion ubi;
    private MediumService service;
    private Medium medium;
    private UbicacionService ubicacionService;

    @BeforeEach
    void prepare() {
        ubi = new Ubicacion("ubi");
        this.medium = new Medium("Thiago", 50, 30, ubi);
        MediumDAO dao = new HibernateMediumDAO();
        EspirituDAO daoEsp = new HibernateEspirituDAO();
        UbicacionDAO daoUbi = new HibernateUbicacionDAO();
        this.ubicacionService = new UbicacionServiceImpl(daoUbi, daoEsp);
        this.service = new MediumServiceImpl(dao,  daoEsp, daoUbi);
    }

    @Test
    void crearMediumTest(){
        assertNull(medium.getId());
        ubicacionService.crear(ubi);
        service.crear(medium);
        assertNotNull(medium.getId());
    }

/*
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

    @Test
    void exorcizarConExorcistaSinEspiritusAngelicalesLanzaExcepcionTest() {
        Medium ozzy = new Medium("ozzy", 100, 100, ubi);
        Long exorcistaId = service.crear(medium).getId();
        Long ozzyId = service.crear(ozzy).getId();
        assertThrows(ExorcistaSinAngelesException.class, () -> service.exorcizar(exorcistaId, ozzyId));
    }

    @Test
    void exorcizarTest() {
        Medium tai = new Medium("Tai", 100, 80, ubi);
        Medium elNoba = new Medium("El Noba", 100, 100, ubi);

        //TODO: Cuando creen conectar podemos obviar la conexion por modelo que hice acá y hacerla por service
        EspirituAngelical angel = new EspirituAngelical(60, "angel", ubi);
        EspirituDemoniaco demonio = new EspirituDemoniaco(20, "demonio", ubi);

        RandomizerFalso randomizer = new RandomizerFalso();

        angel.setCustomRandomizer(randomizer);
        demonio.setCustomRandomizer(randomizer);

        tai.conectarseAEspiritu(angel);
        elNoba.conectarseAEspiritu(demonio);

        Long taiId = service.crear(tai).getId();
        Long elNobaId = service.crear(elNoba).getId();

        randomizer.setSecuenciaDeAtaques(5);
        randomizer.setSecuenciaDeDefensas(6);
        service.exorcizar(taiId, elNobaId);

        //TODO: Acá también, al llamar espiritus se espera que usemos el service
        elNoba = service.recuperar(elNobaId);

        assertTrue(elNoba.getEspiritus().isEmpty());
    }

    @Test
    void descansarTest(){
        Medium m1 = service.crear(medium);
        Long mediumID = m1.getId();
        service.descansar(mediumID);
        Medium m2 = service.recuperar(mediumID);
        assertEquals(m2.getMana(), 45);
    }
*/
    @AfterEach
    void cleanup(){
        service.eliminarTodo();
    }


}
