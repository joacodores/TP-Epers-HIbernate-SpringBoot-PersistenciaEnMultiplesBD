package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Ubicacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ubicacion", discriminatorType = DiscriminatorType.STRING, length = 20)
@SQLDelete(sql = "UPDATE Ubicacion SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public abstract class UbicacionSQL {

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Setter
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false, length = 500)
    private String nombre;

    @Setter
    @Getter
    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EspirituSQL> espiritus = new ArrayList<>();

    @Setter
    @Getter
    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MediumSQL> mediums = new ArrayList<>();

    @Setter
    @Column(name = "energia", nullable = false, columnDefinition = "INTEGER CHECK(energia BETWEEN 1 AND 100)")
    private Integer energia;

    @Setter
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public UbicacionSQL(Ubicacion ubicacion) {
        this.id = ubicacion.getId();
        this.nombre = ubicacion.getNombre();
        this.energia = ubicacion.getEnergia();
        this.mediums = ubicacion.getMediums().stream().map(medium -> {
            MediumSQL mediumSQL = new MediumSQL(medium.getId(), medium.getNombre());
            mediumSQL.setUbicacion(this);
            return mediumSQL;
        }).collect(Collectors.toCollection(ArrayList::new));
        this.espiritus = ubicacion.getEspiritus().stream().map(espiritu -> {
            EspirituSQL espirituSQL;
            if (espiritu.esAngelical()) {
                espirituSQL = new EspirituAngelicalSQL(espiritu.getId(), espiritu.getNombre());
            } else {
                espirituSQL = new EspirituDemoniacoSQL(espiritu.getId(), espiritu.getNombre());
            }
            espirituSQL.setUbicacion(this);
            return espirituSQL;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public UbicacionSQL(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
