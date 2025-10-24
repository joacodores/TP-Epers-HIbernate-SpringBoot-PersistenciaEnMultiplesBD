package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public record RecuperarMediumDTO(Long id,
                                 String nombre,
                                 Integer manaMax,
                                 Integer mana,
                                 Long ubicacionId,
                                 List<RecuperarEspirituDTO> espiritus,
                                 RecuperarUbicacionDTO ubicacion) {

    public static RecuperarMediumDTO desdeModelo(Medium medium) {
        // Capaz haya que cambiar los desdeModelo de espiritu y medium para que sean llamadas a los service.
        return new RecuperarMediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getManaMax(),
                medium.getMana(),
                medium.getUbicacion().getId(),
                medium.getEspiritus().stream().map(RecuperarEspirituDTO::desdeModelo).toList(),
                RecuperarUbicacionDTO.desdeModelo(medium.getUbicacion())
        );
    }

}
