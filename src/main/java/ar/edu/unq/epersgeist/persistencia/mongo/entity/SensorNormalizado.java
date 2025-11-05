package ar.edu.unq.epersgeist.persistencia.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@AllArgsConstructor
@Document(collection = "epersgeist_normalized")
public class SensorNormalizado {

    @Id
    private String id;

    @Field("sensor_id")
    private String sensorId;
    private String tipo;
    private Double valor;
    private String unidad;
    private String fecha;

}
