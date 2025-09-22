package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;

import java.util.List;

public record RecuperarMediumDTO(Long id,
                                 String nombre,
                                 Integer manaMax,
                                 Integer mana,
                                 Long ubicacionId,
                                 List<RecuperarEspirituDTO> espiritus,
                                 RecuperarUbicacionDTO ubicacion) { }
