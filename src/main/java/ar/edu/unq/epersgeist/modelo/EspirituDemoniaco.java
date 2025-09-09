package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Entity
public class EspirituDemoniaco extends Espiritu{
    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
    }

    @Override
    public String getTipo() {
        return "Demoniaco";
    }
}
