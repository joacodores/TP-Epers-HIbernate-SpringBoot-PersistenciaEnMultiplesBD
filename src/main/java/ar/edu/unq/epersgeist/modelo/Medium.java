package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.Integer.min;

@NoArgsConstructor

@Entity
public class Medium implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(nullable = false, length = 500)
    private String nombre;
    @Column(nullable = false)
    private Integer manaMax;
    @Column(nullable = false)
    private Integer mana;
    private Set<Espiritu> espiritus = new HashSet<>();

    public Medium(String nombre, Integer manaMax, Integer mana) {

        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        espiritus.add(espiritu);
        espiritu.aumentarConexion(this);
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

    public Set<Espiritu> getEspiritus() {
        return espiritus;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public void setNombre(String nombre) { this.nombre = nombre; }
}