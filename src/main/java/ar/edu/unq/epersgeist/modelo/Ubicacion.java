package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(unique = true, nullable = false, length = 500)
    private String nombre;

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Espiritu> espiritus = new ArrayList<>();

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Medium> mediums = new ArrayList<>();

    public Ubicacion(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public void agregarMedium(Medium medium) {
        mediums.add(medium);
        medium.setUbicacion(this);
    }

    public void agregarEspiritu(Espiritu espiritu) {
        this.espiritus.add(espiritu);
        espiritu.setUbicacion(this);
    }

    public void eliminarEspiritu(Espiritu espiritu) {
        this.espiritus.remove(espiritu);
        espiritu.setUbicacion(null);
    }
}
