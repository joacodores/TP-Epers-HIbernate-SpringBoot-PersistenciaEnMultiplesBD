package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;

public enum TipoEspirituDTO {
    ANGELICAL, DEMONIO;

    public static TipoEspirituDTO desdeModelo(Espiritu espiritu) {
        if (espiritu instanceof EspirituAngelical) {
            return ANGELICAL;
        } else if (espiritu instanceof EspirituDemoniaco) {
            return DEMONIO;
        }
        throw new IllegalArgumentException("No se encontro el tipo de espiritu");
    }
}
