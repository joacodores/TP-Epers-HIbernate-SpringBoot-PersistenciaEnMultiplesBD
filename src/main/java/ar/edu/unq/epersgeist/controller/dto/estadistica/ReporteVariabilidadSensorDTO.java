package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReporteVariabilidadSensorDTO(
        @Field("sensor_id")
        String sensorId,
        String tipo,
        Double desviacion,
        Double promedio,
        Long total
) {

}
