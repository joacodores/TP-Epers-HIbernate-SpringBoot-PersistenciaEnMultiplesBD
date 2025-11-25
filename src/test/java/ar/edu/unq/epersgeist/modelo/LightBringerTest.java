package ar.edu.unq.epersgeist.modelo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightBringerTest {

    private LightBringer l1;
    private Set<Coordenada> coordsMZA;
    private Ubicacion riber;
    private Medium tai;
    private EspirituDemoniaco e1;

    @BeforeEach
    public void setUp() {

        coordsMZA = Set.of(
                new Coordenada(-32.8894, -68.8458),
                new Coordenada(-32.8850, -68.8420),
                new Coordenada(-32.8905, -68.8365)
        );

        riber = new Cementerio("Descendido", 40, coordsMZA);

        e1 = new EspirituDemoniaco(10,"Jere",riber);

        tai = new Medium("Tai", 100, 80, riber);

        l1 = new LightBringer("GordaLaGallina",20);

    }

    @Test
    public void seCreaUnLightbringerTests(){
        assertEquals(l1.getNombre(),"GordaLaGallina");
        assertEquals(l1.getFuerzaDeAtaque(),20L);
    }

    @Test
    public void iluminarMediumTest(){

        e1.poseerMedium(tai);

        l1.iluminarMedium(tai);

        assertEquals(e1.getNivelDeConexion(), 0);
    }

}