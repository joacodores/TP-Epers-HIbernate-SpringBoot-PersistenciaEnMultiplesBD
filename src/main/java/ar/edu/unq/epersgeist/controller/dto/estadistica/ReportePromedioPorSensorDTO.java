package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReportePromedioPorSensorDTO(
        @Field("sensor_id")
        String sensorId,
        String tipo,
        Double promedio,
        Double maximo,
        Double minimo,
        @Field("total_mediciones")
        Long totalMediciones
) {

}
