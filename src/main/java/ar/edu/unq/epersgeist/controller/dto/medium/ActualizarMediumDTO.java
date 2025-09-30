package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record ActualizarMediumDTO(String nombre, Integer manaMax, Integer mana) {

    public Medium aModelo(Long id) {
        Ubicacion ubicacionNula = new Ubicacion("ignorar");
        Medium m = new Medium(nombre, manaMax, mana, ubicacionNula );
        m.setId(id);
        return m;
    }
}
