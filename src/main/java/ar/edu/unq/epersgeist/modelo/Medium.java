package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.controller.exceptions.ConexionPsionicaException;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.persistencia.sql.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.GenerationType.AUTO;
import static java.lang.Integer.min;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medium {

    private Long id;
    private String nombre;
    private Integer manaMax;
    private Integer mana;
    private List<Espiritu> espiritus = new ArrayList<>();
    private Ubicacion ubicacion;
    private final Date createdAt = new Date();
    private Date updatedAt;
    private Boolean deletedAt = false;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
        this.ubicacion = ubicacion;
        ubicacion.agregarMedium(this);
    }

    public Medium(MediumSQL mediumSQL) {
        this.id = mediumSQL.getId();
        this.nombre = mediumSQL.getNombre();
        this.manaMax = mediumSQL.getManaMax();
        this.mana = mediumSQL.getMana();
        for (var espirituSQL : mediumSQL.getEspiritus()){
            Espiritu espiritu;
            if(espirituSQL instanceof EspirituAngelicalSQL) {
                espiritu = new EspirituAngelical(espirituSQL);
            } else {
                espiritu = new EspirituDemoniaco(espirituSQL);
            }
            this.espiritus.add(espiritu);
            espiritu.setOwner(this);
        }
        if (mediumSQL.getUbicacion() instanceof SantuarioSQL){
            this.ubicacion = new Santuario(mediumSQL.getUbicacion().getNombre(), mediumSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(mediumSQL.getUbicacion().getId());
        }else{
            this.ubicacion = new Cementerio(mediumSQL.getUbicacion().getNombre(), mediumSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(mediumSQL.getUbicacion().getId());
        }
    }

    public static Medium from(MediumSQL mediumSQL) {
        return new Medium(mediumSQL);
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (!espiritu.esEspirituLibre() || !comparteUbicacion(espiritu)) {
            throw new EspirituNoPuedeConectarException("El espíritu no puede conectarse al medium");
        }
        espiritus.add(espiritu);
        espiritu.conectar(this);
    }

    public boolean comparteUbicacion(Espiritu espiritu) {
        return (this.ubicacion.getId() != null ? this.ubicacion.getId() == espiritu.getUbicacion().getId() : this.ubicacion == espiritu.getUbicacion());
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
        Optional<ConexionPsionica> conexionOptional = ubicacion.getConexiones().stream().filter(conexion -> conexion.getDestinoId() == this.getUbicacion().getId()).findFirst();
        if(conexionOptional.isEmpty()) {
            throw new ConexionPsionicaException("La conexion no existe");
        }
        setUbicacion(ubicacion);
        this.mana = Math.max(0, this.mana - conexionOptional.get().getCosto());
        // iteramos sobre una copia para evitar ConcurrentModificationException
        new ArrayList<>(espiritus).forEach(espiritu -> espiritu.cambiarUbicacion(ubicacion));
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    void internalSetUbicacion(Ubicacion u) { this.ubicacion = u; }
    void internalAddEspiritu(Espiritu e) {
        if (e != null && !espiritus.contains(e)) espiritus.add(e);
    }
}
