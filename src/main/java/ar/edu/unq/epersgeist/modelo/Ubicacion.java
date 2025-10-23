package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.neo.entity.TipoUbicacion;
import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.unq.epersgeist.persistencia.neo.entity.TipoUbicacion.SANTUARIO;

@Getter @Setter
@AllArgsConstructor
public abstract class Ubicacion {

    private Long id;
    private String nombre;
    private List<Espiritu> espiritus = new ArrayList<>();
    private List<Medium> mediums = new ArrayList<>();
    private Set<ConexionPsionica> conexiones = new HashSet<>();
    private Integer energia;
    private final Date createdAt = new Date();

    private Date updatedAt;
    private Boolean deletedAt = false;

    public Ubicacion(String nombre, Integer energia) {
        this.nombre = nombre; this.energia = energia;
    }

    public Ubicacion(UbicacionSQL ubicacionSQL) {
        this.id = ubicacionSQL.getId();
        this.nombre = ubicacionSQL.getNombre();
        this.energia = ubicacionSQL.getEnergia();

        for(var espirituSQL : ubicacionSQL.getEspiritus()){
            Espiritu espiritu;
            if(espirituSQL instanceof EspirituAngelicalSQL) {
                espiritu = EspirituAngelical.from(espirituSQL);
            } else {
                espiritu = EspirituDemoniaco.from(espirituSQL);
            }
            espiritus.add(espiritu);
            espiritu.setUbicacion(this);
        }

        for( var mediumSQL : ubicacionSQL.getMediums()){
            Medium medium = new Medium(mediumSQL);
            this.mediums.add(medium);
            medium.setUbicacion(this);
        }
    }

    public Ubicacion(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public static Ubicacion from(UbicacionSQL ubicacionSQL, UbicacionNeo4J ubicacionNeo4J) {
        Ubicacion ubi;
        if (ubicacionSQL instanceof SantuarioSQL) {
             ubi = Santuario.from(ubicacionSQL);
        } else {
             ubi = Cementerio.from(ubicacionSQL);
        }

        if (ubicacionNeo4J.getConexiones() == null) {
            ubi.setConexiones(Collections.emptySet());
        }else {
            ubi.conexiones = ubicacionNeo4J.getConexiones().
                    stream().filter(conexion -> !conexion.getDestino().getId().equals(ubicacionSQL.getId()))
                    .map(conexion -> {
                        Ubicacion destino;
                        if (conexion.getDestino().getTipo().equals(SANTUARIO) ) {
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

    public abstract boolean permiteInvocar(Espiritu e);
    public abstract int manaRecuperadaMedium();
    public abstract int conexionGanadaPara(Espiritu e);
    public abstract boolean esSantuario();
    public abstract boolean esCementerio();

}
