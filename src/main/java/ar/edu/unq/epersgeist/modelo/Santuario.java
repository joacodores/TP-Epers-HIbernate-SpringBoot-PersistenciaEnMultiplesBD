package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Entity
@DiscriminatorValue("SANTUARIO")
@SQLDelete(sql = "UPDATE Ubicacion SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at=false")
public class Santuario extends Ubicacion{

    public Santuario(String nombre, Integer energia){
        super(nombre, energia);
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
