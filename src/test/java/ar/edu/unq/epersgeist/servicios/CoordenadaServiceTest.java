package ar.edu.unq.epersgeist.servicios;


import ar.edu.unq.epersgeist.controller.exceptions.CoordenadaNoEncontradaException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CoordenadaServiceTest {

    @Autowired
    private CoordenadaService service;
    private Coordenada coordA;
    private Coordenada coordB;

    @BeforeEach
    void prepare() {
        coordA = new Coordenada(-34.6037, -58.3816); // Obelisco, Buenos Aires
        coordB = new Coordenada(-34.6920, -58.3816); // 10km al sur
    }

    @Test
    void crearCoordenadaTest(){
        assertNull(coordA.getId());
        service.crear(coordA);
        assertNotNull(coordA.getId());
    }
    @Test
    void recuperarCoordenadaNoExistenteLanzaExcepcion() {
        Long coordenadaId = service.crear(coordA).getId();
        assertThrows(CoordenadaNoEncontradaException.class, () -> service.recuperar(coordenadaId + 1));
    }

    @Test
    void recuperarCoordenadaTest() {
        Long coordId = service.crear(coordA).getId();
        Coordenada coordenada = service.recuperar(coordId);
        assertEquals(coordA.getLatitud(), coordenada.getLatitud());
    }
    @Test
    void recuperarTodosDevuelveTodasLasCoordenadasTest() {
        service.crear(coordA);
        service.crear(coordB);
        assertEquals(List.of(coordA.getLatitud(), coordB.getLatitud()), service.recuperarTodos().stream().map(Coordenada::getLatitud).toList());
    }

    @Test
    void eliminarCoordenadaTest() {
        service.crear(coordA);
        assertFalse(service.recuperarTodos().isEmpty());
        service.eliminar(coordA.getId());
        assertTrue(service.recuperarTodos().isEmpty());
    }

    @Test
    void coordenadaEstaA10kmDeDistanciaDeOtraTest(){
        Long coordAId = service.crear(coordA).getId();
        Long coordBId = service.crear(coordB).getId();
        double distancia = service.distanciaEnKm(coordAId, coordBId);
        assertTrue(distancia > 9 & distancia < 11);
    }

    @AfterEach
    void eliminarTodoTest(){
        service.eliminarTodo();
    }

}
