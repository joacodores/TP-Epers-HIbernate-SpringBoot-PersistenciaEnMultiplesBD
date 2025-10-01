package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record ActualizarMediumDTO(String nombre, Integer manaMax, Integer mana) {
    public Medium aModelo(Long id) {
        Medium m = new Medium(nombre, manaMax, mana, new Ubicacion("ignorar"));
        m.setId(id);
        return m;
    }
}
