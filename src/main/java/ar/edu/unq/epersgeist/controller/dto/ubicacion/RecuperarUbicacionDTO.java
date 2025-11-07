package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.controller.dto.coordenada.CrearCoordenadaDTO;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Set;
import java.util.stream.Collectors;

public record RecuperarUbicacionDTO(Long id,
                                    TipoUbicacionDTO tipo,
                                    String nombre,
                                    Integer energia,
                                    Set<CrearCoordenadaDTO> coordenadas) {

    public static RecuperarUbicacionDTO desdeModelo(Ubicacion ubicacion) {
        Set<CrearCoordenadaDTO> coordenadasDTO = ubicacion.getCoordenadas().stream()
                .map(CrearCoordenadaDTO::desdeModelo)
                .collect(Collectors.toSet());
        return new RecuperarUbicacionDTO(
                ubicacion.getId(),
                TipoUbicacionDTO.desdeModelo(ubicacion),
                ubicacion.getNombre(),
                ubicacion.getEnergia(),
                coordenadasDTO
        );
    }

}
