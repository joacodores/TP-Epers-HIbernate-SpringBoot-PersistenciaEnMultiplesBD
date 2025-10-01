package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;

public record ActualizarEspirituDTO(String nombre) {
    public Espiritu aModelo(Long id) {
        Espiritu e = new EspirituAngelical();
        e.setNombre(nombre);
        e.setId(id);
        return e;
    }
}
