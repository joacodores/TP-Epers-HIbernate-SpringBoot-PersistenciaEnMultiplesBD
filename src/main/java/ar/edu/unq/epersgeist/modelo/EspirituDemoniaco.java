package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;

@Entity
public class EspirituDemoniaco extends Espiritu{

    @SuppressWarnings("unused")
    public EspirituDemoniaco() {
        super();
    }

    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }

    @Override
    public boolean puedeExorcizar() {
        return false;
    }

    private boolean fueAtaqueExitoso(int ataque){
        return ataque > this.randomizer.lanzarDadoDeDefensa();
    }

    public void atacar(Espiritu espiritu){}

    public void recibirAtaque(int ataque, Espiritu atacante) {
        if (fueAtaqueExitoso(ataque)) {
            this.sufrirDerrota(atacante.getNivelDeConexion() / 2);
        } else {
            atacante.sufrirDerrota(5);
        }
    }

}
