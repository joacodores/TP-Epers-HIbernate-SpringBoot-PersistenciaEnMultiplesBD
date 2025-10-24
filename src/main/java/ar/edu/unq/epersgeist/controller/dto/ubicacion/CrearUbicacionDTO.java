package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import static ar.edu.unq.epersgeist.controller.dto.ubicacion.TipoUbicacionDTO.CEMENTERIO;
import static ar.edu.unq.epersgeist.controller.dto.ubicacion.TipoUbicacionDTO.SANTUARIO;

public record CrearUbicacionDTO(String nombre,
                                Integer energia,
                                TipoUbicacionDTO tipo) {

    public Ubicacion aModelo() {
        if (tipo.equals(SANTUARIO)) {
            return new Santuario(nombre, energia);
        } else if (tipo.equals(CEMENTERIO)) {
            return new Cementerio(nombre, energia);
        }
        throw new IllegalArgumentException("Tipo de ubicacion invalido");
    }

}
