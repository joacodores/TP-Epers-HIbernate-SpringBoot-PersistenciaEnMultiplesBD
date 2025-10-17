package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.AUTO;

@Getter @Setter
@AllArgsConstructor
public abstract class Ubicacion {

    private Long id;
    private String nombre;
    private List<Espiritu> espiritus = new ArrayList<>();
    private List<Medium> mediums = new ArrayList<>();
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

    void internalAddMedium(Medium m) {
        if (m != null && !mediums.contains(m)) mediums.add(m);
    }
    void internalAddEspiritu(Espiritu e) {
        if (e != null && !espiritus.contains(e)) espiritus.add(e);
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
