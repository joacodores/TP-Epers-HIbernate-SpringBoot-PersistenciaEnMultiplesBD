package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;

public record RecuperarEspirituDTO(Long id,
                                   TipoEspirituDTO tipo,
                                   String nombre,
                                   Integer nivelDeConexion,
                                   Long mediumId,
                                   RecuperarUbicacionDTO ubicacion) { }
