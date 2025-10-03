package ar.edu.unq.epersgeist.controller.dto.estadistica;

import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record ReporteSantuarioMasCorruptoDTO(String nombreSantuario,
                                             RecuperarMediumDTO medium,
                                             Integer cantidadDeDemonios,
                                             Integer cantidadDeDemoniosLibres) {

    public static ReporteSantuarioMasCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteSantuarioMasCorruptoDTO(
                reporte.nombreSantuario(),
                RecuperarMediumDTO.desdeModelo(reporte.mediumConMasDemonios()),
                reporte.cantDemoniosTotal(),
                reporte.cantDemoniosLibres()
        );
    }

}
