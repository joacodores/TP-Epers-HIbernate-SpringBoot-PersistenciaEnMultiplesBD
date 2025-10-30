package ar.edu.unq.epersgeist.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoordenadaTest {

    private Coordenada coordA;
    private Coordenada coordB;

    @BeforeEach
    public void setUp() {
        coordA = new Coordenada(-34.6037, -58.3816); // Obelisco, Buenos Aires
        coordB = new Coordenada(-34.6920, -58.3816); // 10km al sur
    }

    @Test
    void coordenadaEstaA10kmDeDistanciaDeOtraTest(){
        double distancia = coordA.distanciaEnKm(coordB);
        assertTrue(distancia > 9 & distancia < 11);
    }

}
