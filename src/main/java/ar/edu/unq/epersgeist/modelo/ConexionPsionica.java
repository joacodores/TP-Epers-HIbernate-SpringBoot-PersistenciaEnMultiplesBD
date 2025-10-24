package ar.edu.unq.epersgeist.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConexionPsionica {

    private Long id;
    private Ubicacion destino;
    private int costo;

    public ConexionPsionica(Ubicacion destino, int costo) {
        this.destino = destino;
        this.costo = costo;
    }

}

