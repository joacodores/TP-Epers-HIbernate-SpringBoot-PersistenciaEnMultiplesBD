package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EspirituServiceTest {

    private Espiritu zorro;
    private Medium naruto;
    private EspirituService service;
    private Long id;

    @BeforeEach
    void crearModelo() {
        zorro = new Espiritu("fuego", 0, "zorro");
        naruto = new Medium("Naruto", 100, 100);
        EspirituDAO dao = new JDBCEspirituDAO();
        service = new EspirituServiceImpl(dao);
    }

    @Test
    void crearYRecuperarEspirituTest(){
        id = service.crear(zorro).getId();
        Espiritu zorroDB = service.recuperar(id);
        assertEquals(id, zorroDB.getId());
        assertEquals(zorro.getTipo(), zorroDB.getTipo());
        assertEquals(zorro.getNivelDeConexion(), zorroDB.getNivelDeConexion());
        assertEquals(zorro.getNombre(), zorroDB.getNombre());
    }

    @Test
    void conectarEspirituConMediumTest(){
        id = service.crear(zorro).getId();
        assertEquals(0, service.recuperar(id).getNivelDeConexion());
        assertTrue(naruto.getEspiritus().isEmpty());

        service.conectar(id, naruto);

        assertEquals(10, service.recuperar(id).getNivelDeConexion());
        assertTrue(naruto.getEspiritus().stream().anyMatch(esp -> esp.getId().equals(id)));
    }

    @AfterEach
    void eliminarModelo() {
        service.eliminar(id);
    }

}
