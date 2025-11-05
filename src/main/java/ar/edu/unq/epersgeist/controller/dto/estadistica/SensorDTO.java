package ar.edu.unq.epersgeist.controller.dto.estadistica;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;
import org.springframework.data.mongodb.core.mapping.Field;

public record SensorDTO(
        @Field("sensor_id")
        String sensorId,
        String tipo,
        Double valor,
        String unidad,
        String fecha) {

    public static SensorDTO desdeModelo(SensorNormalizado sensor) {
        return new SensorDTO(
                sensor.getSensorId(),
                sensor.getTipo(),
                sensor.getValor(),
                sensor.getUnidad(),
                sensor.getFecha()
        );
    }

}
