package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Randomizer;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@Table(name = "Espiritu")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SQLDelete(sql = "UPDATE Espiritu SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public abstract class EspirituSQL {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "nivel_de_conexion", nullable = false, columnDefinition = "INTEGER CHECK(nivel_de_conexion BETWEEN 0 AND 100)")
    private int nivelDeConexion;

    @Column(nullable = false, length = 500)
    private String nombre;
    private final int maxNivelDeConexion = 100;
    private final int minNivelDeConexion = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    private UbicacionSQL ubicacion;

    @ManyToOne
    private MediumSQL owner;

    @Transient
    protected Randomizer randomizer;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public EspirituSQL(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.nombre = espiritu.getNombre();
        this.nivelDeConexion = espiritu.getNivelDeConexion();
        if(espiritu.getUbicacion().esSantuario()) {
            this.ubicacion = new SantuarioSQL(espiritu.getUbicacion());
        }else {
            this.ubicacion = new CementerioSQL(espiritu.getUbicacion());
        }
    }

    public EspirituSQL() {}

    public EspirituSQL(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
