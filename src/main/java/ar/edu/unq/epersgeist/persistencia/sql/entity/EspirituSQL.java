package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Randomizer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@Table(name = "Espiritu")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING, length = 50)
@SQLDelete(sql = "UPDATE Espiritu SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public abstract class EspirituSQL {

    private final int maxNivelDeConexion = 100;
    private final int minNivelDeConexion = 0;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Transient
    @JsonIgnore
    protected Randomizer randomizer;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "nivel_de_conexion", nullable = false, columnDefinition = "INTEGER CHECK(nivel_de_conexion BETWEEN 0 AND 100)")
    private int nivelDeConexion;

    @Column(nullable = false, length = 500)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    private UbicacionSQL ubicacion;

    @ManyToOne
    private MediumSQL owner;

    @ManyToOne
    private NightBringerSQL nightBringer;

    @ManyToOne
    private EspirituSQL dominante;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EspirituSQL> dominados = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public EspirituSQL(Espiritu espiritu) {
        this.id = espiritu.getId();
        this.nombre = espiritu.getNombre();
        this.nivelDeConexion = espiritu.getNivelDeConexion();
        if (espiritu.getUbicacion().esSantuario()) {
            this.ubicacion = new SantuarioSQL(espiritu.getUbicacion());
        } else {
            this.ubicacion = new CementerioSQL(espiritu.getUbicacion());
        }
        if (espiritu.getOwner() != null) {
            this.owner = new MediumSQL(espiritu.getOwner().getId(), espiritu.getOwner().getNombre());
        } else {
            this.owner = null;
        }
        // El nightBringer se establecerá después en el repositorio usando la entidad real de la BD
        // para evitar problemas de persistencia
        this.nightBringer = null;
        this.dominados = espiritu.getDominados().stream().map(dominado -> {
            EspirituSQL espirituSQL;
            if (dominado.esAngelical()) {
                espirituSQL = new EspirituAngelicalSQL(dominado.getId(), dominado.getNombre());
            } else {
                espirituSQL = new EspirituDemoniacoSQL(dominado.getId(), dominado.getNombre());
            }
            espirituSQL.setDominante(this);
            return espirituSQL;
        }).collect(Collectors.toCollection(ArrayList::new));

        if (espiritu.getDominante() != null) {
            if (espiritu.getDominante().esAngelical()) {
                this.dominante = new EspirituAngelicalSQL(espiritu.getDominante().getId(), espiritu.getDominante().getNombre());
            } else {
                this.dominante = new EspirituDemoniacoSQL(espiritu.getDominante().getId(), espiritu.getDominante().getNombre());
            }
        }
    }

    public EspirituSQL() {
    }

    public EspirituSQL(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
