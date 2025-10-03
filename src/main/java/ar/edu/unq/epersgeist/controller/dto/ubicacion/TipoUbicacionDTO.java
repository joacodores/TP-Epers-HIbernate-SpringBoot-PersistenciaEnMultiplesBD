package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public enum TipoUbicacionDTO {
    CEMENTERIO, SANTUARIO;

    public static TipoUbicacionDTO desdeModelo(Ubicacion ubicacion) {
        if (ubicacion instanceof Cementerio) {
            return CEMENTERIO;
        } else if (ubicacion instanceof Santuario) {
            return SANTUARIO;
        }
        throw new IllegalArgumentException("No se encontro el tipo de ubicacion");
    }
}
