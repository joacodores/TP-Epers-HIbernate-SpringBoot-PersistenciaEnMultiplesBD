package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;

import static java.lang.Math.min;

@Entity
public class EspirituAngelical extends Espiritu {
    @SuppressWarnings("unused")
    public EspirituAngelical() {
        super();
    }

    public EspirituAngelical(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }

    @Override
    public boolean puedeExorcizar() {
        return true;
    }

    @Override
    protected void validarUbicacionPorTipo() {
        if (getUbicacion().esCementerio()){
            this.disminuirConexion(5);
        }
    }

    private int calcularPorcentajeDeAtaqueExitoso() {
        int dado = this.randomizer.lanzarDadoDeAtaque();
        return min(dado + this.getNivelDeConexion(), 100);
    }

    @Override
    public void atacar(Espiritu espiritu) {
        espiritu.recibirAtaque(this.calcularPorcentajeDeAtaqueExitoso(), this);
    }

    @Override
    public void recibirAtaque(int ataque, Espiritu atacante) {
    }

    @Override
    public boolean esDemoniaco() {
        return false;
    }

    @Override
    public boolean esAngelical() {
        return true;
    }
}
