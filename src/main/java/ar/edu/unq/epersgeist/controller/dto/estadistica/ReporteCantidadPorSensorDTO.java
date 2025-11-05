package ar.edu.unq.epersgeist.controller.dto.estadistica;

import org.springframework.data.mongodb.core.mapping.Field;

public record ReporteCantidadPorSensorDTO(
        @Field("sensor_id")
        String sensorId,
        Long cantidad
) {

}
