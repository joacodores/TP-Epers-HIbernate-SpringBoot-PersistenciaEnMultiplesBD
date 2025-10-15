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

import static jakarta.persistence.GenerationType.AUTO;

@Data
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
        this.espiritus = ubicacionSQL.getEspiritus().stream()
                .map(espirituSQL -> {
                    if(espirituSQL instanceof EspirituAngelicalSQL) {
                        return EspirituAngelical.from(espirituSQL);
                    } else {
                        return EspirituDemoniaco.from(espirituSQL);
                    }
                }).toList();
        this.mediums = ubicacionSQL.getMediums().stream()
                .map(Medium::new).toList();
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
