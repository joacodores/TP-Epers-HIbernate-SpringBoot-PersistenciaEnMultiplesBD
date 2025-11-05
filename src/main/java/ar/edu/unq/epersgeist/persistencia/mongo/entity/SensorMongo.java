package ar.edu.unq.epersgeist.persistencia.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "epersgeist_unnormalized")
public class SensorMongo {

    @Id
    private String id;

}
