package ar.edu.unq.epersgeist.persistencia.mongo.entity;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import lombok.*;

@Data
@NoArgsConstructor
public class CoordenadaMongo {
    private double latitud;
    private double longitud;

    public CoordenadaMongo(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public CoordenadaMongo(Coordenada coordenada) {
        this.latitud = coordenada.getLatitud();
        this.longitud = coordenada.getLongitud();
    }
}
