package ar.edu.unq.epersgeist.modelo;
import ar.edu.unq.epersgeist.controller.exceptions.EspirituAngelicalException;
import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NightBringerTest {

    private NightBringer n1;
    private Set<Coordenada> coordsMZA;
    private Ubicacion riber;
    private EspirituDemoniaco e1;

    @BeforeEach
    public void setUp() {

        coordsMZA = Set.of(
                new Coordenada(-32.8894, -68.8458),
                new Coordenada(-32.8850, -68.8420),
                new Coordenada(-32.8905, -68.8365)
        );
        riber = new Cementerio("Descendido", 40, coordsMZA);

        n1 = new NightBringer("Mateo");


        e1 = new EspirituDemoniaco(10,"Jere",riber);

    }

    @Test
    public void seCreaUnNightbringerTest(){
        assertEquals(n1.getNombre(),"Mateo");
        assertEquals(n1.getEspiritus().size(),0);
    }


    @Test
    public void spawneaUnEspirituDemoniacoTest(){
        n1.spawnearEspirituEnUbicacion(riber, "PEPE");
        Espiritu e = n1.getEspiritus().stream().findFirst().get();
        assertEquals(n1.getEspiritus().size(),1);

        assertTrue(riber.estaDentro(e.getCoordenada()));
    }

    @Test
    public void unEspirituAngelicalNoPuedePoseerUnMediumTest(){
        EspirituAngelical a1 = new EspirituAngelical(10,"Jacobo",riber);
        Medium tai = new Medium("Tai", 100, 80, riber);

        assertThrows(EspirituAngelicalException.class, () -> a1.poseerMedium(tai));

    }

}