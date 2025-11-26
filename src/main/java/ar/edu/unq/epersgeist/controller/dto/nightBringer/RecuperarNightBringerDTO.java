package ar.edu.unq.epersgeist.controller.dto.nightBringer;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.modelo.NightBringer;

import java.util.List;

public record RecuperarNightBringerDTO(Long id,
                                       String nombre,
                                       List<RecuperarEspirituDTO> espiritus) {
    public static RecuperarNightBringerDTO desdeModelo(NightBringer nightBringer) {
        return new RecuperarNightBringerDTO(
                nightBringer.getId(),
                nightBringer.getNombre(),
                nightBringer.getEspiritus().stream().map(RecuperarEspirituDTO::desdeModelo).toList()
        );
    }
}
