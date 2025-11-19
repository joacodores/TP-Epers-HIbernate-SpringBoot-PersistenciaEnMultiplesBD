package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.CoordenadaMongo;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
public class Coordenada {
    private double latitud;
    private double longitud;

    public Coordenada() { }

    public Coordenada(double latitud, double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Coordenada(CoordenadaMongo coordenada){
        this.longitud = coordenada.getLongitud();
        this.latitud = coordenada.getLatitud();
    }

    public double distanciaEnKm(Coordenada coordenada){
        double radioTierra = 6371.0;
        double dLat = Math.toRadians(coordenada.latitud - this.latitud);
        double dLon = Math.toRadians(coordenada.longitud - this.longitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(coordenada.latitud))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radioTierra * c;
    }
}
