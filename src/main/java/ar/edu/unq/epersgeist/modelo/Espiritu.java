package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Espiritu {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(nullable = false)
    private double nivelDeConexion;
    @Column(nullable = false, length = 500)
    private String nombre;
    private final double maxNivelDeConexion = 100;
    private final double minNivelDeConexion = 0;

    @ManyToOne
    private Ubicacion ubicacion;
    @ManyToOne
    private Medium owner;

    public Espiritu(@NonNull Integer nivelDeConexion, @NonNull String nombre, Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    private void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < minNivelDeConexion || nivelDeConexion > maxNivelDeConexion) {
            throw new NivelDeConexionFueraDeRangoException(String.format("El nivel de conexión para el espiritu %s es inválido (debe ser un número entre %s y %s)", this.nombre, this.minNivelDeConexion, this.maxNivelDeConexion));
        }
        this.nivelDeConexion = nivelDeConexion;
    }

    public void aumentarConexion(Medium medium) {
        Integer manaDeMedium = medium.getMana();
        this.nivelDeConexion += (manaDeMedium * 0.20);
        if (this.nivelDeConexion >= 100) this.nivelDeConexion = 100;
    }

    public void conectar(Medium medium){
        aumentarConexion(medium);
        setOwner(medium);
    }

    public boolean esEspirituLibre() {
        return this.owner == null;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public abstract String getTipo();

}