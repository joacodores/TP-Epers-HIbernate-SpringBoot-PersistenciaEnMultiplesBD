package ar.edu.unq.epersgeist.persistencia.sql.entity;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.AUTO;

@NoArgsConstructor
@Entity
@Table(name = "Medium")
@SQLDelete(sql = "UPDATE Medium SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public class MediumSQL {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, length = 500)
    private String nombre;

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer manaMax;

    @Getter
    @Column(nullable = false)
    private Integer mana;

    @Getter
    @Setter
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EspirituSQL> espiritus = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private UbicacionSQL ubicacion;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public MediumSQL(Medium medium) {
        this.id = medium.getId();
        this.nombre = medium.getNombre();
        this.manaMax = medium.getManaMax();
        this.mana = medium.getMana();
        this.espiritus = medium.getEspiritus().stream().map(espiritu -> {
            EspirituSQL espirituSQL;
            if(espiritu.esAngelical()) {
                espirituSQL = new EspirituAngelicalSQL(espiritu);
            } else {
                espirituSQL = new EspirituDemoniacoSQL(espiritu);
            }
            espirituSQL.setOwner(this);
            return espirituSQL;
        }).collect(Collectors.toList());
        if(medium.getUbicacion().esSantuario()) {
            this.ubicacion = new SantuarioSQL(medium.getUbicacion());
        }else {
            this.ubicacion = new CementerioSQL(medium.getUbicacion());
        }
    }

    public MediumSQL(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
