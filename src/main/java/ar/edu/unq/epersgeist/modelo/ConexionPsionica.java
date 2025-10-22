package ar.edu.unq.epersgeist.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConexionPsionica {
    private Long id;
    private Long destinoId;
    private int costo;

    public ConexionPsionica(Long destinoId, int costo) {
        this.destinoId = destinoId; this.costo = costo;
    }

}

