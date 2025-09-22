package ar.edu.unq.epersgeist.controller.dto.estadistica;

import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;

public record ReporteSantuarioMasCorruptoDTO(String nombreSantuario,
                                             RecuperarMediumDTO medium,
                                             Integer cantidadDeDemonios,
                                             Integer cantidadDeDemoniosLibres) {
}
