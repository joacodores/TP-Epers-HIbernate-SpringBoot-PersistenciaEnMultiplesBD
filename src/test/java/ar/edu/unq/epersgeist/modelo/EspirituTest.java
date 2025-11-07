package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoPuedeSerDominadoException;
import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituTest {

    private Espiritu rika;
    private Medium yuta;
    private Ubicacion puebloPaleta;
    private Set<Coordenada> coordsBA;

    @BeforeEach
    void crearModelo() {
        coordsBA = Set.of(
                new Coordenada(-34.6037, -58.3816),
                new Coordenada(-34.6100, -58.3852),
                new Coordenada(-34.6075, -58.3748)
        );
        puebloPaleta = new Cementerio("Pueblo Paleta", 50, coordsBA);
        rika = new EspirituAngelical(50, "Rika", puebloPaleta);
        yuta = new Medium("Yuta", 100, 100, puebloPaleta);
    }

    public Coordenada generarCoordenadaCercana(Coordenada origen, double minKm, double maxKm) {
        Random rnd = new Random();

        while (true) {
            double distanciaKm = minKm + rnd.nextDouble() * (maxKm - minKm);

            double angulo = rnd.nextDouble() * 2 * Math.PI;

            double radioTierra = 6371.0;

            double latOrigenRad = Math.toRadians(origen.getLatitud());
            double lonOrigenRad = Math.toRadians(origen.getLongitud());

            double nuevaLatitudRad = Math.asin(
                    Math.sin(latOrigenRad) * Math.cos(distanciaKm / radioTierra) +
                            Math.cos(latOrigenRad) * Math.sin(distanciaKm / radioTierra) * Math.cos(angulo)
            );

            double nuevaLongitudRad = lonOrigenRad + Math.atan2(
                    Math.sin(angulo) * Math.sin(distanciaKm / radioTierra) * Math.cos(latOrigenRad),
                    Math.cos(distanciaKm / radioTierra) - Math.sin(latOrigenRad) * Math.sin(nuevaLatitudRad)
            );


            double nuevaLat = Math.toDegrees(nuevaLatitudRad);
            double nuevaLon = Math.toDegrees(nuevaLongitudRad);

            return new Coordenada(nuevaLat, nuevaLon);
        }
    }

    @Test
    void crearEspirituConNivelDeConexionMenorAlRangoLanzaExcepcionTest() {
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituAngelical(-1, "Aura Negativa", puebloPaleta));
    }

    @Test
    void crearEspirituConNivelDeConexionMayorAlRangoLanzaExcepcionTest() {
        assertThrows(NivelDeConexionFueraDeRangoException.class, () -> new EspirituDemoniaco(101, "Faker", puebloPaleta));
    }

    @Test
    void crearEspirituConNivelDeConexionMinimoNoLanzaExcepcionTest() {
        assertDoesNotThrow(() -> new EspirituAngelical(0, "Chill guy", puebloPaleta));
    }

    @Test
    void crearEspirituConNivelDeConexionMaximoNoLanzaExcepcionTest() {
        assertDoesNotThrow(() -> new EspirituDemoniaco(100, "Monster Ultra", puebloPaleta));
    }

    @Test
    void crearEspirituConUbicacionNullLanzaExcepcionTest() {
        assertThrows(NullPointerException.class, () -> new EspirituAngelical(10, "hola", null));
    }

    @Test
    void aumentarConexionYaEstandoAlMaximoNoHaceNadaTest() {
        Espiritu sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        sukuna.aumentarConexion(puebloPaleta);
        assertEquals(100, sukuna.getNivelDeConexion());
    }

    @Test
    void aumentarConexionQueSuperariaElMaximoCapeaAlMaximoTest() {
        Espiritu mahoraga = new EspirituDemoniaco(95, "Mahoraga", puebloPaleta);
        mahoraga.aumentarConexion(puebloPaleta);
        assertEquals(100, mahoraga.getNivelDeConexion());
    }

    @Test
    void conexionConMediumTest() {
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().contains(rika));
    }

    @Test
    void disminuirConexionTest() {
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(10);
        assertEquals(60, rika.getNivelDeConexion());
    }

    @Test
    void disminuirCeroConexionNoHaceNadaTest() {
        yuta.conectarseAEspiritu(rika);
        assertEquals(70, rika.getNivelDeConexion());
        rika.disminuirConexion(0);
        assertEquals(70, rika.getNivelDeConexion());
    }

    @Test
    void disminuirConexionACeroDesvinculaDeMediumTest() {
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(70);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void seCapeaACeroElNivelDeConexionAlDisminuirConexionMuchoTest() {
        yuta.conectarseAEspiritu(rika);
        assertFalse(yuta.getEspiritus().isEmpty());
        rika.disminuirConexion(1000);
        assertEquals(0, rika.getNivelDeConexion());
        assertTrue(yuta.getEspiritus().isEmpty());
    }

    @Test
    void angelesPuedenExorcizarTest() {
        assertTrue(new EspirituAngelical(100, "Galaiel", puebloPaleta).puedeExorcizar());
    }

    @Test
    void demoniosNoPuedenExorcizarTest() {
        assertFalse(new EspirituDemoniaco(100, "Baal", puebloPaleta).puedeExorcizar());
    }

    @Test
    void demonioAtacarNoHaceNada() {
        EspirituDemoniaco sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        sukuna.atacar(rika);
        assertEquals(50, rika.getNivelDeConexion());
        assertEquals(100, sukuna.getNivelDeConexion());
    }

    @Test
    void angelRecibirAtaqueNoHaceNada() {
        EspirituDemoniaco sukuna = new EspirituDemoniaco(100, "Sukuna", puebloPaleta);
        rika.recibirAtaque(100, sukuna);
        assertEquals(50, rika.getNivelDeConexion());
        assertEquals(100, sukuna.getNivelDeConexion());
    }

    @Test
    void unEspirituNoEstaSiendoDominadoTest() {
        assertFalse(rika.estaSiendoDominado());
    }

    @Test
    void unEspirituSePuedeDominarTest() {
        Coordenada coordenada = generarCoordenadaCercana(rika.getCoordenada(), 2, 5);
        EspirituAngelical serua = new EspirituAngelical(40, "Serua", puebloPaleta);
        serua.setCoordenada(coordenada);
        assertTrue(rika.sePuedeDominar(serua));
    }

    @Test
    void unEspirituNoSePuedeDominarAlNoEstarLibreTest() {
        rika.conectar(yuta);
        EspirituAngelical serua = new EspirituAngelical(40, "Serua", puebloPaleta);
        assertFalse(rika.sePuedeDominar(serua));
    }

    @Test
    void unEspirituNoSePuedeDominarAlNoEstarEntre2O5KilometrosDeDistanciaTest() {
        Set<Coordenada> coordsLejanas = Set.of(
                new Coordenada(-34.5733, -58.4205), // Palermo
                new Coordenada(-34.5622, -58.4586), // Belgrano
                new Coordenada(-34.6795, -58.4698)  // Villa Lugano
        );

        Cementerio cementerioLejano = new Cementerio("Cementerio Lejano", 60, coordsLejanas);

        EspirituAngelical serua = new EspirituAngelical(40, "Serua", cementerioLejano);
        serua.setCoordenada(new Coordenada(-34.6795, -58.4698));

        assertFalse(rika.sePuedeDominar(serua));
    }

    @Test
    void unEspirituEsDominadoTest() {

        EspirituAngelical serua = new EspirituAngelical(40, "Serua", puebloPaleta);
        Coordenada coordenada =  generarCoordenadaCercana(rika.getCoordenada(), 2, 5);
        serua.setCoordenada(coordenada);

        rika.dominar(serua);

        assertEquals(serua.getDominante(), rika);
    }

    @Test
    void unEspirituTieneDominadosTest() {
        EspirituAngelical serua = new EspirituAngelical(40, "Serua", puebloPaleta);
        Coordenada coordenada =  generarCoordenadaCercana(rika.getCoordenada(), 2, 5);
        serua.setCoordenada(coordenada);

        rika.dominar(serua);

        assertEquals(1, rika.getDominados().size());
    }

    @Test
    void lanzarExcepcionCuandoUnEspirituNoPuedeDominarTest() {
        rika.conectar(yuta);
        EspirituAngelical serua = new EspirituAngelical(40, "Serua", puebloPaleta);
        assertThrows(EspirituNoPuedeSerDominadoException.class, () -> rika.dominar(serua));
    }

}
