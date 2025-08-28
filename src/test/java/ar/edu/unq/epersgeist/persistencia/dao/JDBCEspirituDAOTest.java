package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCEspirituDAOTest {
    private final EspirituDAO dao = new JDBCEspirituDAO();
    private Espiritu zorro;
    private List<Long> lastIds;

    @BeforeEach
    void crearModelo(){
        zorro = new Espiritu("fuego", 0, "zorro");
        lastIds = new ArrayList<>();
    }

    private boolean equals(Espiritu unEspiritu, Espiritu otroEspiritu){
        return unEspiritu.getId().equals(otroEspiritu.getId())
                && unEspiritu.getNombre().equals(otroEspiritu.getNombre())
                && unEspiritu.getTipo().equals(otroEspiritu.getTipo())
                && unEspiritu.getNivelDeConexion().equals(otroEspiritu.getNivelDeConexion());
    }

    private boolean contains(List<Espiritu> espiritus, Espiritu espiritu){
        return espiritus.stream().anyMatch(esp -> equals(esp, espiritu));
    }

    @Test
    void alCrearYRecuperarSeObtienenEspiritusSimilares() {
        lastIds.add(dao.crear(zorro).getId());
        var spiritSimilar = dao.recuperar(lastIds.getFirst());
        assertTrue(equals(zorro, spiritSimilar));
    }

    @Test
    void puedenActualizarseLosDatosDeUnEspirituYaGuardado() {
        lastIds.add(dao.crear(zorro).getId());
        Integer nivelDeConexionInicial = zorro.getNivelDeConexion();
        zorro.aumentarConexion(new Medium("", 0, 0));
        Espiritu dbSpirit = dao.recuperar(lastIds.getFirst());
        assertEquals(nivelDeConexionInicial, dbSpirit.getNivelDeConexion());
        dao.actualizar(zorro);
        assertEquals(nivelDeConexionInicial + 10, dao.recuperar(lastIds.getFirst()).getNivelDeConexion());
    }

    @Test
    void recuperarTodosTest() {
        Espiritu koi = new Espiritu("agua", 0, "pez koi");
        Espiritu messi = new Espiritu("futbol", 100, "messi");
        lastIds.add(dao.crear(zorro).getId());
        lastIds.add(dao.crear(koi).getId());
        lastIds.add(dao.crear(messi).getId());
        List<Espiritu> espiritus = dao.recuperarTodos();
        System.out.println(espiritus.getFirst());
        assertTrue(contains(espiritus, zorro));
        assertTrue(contains(espiritus, koi));
        assertTrue(contains(espiritus, messi));
    }

    @Test
    void eliminarEspirituNoExistenteEnLaDBNoHaceNada(){
        Long id = dao.crear(zorro).getId();
        dao.eliminar(id);
        int cantElementosAntesDeVolverAEliminar = dao.recuperarTodos().size();
        assertDoesNotThrow(() -> dao.eliminar(id));
        assertEquals(cantElementosAntesDeVolverAEliminar, dao.recuperarTodos().size());
    }

    @AfterEach
    void eliminarModelo() {
        lastIds.forEach(dao::eliminar);
    }

}
