package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.EspirituMongo;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;

public class EspirituDemoniaco extends Espiritu {

    @SuppressWarnings("unused")
    public EspirituDemoniaco() {
        super();
    }

    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }

    public EspirituDemoniaco(Long id, String nombre) {
        super(id, nombre);
    }

    public EspirituDemoniaco(EspirituSQL espirituSQL) {
        super(espirituSQL);
    }

    public static EspirituDemoniaco from(EspirituSQL espirituSQL) {
        return new EspirituDemoniaco(espirituSQL);
    }

    @Override
    public boolean puedeExorcizar() {
        return false;
    }

    @Override
    protected void validarUbicacionPorTipo() {
        if (getUbicacion().esSantuario()) {
            this.disminuirConexion(10);
        }
    }

    private boolean fueAtaqueExitoso(int ataque) {
        return ataque > this.randomizer.lanzarDadoDeDefensa();
    }

    public void atacar(Espiritu espiritu) {
    }

    public void recibirAtaque(int ataque, Espiritu atacante) {
        if (fueAtaqueExitoso(ataque)) {
            this.sufrirDerrota(atacante.getNivelDeConexion() / 2);
        } else {
            atacante.sufrirDerrota(5);
        }
    }

    @Override
    public boolean esDemoniaco() {
        return true;
    }

    @Override
    public boolean esAngelical() {
        return false;
    }

}
