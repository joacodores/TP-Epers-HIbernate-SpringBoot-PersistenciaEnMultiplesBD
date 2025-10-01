package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import static ar.edu.unq.epersgeist.controller.dto.espiritu.TipoEspirituDTO.ANGELICAL;

public record CrearEspirituDTO(String nombre,
                               TipoEspirituDTO tipo,
                               Integer nivelDeConexion,
                               Long ubicacionId) {
    public Espiritu aModelo(Ubicacion ubicacion) {
        if (tipo == ANGELICAL) {
            return new EspirituAngelical(nivelDeConexion, nombre, ubicacion);
        } else {
            return new EspirituDemoniaco(nivelDeConexion, nombre, ubicacion);
        }
    }
}
