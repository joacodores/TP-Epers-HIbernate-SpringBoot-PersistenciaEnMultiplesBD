package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Ubicacion {
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

    /*
    @Column(name = "energia", nullable = false, columnDefinition = "INTEGER CHECK(energia BETWEEN 1 AND 100)")
    private Integer energia;
    */

    public Ubicacion(@NonNull String nombre) {
        this.nombre = nombre;
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
        //espiritu.setUbicacion(null);
    }
}
