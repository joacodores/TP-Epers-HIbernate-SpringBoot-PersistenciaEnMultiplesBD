package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.controller.dto.coordenada.CrearCoordenadaDTO;
import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Set;
import java.util.stream.Collectors;

import static ar.edu.unq.epersgeist.controller.dto.ubicacion.TipoUbicacionDTO.CEMENTERIO;
import static ar.edu.unq.epersgeist.controller.dto.ubicacion.TipoUbicacionDTO.SANTUARIO;

public record CrearUbicacionDTO(String nombre,
                                Integer energia,
                                TipoUbicacionDTO tipo,
                                Set<CrearCoordenadaDTO> coordenadas) {

    public Ubicacion aModelo() {
        Set<Coordenada> coordenadasModelo = coordenadas.stream()
                .map(CrearCoordenadaDTO::aModelo)
                .collect(Collectors.toSet());
        if (tipo.equals(SANTUARIO)) {
            return new Santuario(nombre, energia, coordenadasModelo);
        } else if (tipo.equals(CEMENTERIO)) {
            return new Cementerio(nombre, energia, coordenadasModelo);
        }
        throw new IllegalArgumentException("Tipo de ubicacion invalido");
    }

}
