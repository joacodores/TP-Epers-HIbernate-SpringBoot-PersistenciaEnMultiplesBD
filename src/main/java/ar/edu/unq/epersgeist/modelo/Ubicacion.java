package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(unique = true, nullable = false, length = 500)
    private String nombre;

    public Ubicacion(@NonNull String nombre){
        this.nombre = nombre;
    }
}
