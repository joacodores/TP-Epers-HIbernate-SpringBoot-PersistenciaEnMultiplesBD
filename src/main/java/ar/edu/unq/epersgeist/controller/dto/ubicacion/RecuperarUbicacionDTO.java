package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record RecuperarUbicacionDTO(Long id,
                                    TipoUbicacionDTO tipo,
                                    String nombre,
                                    Integer energia) {
    public static RecuperarUbicacionDTO desdeModelo(Ubicacion ubicacion) {

        return new RecuperarUbicacionDTO(
                ubicacion.getId(),
                TipoUbicacionDTO.desdeModelo(ubicacion),
                ubicacion.getNombre(),
                ubicacion.getEnergia()
        );
    }
}
