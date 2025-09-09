package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Espiritu {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(nullable = false)
    private Integer nivelDeConexion;
    @Column(nullable = false, length = 500)
    private String nombre;
    private final Integer maxNivelDeConexion = 100;
    private final Integer minNivelDeConexion = 0;

    @ManyToOne
    private Medium owner;

    @ManyToOne
    private Ubicacion ubicacion;

    public Espiritu(@NonNull Integer nivelDeConexion, @NonNull String nombre) {
        this.nombre = nombre;
        validarNivelDeConexion(nivelDeConexion);

    }

    private void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < minNivelDeConexion || nivelDeConexion > maxNivelDeConexion) {
            throw new NivelDeConexionFueraDeRangoException(String.format("El nivel de conexión para el espiritu %s es inválido (debe ser un número entre %s y %s)", this.nombre, this.minNivelDeConexion, this.maxNivelDeConexion));
        }
        this.nivelDeConexion = nivelDeConexion;
    }

    public Medium aumentarConexion(Medium medium) {
        this.nivelDeConexion += 10;
        if (this.nivelDeConexion >= 100) this.nivelDeConexion = 100;
        return medium;
    }



    public abstract String getTipo();

}