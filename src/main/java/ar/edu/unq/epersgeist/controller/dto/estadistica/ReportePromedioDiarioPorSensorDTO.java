package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReportePromedioDiarioPorSensorDTO(
        @Field("sensor_id")
        String sensorId,
        String fecha,
        Double promedio,
        Double maximo,
        Double minimo,
        String tipo,
        String unidad,
        @Field("total_mediciones")
        Long totalMediciones
) {

}
