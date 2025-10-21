package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.neo.entity.TipoUbicacion;
import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.min;

@Getter @Setter
@AllArgsConstructor
public abstract class Ubicacion {

    private Long id;
    private String nombre;
    private List<Espiritu> espiritus = new ArrayList<>();
    private List<Medium> mediums = new ArrayList<>();
    private Set<Ubicacion> ubicacionesConectadas = new HashSet<>();
    private Integer energia;
    private final Date createdAt = new Date();
    private Long costo;
    private Date updatedAt;
    private Boolean deletedAt = false;

    public Ubicacion(String nombre, Integer energia,  Long costo) {
        this.nombre = nombre; this.energia = energia;  this.costo = Math.max(0, Math.min(100, costo));
    }

    public Ubicacion(UbicacionSQL ubicacionSQL) {
        this.id = ubicacionSQL.getId();
        this.nombre = ubicacionSQL.getNombre();
        this.energia = ubicacionSQL.getEnergia();
        this.costo = ubicacionSQL.getCosto();

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
        ubi.ubicacionesConectadas = ubicacionNeo4J.getUbicacionesConectadas().
                stream().filter(conexion -> !conexion.getId().equals(ubicacionSQL.getId()))
                .map(conexion -> {
                    Ubicacion ubiConectada;
                    if (conexion.getTipo().equals(TipoUbicacion.SANTUARIO)) {
                        ubiConectada = new Santuario(conexion.getNombre(),  conexion.getEnergia(), conexion.getCosto());
                        ubiConectada.setId(conexion.getId());
                    } else {
                        ubiConectada = new Cementerio(conexion.getNombre(),  conexion.getEnergia(), conexion.getCosto());
                        ubiConectada.setId(conexion.getId());
                    }
                    return ubiConectada;
                }).collect(Collectors.toSet());
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
