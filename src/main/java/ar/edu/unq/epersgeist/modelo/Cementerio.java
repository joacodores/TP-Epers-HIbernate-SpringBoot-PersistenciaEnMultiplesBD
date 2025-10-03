package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("CEMENTERIO")
public class Cementerio extends Ubicacion{

    public Cementerio(String nombre, Integer energia){
        super(nombre, energia);
    }

    @Override
    public boolean permiteInvocar(Espiritu e) {
        return e.esDemoniaco();
    }

    @Override
    public int manaRecuperadaMedium() {
        return getEnergia() / 2;
    }

    @Override
    public int conexionGanadaPara(Espiritu e) {
        return e.esDemoniaco() ? getEnergia() : 0;
    }

    @Override
    public boolean esSantuario() {
        return false;
    }

    @Override
    public boolean esCementerio() {
        return true;
    }

}
