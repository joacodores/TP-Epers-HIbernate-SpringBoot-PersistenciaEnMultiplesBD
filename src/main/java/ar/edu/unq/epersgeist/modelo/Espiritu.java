package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@Table(name = "espiritu")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SQLDelete(sql = "UPDATE Espiritu SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public abstract class Espiritu {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "nivel_de_conexion", nullable = false, columnDefinition = "INTEGER CHECK(nivel_de_conexion BETWEEN 0 AND 100)")
    private int nivelDeConexion;

    @Column(nullable = false, length = 500)
    private String nombre;
    private final int maxNivelDeConexion = 100;
    private final int minNivelDeConexion = 0;

    @ManyToOne
    private Ubicacion ubicacion;

    @ManyToOne
    private Medium owner;

    @Transient
    protected Randomizer randomizer;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    @SuppressWarnings("unused")
    public Espiritu() {
        this.randomizer = new RandomizerImpl();
    }

    public Espiritu(int nivelDeConexion, @NonNull String nombre, @NonNull Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.randomizer = new RandomizerImpl();
        ubicacion.agregarEspiritu(this);
    }

    public abstract boolean puedeExorcizar();

    private void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < minNivelDeConexion || nivelDeConexion > maxNivelDeConexion) {
            throw new NivelDeConexionFueraDeRangoException(String.format("El nivel de conexión para el espiritu %s es inválido (debe ser un número entre %s y %s)", this.nombre, this.minNivelDeConexion, this.maxNivelDeConexion));
        }
        this.nivelDeConexion = nivelDeConexion;
    }

    private void capearNivelDeConexion() {
        if (this.nivelDeConexion >= 100) this.nivelDeConexion = 100;
    }

    public void aumentarConexion(Ubicacion ubicacionDeDescanso) {
        this.nivelDeConexion += ubicacionDeDescanso.conexionGanadaPara(this);
        this.capearNivelDeConexion();
    }

    public void desvincularDeMedium() {
        owner.desvincularEspiritu(this);
        this.setOwner(null);
    }

    public void disminuirConexion(int cantidad) {
        this.nivelDeConexion -= cantidad;
        if (nivelDeConexion <= 0) {
            this.nivelDeConexion = 0;
            this.desvincularDeMedium();
        }
    }

    public void conectar(Medium medium) {
        this.nivelDeConexion += (medium.getMana() * 20) / 100;
        this.capearNivelDeConexion();
        this.setOwner(medium);
    }

    public boolean esEspirituLibre() {
        return this.owner == null;
    }

    public void cambiarUbicacion(Ubicacion ubicacionNueva) {
        this.ubicacion.eliminarEspiritu(this);
        ubicacionNueva.agregarEspiritu(this);
        this.validarUbicacionPorTipo();
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    protected abstract void validarUbicacionPorTipo();

    public void setCustomRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    public abstract void atacar(Espiritu espiritu);

    public abstract void recibirAtaque(int ataque, Espiritu atacante);

    public void sufrirDerrota(int dmg) {
        this.disminuirConexion(dmg);
    }

    public abstract boolean esDemoniaco();

    public abstract boolean esAngelical();
}