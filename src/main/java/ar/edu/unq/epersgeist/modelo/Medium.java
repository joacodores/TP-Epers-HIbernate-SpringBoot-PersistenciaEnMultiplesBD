package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.ExorcistaSinAngelesException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.Integer.min;

@NoArgsConstructor

@Entity
public class Medium {
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Getter @Setter
    @Column(nullable = false, length = 500)
    private String nombre;
    @Getter
    @Column(nullable = false)
    private Integer manaMax;
    @Getter
    @Column(nullable = false)
    private Integer mana;
    @Getter
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Espiritu> espiritus = new ArrayList<>();

    public Medium(String nombre, Integer manaMax, Integer mana) {
        this.setNombre(nombre);
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (espiritus.contains(espiritu)) return;
        espiritus.add(espiritu);
        // TODO: en rama conectar probablemente aumentarConexion tenga que cambiarse
        espiritu.aumentarConexion(this, 10);
        espiritu.conectar(this);
    }

    public void desvincularEspiritu(Espiritu espiritu) {
        espiritus.remove(espiritu);
    }

    private boolean tieneAlMenosUnEspirituAngelical(){
        return espiritus.stream().anyMatch(Espiritu::puedeExorcizar);
    }

    private List<Espiritu> getEspiritusAngelicales(){
        return this.espiritus.stream()
                .filter(Espiritu::puedeExorcizar)
                .toList();
    }

    public Optional<Espiritu> getEspirituAExorcizar() {
        return this.espiritus.stream()
                .filter(e -> !e.puedeExorcizar())
                .findFirst();
    }

    public void exorcizar(Medium mediumAExorcizar){
        if(!this.tieneAlMenosUnEspirituAngelical()){
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

    public void aumentarNivelDeConexionATodosLosEspiritus(int i) {
        espiritus.forEach(espiritu -> espiritu.aumentarConexion(this, i));
    }
}