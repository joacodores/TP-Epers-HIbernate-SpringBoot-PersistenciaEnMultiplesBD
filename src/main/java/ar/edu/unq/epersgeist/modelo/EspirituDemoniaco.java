package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Entity
public class EspirituDemoniaco extends Espiritu{
    public EspirituDemoniaco(Integer nivelDeConexion, String nombre) {
        super(nivelDeConexion, nombre);
    }

    @Override
    public String getTipo() {
        return "Demoniaco";
    }
}
