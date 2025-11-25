package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoPuedeSerDominadoException;
import ar.edu.unq.epersgeist.modelo.exceptions.NivelDeConexionFueraDeRangoException;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.EspirituMongo;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.MediumMongo;
import ar.edu.unq.epersgeist.persistencia.sql.entity.*;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Embedded
    private Coordenada coordenada;
    private Ubicacion ubicacion;
    private Medium owner;
    private Date updatedAt;
    private Boolean deletedAt = false;
    private Espiritu dominante;
    private List<Espiritu> dominados = new ArrayList<>();

    @SuppressWarnings("unused")
    public Espiritu() {
        this.randomizer = new RandomizerImpl();
    }

    public Espiritu(int nivelDeConexion, String nombre, Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.randomizer = new RandomizerImpl();
        this.coordenada = ubicacion.generarCoordenadaAleatoria();
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

        this.dominados = espirituSQL.getDominados().stream().map(dominadoSQL -> {
            Espiritu espiritu;
            if (dominadoSQL instanceof EspirituAngelicalSQL) {
                espiritu = new EspirituAngelical(dominadoSQL.getId(), dominadoSQL.getNombre());
            } else {
                espiritu = new EspirituDemoniaco(dominadoSQL.getId(), dominadoSQL.getNombre());
            }
            espiritu.setDominante(this);
            return espiritu;
        }).collect(Collectors.toList());

        if (espirituSQL.getDominante() != null) {
            if (espirituSQL.getDominante() instanceof EspirituAngelicalSQL) {
                this.dominante = EspirituAngelical.from(espirituSQL.getDominante());
            } else {
                this.dominante = EspirituDemoniaco.from(espirituSQL.getDominante());;
            }
        }
    }

    public Espiritu(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public static Espiritu from(EspirituSQL espirituSQL, EspirituMongo espirituMongo) {
        Espiritu e;
        if (espirituSQL instanceof EspirituAngelicalSQL) {
            e = new EspirituAngelical(espirituSQL);
        } else {
            e = new EspirituDemoniaco(espirituSQL);
        }
        e.coordenada = new Coordenada(espirituMongo.getCoordenada());
        return e;
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

    public void cambiarUbicacion(Ubicacion ubicacionNueva, Coordenada coordenadaNueva) {
        this.ubicacion.eliminarEspiritu(this);
        ubicacionNueva.agregarEspiritu(this);
        this.validarUbicacionPorTipo();
        this.coordenada = coordenadaNueva;
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

    public void cambiarCoordenada(Coordenada coordenadaDestino) {
        setCoordenada(coordenadaDestino);
    }

    public boolean estaSiendoDominado() {
        return this.dominante != null;
    }

    public abstract void poseerMedium(Medium medium);
    public abstract void recibirAtaqueDeLuz(int fuerzaDeAtaque);

    public void dominar(Espiritu espirituADominar) {
        if(!espirituADominar.sePuedeDominar(this)) {
            throw new EspirituNoPuedeSerDominadoException("El espiritu no se puede dominar");
        }
        this.dominados.add(espirituADominar);
        espirituADominar.setDominante(this);
    }

    public boolean sePuedeDominar(Espiritu espirituDominante) {
        return esEspirituLibre() && estaEntr2O5KilometrosDeDistancia(espirituDominante.getCoordenada())
                && !esEspirituDominado(espirituDominante);
    }

    private boolean estaEntr2O5KilometrosDeDistancia(Coordenada coordenadaDominante) {
        double distancia = coordenada.distanciaEnKm(coordenadaDominante);
        return distancia >= 2 && distancia <= 5;
    }

    private boolean esEspirituDominado(Espiritu espirituDominante) {
        return this.dominados.stream()
                .anyMatch(e -> this.id == null ? this == espirituDominante : this.id == espirituDominante.getDominante().getId());
    }

}