package ar.edu.unq.epersgeist.controller.dto.coordenada;

import ar.edu.unq.epersgeist.modelo.Coordenada;

public record CrearCoordenadaDTO(Double latitud, Double longitud) {

    public Coordenada aModelo() {
        return new Coordenada(latitud, longitud);
    }

    public static CrearCoordenadaDTO desdeModelo(Coordenada coordenada) {
        return new CrearCoordenadaDTO(coordenada.getLatitud(), coordenada.getLongitud());
    }
}


