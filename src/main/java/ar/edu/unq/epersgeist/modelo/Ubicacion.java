package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ubicacion", discriminatorType = DiscriminatorType.STRING, length = 20)
@SQLDelete(sql = "UPDATE Ubicacion SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public abstract class Ubicacion {
    @Setter
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false, length = 500)
    private String nombre;

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Espiritu> espiritus = new ArrayList<>();

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Medium> mediums = new ArrayList<>();

    @Setter
    @Column(name = "energia", nullable = false, columnDefinition = "INTEGER CHECK(energia BETWEEN 1 AND 100)")
    private Integer energia;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public Ubicacion(@NonNull String nombre, @NonNull Integer energia) {
        this.nombre = nombre; this.energia = energia;
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
