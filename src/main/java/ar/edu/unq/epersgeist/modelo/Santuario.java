package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

public class Santuario extends Ubicacion{

    public Santuario(String nombre, Integer energia, Long costo){
        super(nombre, energia, costo);
    }

    public Santuario(UbicacionSQL ubicacionSQL){
        super(ubicacionSQL);
    }

    public static Santuario from(UbicacionSQL ubicacionSQL) {
        return new Santuario(ubicacionSQL);
    }


    @Override
    public boolean permiteInvocar(Espiritu e) {
        return e.esAngelical();
    }

    @Override
    public int manaRecuperadaMedium() {
        return (getEnergia() * 150) / 100;
    }

    @Override
    public int conexionGanadaPara(Espiritu e) {
        return e.esAngelical() ? getEnergia() : 0;
    }


    @Override
    public boolean esSantuario() {
        return true;
    }

    @Override
    public boolean esCementerio() {
        return false;
    }
}
