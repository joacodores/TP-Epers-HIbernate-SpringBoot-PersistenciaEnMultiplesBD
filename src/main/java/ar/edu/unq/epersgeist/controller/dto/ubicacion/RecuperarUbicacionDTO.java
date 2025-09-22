package ar.edu.unq.epersgeist.controller.dto.ubicacion;

public record RecuperarUbicacionDTO(Long id,
                                    TipoUbicacionDTO tipo,
                                    String nombre,
                                    Integer energia) { }
