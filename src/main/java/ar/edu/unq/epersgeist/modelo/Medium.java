package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.*;
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
import java.util.Optional;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.Integer.min;

@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Medium SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public class Medium {
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
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Espiritu> espiritus = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private Ubicacion ubicacion;

    @Temporal(TemporalType.DATE)
    private final Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Boolean deletedAt = false;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
        this.ubicacion = ubicacion;
        ubicacion.agregarMedium(this);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (!espiritu.esEspirituLibre() || !comparteUbicacion(espiritu)) {
            throw new EspirituNoPuedeConectarException("El espíritu no puede conectarse al medium");
        }
        espiritus.add(espiritu);
        espiritu.conectar(this);
    }

    public boolean comparteUbicacion(Espiritu espiritu) {
        return (this.ubicacion == espiritu.getUbicacion());
    }

    public void desvincularEspiritu(Espiritu espiritu) {
        espiritus.remove(espiritu);
    }

    private boolean tieneAlMenosUnEspirituAngelical() {
        return espiritus.stream().anyMatch(Espiritu::puedeExorcizar);
    }

    private List<Espiritu> getEspiritusAngelicales() {
        return this.espiritus.stream()
                .filter(Espiritu::puedeExorcizar)
                .toList();
    }

    public Optional<Espiritu> getEspirituAExorcizar() {
        return this.espiritus.stream()
                .filter(e -> !e.puedeExorcizar())
                .findFirst();
    }

    public void disminuirMana(Integer mana) {
        this.mana = this.mana - mana;
    }

    public void exorcizar(Medium mediumAExorcizar) {
        if (!this.tieneAlMenosUnEspirituAngelical()) {
            throw new ExorcistaSinAngelesException("El medium exorcista %s no puede realizar un exorcismo, ya que no posee ningún Espiritu Angelical");
        }
        this.getEspiritusAngelicales().forEach(a -> mediumAExorcizar.getEspirituAExorcizar().ifPresent(a::atacar));
    }

    public void vaciarEspiritus() {
        espiritus.forEach(Espiritu::desvincularDeMedium);
    }

    public void aumentarMana(int i) {
        this.mana = Math.min(this.mana + i, this.manaMax);
    }

    public void aumentarNivelDeConexionAEspiritusDeMediumEn(Ubicacion ubicacionDeDescanso) {
        espiritus.forEach(espiritu -> espiritu.aumentarConexion(ubicacionDeDescanso));
    }

    public void invocar(Espiritu espiritu) {
        Ubicacion ubicacionDeMedium = this.getUbicacion();
        if (!espiritu.esEspirituLibre()) {
            throw new EspirituNoEsLibreException("El espíritu no puede ser invocado, ya que no es libre");
        }
        if (!ubicacionDeMedium.permiteInvocar(espiritu)) {
            throw new EspirituNoPuedeInvocarseEnUbicacionException("El espíritu no puede ser invocado en esta ubicacion");
        }
        if (getMana() < 10) {
            return;
        }
        espiritu.cambiarUbicacion(ubicacionDeMedium);
        disminuirMana(10);
    }

    public void descansar() {
        Ubicacion ubicacionDeDescanso = this.getUbicacion();
        this.aumentarMana(ubicacionDeDescanso.manaRecuperadaMedium());
        this.aumentarNivelDeConexionAEspiritusDeMediumEn(ubicacionDeDescanso);
    }

    public void setMana(Integer mana) {
        if (mana > manaMax)
            throw new MediumNoPuedeTenerMasManaQueSuManaMax("El Medium no puede tener mas mana que su cantidad maxima permitida");
        this.mana = mana;
    }

    public void mover(Ubicacion ubicacion) {
        setUbicacion(ubicacion);
        // iteramos sobre una copia para evitar ConcurrentModificationException
        new ArrayList<>(espiritus).forEach(espiritu -> espiritu.cambiarUbicacion(ubicacion));
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }
}