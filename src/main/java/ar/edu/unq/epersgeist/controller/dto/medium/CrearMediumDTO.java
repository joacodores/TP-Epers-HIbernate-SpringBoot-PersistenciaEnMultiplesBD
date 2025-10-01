package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record CrearMediumDTO(String nombre, Integer manaMax, Integer mana, Long ubicacionId) {
    public Medium aModelo(Ubicacion ubicacion) {
        return new Medium(nombre, manaMax, mana, ubicacion);
    }
}
