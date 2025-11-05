package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReportePromedioDiarioPorTipoDTO(
        String tipo,
        String fecha,
        Double promedio,
        Double maximo,
        Double minimo,
        String unidad,
        @Field("total_mediciones")
        Long totalMediciones
) {

}
