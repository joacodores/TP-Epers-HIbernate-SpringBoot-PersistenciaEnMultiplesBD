package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
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
    @Transient
    protected Randomizer randomizer;
    @ManyToOne
    private Medium owner;
    @ManyToOne
    private Ubicacion ubicacion;

    @SuppressWarnings("unused")
    public Espiritu() {
        this.randomizer = new RandomizerImpl();
    }

    public Espiritu(@NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.randomizer = new RandomizerImpl();
    }

    public abstract boolean puedeExorcizar();

    public void conectar(Medium medium) {
        /*
        TODO: Para la rama de conectar hay que aplicar la lógica del mana y del service
            y probablemente también cambiar la lógica de aumentarConexion y los tests
            de espiritu
        */
        this.owner = medium;
    }

    private void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < minNivelDeConexion || nivelDeConexion > maxNivelDeConexion) {
            throw new NivelDeConexionFueraDeRangoException(String.format("El nivel de conexión para el espiritu %s es inválido (debe ser un número entre %s y %s)", this.nombre, this.minNivelDeConexion, this.maxNivelDeConexion));
        }
        this.nivelDeConexion = nivelDeConexion;
    }

    public Medium aumentarConexion(Medium medium, int i) {
        this.nivelDeConexion += i;
        if (this.nivelDeConexion >= 100) this.nivelDeConexion = 100;
        return medium;
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

    public void setCustomRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    public abstract void atacar(Espiritu espiritu);

    public abstract void recibirAtaque(int ataque, Espiritu atacante);

    public void sufrirDerrota(int dmg){
        this.disminuirConexion(dmg);
    }

}