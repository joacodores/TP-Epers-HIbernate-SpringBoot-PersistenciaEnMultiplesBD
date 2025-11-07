package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.controller.exceptions.ConexionPsionicaException;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.MediumMongo;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.MediumSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static java.lang.Integer.min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medium {

    private final Date createdAt = new Date();
    private Long id;
    private String nombre;
    private Integer manaMax;
    private Integer mana;
    private List<Espiritu> espiritus = new ArrayList<>();
    private Ubicacion ubicacion;

    @Embedded
    private Coordenada coordenada;
    private Date updatedAt;
    private Boolean deletedAt = false;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = min(manaMax, mana);
        this.ubicacion = ubicacion;
        this.coordenada = ubicacion.generarCoordenadaAleatoria();
        ubicacion.agregarMedium(this);
    }

    public Medium(MediumSQL mediumSQL) {
        this.id = mediumSQL.getId();
        this.nombre = mediumSQL.getNombre();
        this.manaMax = mediumSQL.getManaMax();
        this.mana = mediumSQL.getMana();
        for (var espirituSQL : mediumSQL.getEspiritus()) {
            Espiritu espiritu;
            if (espirituSQL instanceof EspirituAngelicalSQL) {
                espiritu = new EspirituAngelical(espirituSQL);
            } else {
                espiritu = new EspirituDemoniaco(espirituSQL);
            }
            this.espiritus.add(espiritu);
            espiritu.setOwner(this);
        }
        if (mediumSQL.getUbicacion() instanceof SantuarioSQL) {
            this.ubicacion = new Santuario(mediumSQL.getUbicacion().getNombre(), mediumSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(mediumSQL.getUbicacion().getId());
        } else {
            this.ubicacion = new Cementerio(mediumSQL.getUbicacion().getNombre(), mediumSQL.getUbicacion().getEnergia());
            this.ubicacion.setId(mediumSQL.getUbicacion().getId());
        }
    }

    public static Medium from(MediumSQL mediumSQL, MediumMongo mediumMongo) {
        Medium m = new Medium(mediumSQL);
        m.coordenada = new Coordenada(mediumMongo.getCoordenada());
        return m;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (!espiritu.esEspirituLibre() || !comparteUbicacion(espiritu)) {
            throw new EspirituNoPuedeConectarException("El espíritu no puede conectarse al medium");
        }
        espiritus.add(espiritu);
        espiritu.conectar(this);
    }

    public boolean comparteUbicacion(Espiritu espiritu) {
        return (this.ubicacion.getId() != null ? this.ubicacion.getId().equals(espiritu.getUbicacion().getId()) : this.ubicacion == espiritu.getUbicacion());
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
        espiritu.cambiarUbicacion(ubicacionDeMedium, this.getCoordenada());
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

    public void mover(Coordenada coordenadaDestino) {
        if (this.ubicacion.estaDentro(coordenadaDestino)) {
            setCoordenada(coordenadaDestino);
            new ArrayList<>(espiritus).forEach(espiritu -> espiritu.cambiarCoordenada(coordenadaDestino));
            return;
        }
        Optional<ConexionPsionica> conexionOptional = this.ubicacion.getConexiones().stream()
                .filter(conexion -> conexion.getDestino().estaDentro(coordenadaDestino))
                .findFirst();
        if (conexionOptional.isEmpty()) {
            throw new ConexionPsionicaException("La conexion no existe");
        }
        Ubicacion ubi = conexionOptional.get().getDestino();
        Integer costoConexion = conexionOptional.get().getCosto();
        setCoordenada(coordenadaDestino);
        setUbicacion(ubi);
        this.mana = Math.max(0, this.mana - costoConexion);
        // iteramos sobre una copia para evitar ConcurrentModificationException
        new ArrayList<>(espiritus).forEach(espiritu -> espiritu.cambiarUbicacion(ubi, coordenadaDestino));
    }

    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    public void setearConexiones(Set<ConexionPsionica> conexiones) {
        this.ubicacion.setConexiones(conexiones);
    }

}
