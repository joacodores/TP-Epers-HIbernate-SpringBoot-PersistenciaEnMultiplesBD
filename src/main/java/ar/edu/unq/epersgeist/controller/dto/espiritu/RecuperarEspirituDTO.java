package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;

public record RecuperarEspirituDTO(Long id,
                                   TipoEspirituDTO tipo,
                                   String nombre,
                                   Integer nivelDeConexion,
                                   Long mediumId,
                                   RecuperarUbicacionDTO ubicacion) {

    //TODO: actualizar cuando se haga el controller de coordenada
    public static RecuperarEspirituDTO desdeModelo(Espiritu espiritu) {
        return new RecuperarEspirituDTO(
                espiritu.getId(),
                TipoEspirituDTO.desdeModelo(espiritu),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getOwner() != null ? espiritu.getOwner().getId() : null,
                RecuperarUbicacionDTO.desdeModelo(espiritu.getUbicacion())
        );
    }

}
