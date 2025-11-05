package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReportePromedioPorTipoDTO(
        String tipo,
        @Field("promedio_valor")
        Double promedioValor,
        Double minimo,
        Double maximo,
        String unidad,
        @Field("total_mediciones")
        Long totalMediciones) {

}
