package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.NightBringer;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "nightbringers")
public class NightBringerSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "nightBringer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspirituSQL> espiritus = new ArrayList<>();

    public NightBringerSQL() {}

    public NightBringerSQL(NightBringer nightBringer) {
        this.id =  nightBringer.getId();
        this.nombre = nightBringer.getNombre();
        if(nightBringer.getEspiritus()!=null){
            nightBringer.getEspiritus().stream().map(e -> {
                EspirituDemoniacoSQL espirituSQL = new EspirituDemoniacoSQL(e.getId(), e.getNombre());
                espirituSQL.setNivelDeConexion(e.getNivelDeConexion());
                UbicacionSQL ubicacion = new CementerioSQL(e.getUbicacion());
                espirituSQL.setUbicacion(ubicacion);
                espiritus.add(espirituSQL);
                return espirituSQL;
            });
        }

    }

    public NightBringerSQL(String nombre) {
        this.nombre = nombre;
    }
}