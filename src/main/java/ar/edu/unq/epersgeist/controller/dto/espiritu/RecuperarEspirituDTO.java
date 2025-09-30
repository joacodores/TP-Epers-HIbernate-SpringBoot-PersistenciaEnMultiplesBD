package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;

public record RecuperarEspirituDTO(Long id,
                                   TipoEspirituDTO tipo,
                                   String nombre,
                                   Integer nivelDeConexion,
                                   Long mediumId,
                                   RecuperarUbicacionDTO ubicacion) {

    public static RecuperarEspirituDTO desdeModelo(Espiritu espiritu) {
        return new RecuperarEspirituDTO(
                espiritu.getId(),
                null,
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getOwner()!= null ? espiritu.getOwner().getId(): null,
                RecuperarUbicacionDTO.desdeModelo(espiritu.getUbicacion())
                );
    }
}
