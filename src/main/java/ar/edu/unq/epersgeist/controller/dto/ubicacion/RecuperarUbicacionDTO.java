package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

public record RecuperarUbicacionDTO(Long id,
                                    TipoUbicacionDTO tipo,
                                    String nombre,
                                    Integer energia) {

    public static RecuperarUbicacionDTO desdeModelo(Ubicacion ubicacion) {
        /* TODO: Luego de terminar la migración
         return new RecuperarUbicacionDTO(ubicacion.getId(), TipoUbicacionDTO.getTipo(ubicacion.getClass()) o algo asi, ubicacion.getNombre(), ubicacion.getEnergia());
        */
        return new RecuperarUbicacionDTO(ubicacion.getId(), null, ubicacion.getNombre(), null);
    }
}
