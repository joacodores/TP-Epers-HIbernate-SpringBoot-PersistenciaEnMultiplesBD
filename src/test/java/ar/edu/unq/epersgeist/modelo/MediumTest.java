package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoEsLibreException;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoPuedeConectarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MediumTest {

    private Ubicacion fuerteApache;
    private Ubicacion bocaPredio;
    private Medium juan;
    private Medium palermo;
    private Espiritu carlitos;

    @BeforeEach
    void prepare(){
        fuerteApache = new Ubicacion("Fuerte Apache");
        bocaPredio = new Ubicacion("Boca Predio");
        palermo = new Medium("Martin Palermo", 100, 25, fuerteApache);
        juan = new Medium("Juan", 100, 20, bocaPredio);
        carlitos = new EspirituAngelical(80, "Carlitos", fuerteApache );
        }


    @Test
    void constructorAndGettersTest() {
        assertEquals("Juan", juan.getNombre());
        assertEquals(100, juan.getManaMax());
        assertEquals(20, juan.getMana());
    }

    @Test
    void manaNoPuedeSuperarAManaMax()
    {
        Medium tai = new Medium("Tai", 50, 70, fuerteApache);
        Medium elNoba = new Medium("El Noba", 50, 50,  fuerteApache);

        assertEquals(50, tai.getMana());
        assertEquals(50, elNoba.getMana());
        assertEquals(elNoba.getMana(), tai.getMana());
    }

    @Test
    void mediumInvocaEspirituASuUbicacion(){
        assertEquals(fuerteApache, carlitos.getUbicacion());
        assertEquals(bocaPredio, juan.getUbicacion());
        juan.invocar(carlitos);
        assertEquals(bocaPredio, carlitos.getUbicacion());
    }
    @Test
    void mediumNoPuedeInvocarEspirituPorBajaMana(){
        juan.disminuirMana(11);
        assertEquals(9, juan.getMana());
        juan.invocar(carlitos);
        //no pasa nada
        assertEquals(9, juan.getMana());
        assertEquals(fuerteApache, carlitos.getUbicacion());
    }

    @Test
    void espirituConectaConMedium(){
        assert(palermo.getEspiritus().isEmpty());
        assertEquals(carlitos.getOwner(), null);
        palermo.conectarseAEspiritu(carlitos);
        assert(palermo.getEspiritus().contains(carlitos));
        assertEquals(carlitos.getOwner(), palermo);
    }

    @Test
    void mediumNoPuedeInvocarEspirituNoLibre(){
        palermo.conectarseAEspiritu(carlitos);
        assertThrows(EspirituNoEsLibreException.class, () -> juan.invocar(carlitos));
    }
/*
    @Test
    void espirituNoPuedeConectarConMediumEnOtraUbicacion(){
        assertEquals(fuerteApache, carlitos.getUbicacion());
        assertEquals(bocaPredio, juan.getUbicacion());
        assertThrows(EspirituNoPuedeConectarException.class, () -> carlitos.conectar(juan));
    }
*/
}
