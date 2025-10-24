package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.MediumSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public abstract class Espiritu {

    private final int maxNivelDeConexion = 100;
    private final int minNivelDeConexion = 0;
    private final Date createdAt = new Date();
    protected Randomizer randomizer;
    private Long id;
    private int nivelDeConexion;
    private String nombre;
    private Ubicacion ubicacion;
    private Medium owner;
    private Date updatedAt;
    private Boolean deletedAt = false;

    @SuppressWarnings("unused")
    public Espiritu() {
        this.randomizer = new RandomizerImpl();
    }

    public Espiritu(int nivelDeConexion, String nombre, Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.randomizer = new RandomizerImpl();
        ubicacion.agregarEspiritu(this);
    }

    public Espiritu(EspirituSQL espirituSQL) {
        validarNivelDeConexion(espirituSQL.getNivelDeConexion());
        this.id = espirituSQL.getId();
        this.nombre = espirituSQL.getNombre();
        this.randomizer = new RandomizerImpl();
        if (espirituSQL.getUbicacion() instanceof SantuarioSQL) {
            this.ubicacion = new Santuario(espirituSQL.getUbicacion().getNombre(), espirituSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(espirituSQL.getUbicacion().getId());
        } else {
            this.ubicacion = new Cementerio(espirituSQL.getUbicacion().getNombre(), espirituSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(espirituSQL.getUbicacion().getId());
        }
        if (espirituSQL.getOwner() != null) {
            MediumSQL ownerSQL = espirituSQL.getOwner();
            Medium owner = new Medium();
            owner.setId(ownerSQL.getId());
            owner.setNombre(ownerSQL.getNombre());
            this.owner = owner;
        }
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
        //owner.desvincularEspiritu(this);
        this.getOwner().desvincularEspiritu(this);
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