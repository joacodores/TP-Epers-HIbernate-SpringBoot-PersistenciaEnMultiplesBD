package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;

public class Cementerio extends Ubicacion {

    public Cementerio(String nombre, Integer energia) {
        super(nombre, energia);
    }

    public Cementerio(UbicacionSQL ubicacionSQL) {
        super(ubicacionSQL);
    }

    public static Cementerio from(UbicacionSQL ubicacionSQL) {
        return new Cementerio(ubicacionSQL);
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
