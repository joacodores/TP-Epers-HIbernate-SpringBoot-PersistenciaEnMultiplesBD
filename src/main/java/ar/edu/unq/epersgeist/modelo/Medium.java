package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoEsLibreException;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoPuedeConectarException;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.Integer.min;

@NoArgsConstructor

@Entity
public class Medium {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(nullable = false, length = 500)
    private String nombre;
    @Column(nullable = false)
    private Integer manaMax;
    @Column(nullable = false)
    private Integer mana;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Espiritu> espiritus = new HashSet<>();
    @ManyToOne
    private Ubicacion ubicacion;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {

        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
        this.ubicacion = ubicacion;
        ubicacion.agregarMedium(this);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if(!espiritu.esEspirituLibre() || !comparteUbicacion(espiritu)){
            throw new EspirituNoPuedeConectarException("El espíritu no puede conectarse al medium");
        }
        espiritus.add(espiritu);
        espiritu.conectar(this);
    }

    public boolean comparteUbicacion(Espiritu espiritu) {
        return (this.ubicacion == espiritu.getUbicacion());
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getManaMax() {
        return manaMax;
    }

    public Integer getMana() {
        return mana;
    }

    public void disminuirMana(Integer mana) {this.mana = this.mana - mana;}

    public Set<Espiritu> getEspiritus() {
        return espiritus;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void invocar(Espiritu espiritu){
        Ubicacion ubicacionDeMedium = this.getUbicacion();
        Ubicacion ubicacionDeEspiritu = espiritu.getUbicacion();
        if(!espiritu.esEspirituLibre()) {
            throw new EspirituNoEsLibreException("El espíritu no puede ser invocado, ya que no es libre");
        }
        if(getMana() < 10) {
            return;
        }
        espiritu.cambiarUbicacion(ubicacionDeMedium);
        disminuirMana(10);
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}