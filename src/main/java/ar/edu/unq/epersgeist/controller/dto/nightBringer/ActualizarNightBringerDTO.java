package ar.edu.unq.epersgeist.controller.dto.nightBringer;

import ar.edu.unq.epersgeist.modelo.NightBringer;

import java.util.Optional;

public record ActualizarNightBringerDTO(String nombre) {
    public void actualizarNightBringer(NightBringer nightBringer, Long id) {
        nightBringer.setId(id);
        Optional.ofNullable(nombre).ifPresent(nightBringer::setNombre);
    }
}
