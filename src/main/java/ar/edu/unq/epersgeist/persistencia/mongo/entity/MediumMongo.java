package ar.edu.unq.epersgeist.persistencia.mongo.entity;

import ar.edu.unq.epersgeist.modelo.Medium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mediums")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediumMongo {

    @Id
    private String id;

    private Long mediumId;

    private CoordenadaMongo coordenada;

    public MediumMongo(Medium medium) {
        this.mediumId = medium.getId();
        this.coordenada = new CoordenadaMongo(medium.getCoordenada());
    }
}