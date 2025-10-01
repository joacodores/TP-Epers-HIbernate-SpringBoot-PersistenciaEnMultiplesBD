package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.Optional;

public record ActualizarMediumDTO(String nombre, Integer manaMax, Integer mana) {
    public void actualizarMedium(Medium medium, Long id) {
        medium.setId(id);
        Optional.ofNullable(nombre).ifPresent(medium::setNombre);
        Optional.ofNullable(manaMax).ifPresent(medium::setManaMax);
        Optional.ofNullable(mana).ifPresent(medium::setMana);
    }
}
