package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Optional;

public record ActualizarUbicacionDTO(String nombre, Integer energia) {
    public void actualizarUbicacion(Ubicacion ubicacion) {

        Optional.ofNullable(energia).ifPresent(ubicacion::setEnergia);
        Optional.ofNullable(nombre).ifPresent(ubicacion::setNombre);
    }
}
