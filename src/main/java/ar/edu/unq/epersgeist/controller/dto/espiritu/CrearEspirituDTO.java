package ar.edu.unq.epersgeist.controller.dto.espiritu;

public record CrearEspirituDTO(String nombre,
                               TipoEspirituDTO tipo,
                               Integer nivelDeConexion,
                               Long ubicacionId) { }
