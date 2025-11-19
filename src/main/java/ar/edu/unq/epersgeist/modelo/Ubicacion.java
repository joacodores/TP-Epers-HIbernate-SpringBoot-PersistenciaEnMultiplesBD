package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.unq.epersgeist.persistencia.neo.entity.TipoUbicacion.SANTUARIO;

@Getter
@Setter
@AllArgsConstructor
public abstract class Ubicacion {

    private Date createdAt = new Date();
    private Long id;
    private String nombre;
    private List<Espiritu> espiritus = new ArrayList<>();
    private List<Medium> mediums = new ArrayList<>();
    private Set<ConexionPsionica> conexiones = new HashSet<>();
    @Embedded
    private Set<Coordenada> coordenadas = new HashSet<>();
    private Integer energia;
    private Date updatedAt;
    private Boolean deletedAt = false;

    public Ubicacion(String nombre, Integer energia, Set<Coordenada> coordenadas ) {
        this.nombre = nombre;
        this.energia = energia;
        this.coordenadas = coordenadas;
    }

    public Ubicacion(UbicacionSQL ubicacionSQL) {
        this.id = ubicacionSQL.getId();
        this.nombre = ubicacionSQL.getNombre();
        this.energia = ubicacionSQL.getEnergia();
        for (var espirituSQL : ubicacionSQL.getEspiritus()) {
            Espiritu espiritu;
            if (espirituSQL instanceof EspirituAngelicalSQL) {
                espiritu = EspirituAngelical.from(espirituSQL);
            } else {
                espiritu = EspirituDemoniaco.from(espirituSQL);
            }
            espiritus.add(espiritu);
            espiritu.setUbicacion(this);
        }
        for (var mediumSQL : ubicacionSQL.getMediums()) {
            Medium medium = new Medium(mediumSQL);
            this.mediums.add(medium);
            medium.setUbicacion(this);
        }
    }

    public Ubicacion(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Ubicacion(String nombre, Integer energia ) { // Para mapear conexiones Neo4j en el from
        this.nombre = nombre;
        this.energia = energia;
    }

    protected Ubicacion() {
    }


    public static Ubicacion from(UbicacionSQL ubicacionSQL, UbicacionNeo4J ubicacionNeo4J, UbicacionMongo ubicacionMongo) {
        Ubicacion ubi;
        if (ubicacionSQL instanceof SantuarioSQL) {
            ubi = Santuario.from(ubicacionSQL);
        } else {
            ubi = Cementerio.from(ubicacionSQL);
        }
        if (ubicacionNeo4J.getConexiones() == null) {
            ubi.setConexiones(Collections.emptySet());
        } else {
            ubi.conexiones = ubicacionNeo4J.getConexiones().
                    stream().filter(conexion -> !conexion.getDestino().getId().equals(ubicacionSQL.getId()))
                    .map(conexion -> {
                        Ubicacion destino;
                        if (conexion.getDestino().getTipo().equals(SANTUARIO)) {
                            destino = new Santuario(conexion.getDestino().getNombre(), conexion.getDestino().getEnergia());
                            destino.setId(conexion.getDestino().getId());
                        } else {
                            destino = new Cementerio(conexion.getDestino().getNombre(), conexion.getDestino().getEnergia());
                            destino.setId(conexion.getDestino().getId());
                        }
                        ConexionPsionica conexionDestino = new ConexionPsionica(destino, conexion.getCosto());
                        conexionDestino.setId(conexion.getId());
                        return conexionDestino;
                    }).collect(Collectors.toSet());
        }
        ubi.coordenadas = ubicacionMongo.getCoordenadas().stream()
                .map(coordenadaMongo -> new Coordenada(coordenadaMongo.getLatitud(), coordenadaMongo.getLongitud())).collect(Collectors.toSet());
        return ubi;
    }

    public void agregarMedium(Medium medium) {
        mediums.add(medium);
        medium.setUbicacion(this);
    }

    public void agregarEspiritu(Espiritu espiritu) {
        espiritus.add(espiritu);
        espiritu.setUbicacion(this);
    }

    public void eliminarEspiritu(Espiritu espiritu) {
        this.espiritus.remove(espiritu);
        espiritu.setUbicacion(null);
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    public void eliminarMedium(Medium medium) {
        this.mediums.remove(medium);
        medium.setUbicacion(null);
    }
    public Coordenada generarCoordenadaAleatoria() {
        double minLat = coordenadas.stream().mapToDouble(Coordenada::getLatitud).min().orElseThrow();
        double maxLat = coordenadas.stream().mapToDouble(Coordenada::getLatitud).max().orElseThrow();
        double minLon = coordenadas.stream().mapToDouble(Coordenada::getLongitud).min().orElseThrow();
        double maxLon = coordenadas.stream().mapToDouble(Coordenada::getLongitud).max().orElseThrow();

        Random rnd = new Random();

        while (true) {

            double lat = minLat + rnd.nextDouble() * (maxLat - minLat);
            double lon = minLon + rnd.nextDouble() * (maxLon - minLon);

            Coordenada punto = new Coordenada(lat, lon);

            if (estaDentro(punto)) {
                return new Coordenada(lat, lon);
            }
        }
    }
    boolean estaDentro(Coordenada p) {

        boolean inside = false;
        List<Coordenada> poly = this.coordenadas.stream().toList();

        for (int i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {

            double xi = poly.get(i).getLongitud();  // x = lon
            double yi = poly.get(i).getLatitud();   // y = lat
            double xj = poly.get(j).getLongitud();
            double yj = poly.get(j).getLatitud();

            boolean intersect = ((yi > p.getLatitud()) != (yj > p.getLatitud())) &&
                    (p.getLongitud() < (xj - xi) * (p.getLatitud() - yi) / (yj - yi) + xi);

            if (intersect) inside = !inside;
        }

        return inside;
    }


    public abstract boolean permiteInvocar(Espiritu e);

    public abstract int manaRecuperadaMedium();

    public abstract int conexionGanadaPara(Espiritu e);

    public abstract boolean esSantuario();

    public abstract boolean esCementerio();






}
