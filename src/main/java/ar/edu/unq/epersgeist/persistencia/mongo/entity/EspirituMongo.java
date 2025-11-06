package ar.edu.unq.epersgeist.persistencia.mongo.entity;
import ar.edu.unq.epersgeist.modelo.Espiritu;
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
public class EspirituMongo {

    @Id
    private String id;

    private Long espirituId;

    private CoordenadaMongo coordenada;

    public EspirituMongo(Espiritu espiritu) {
        this.espirituId = espiritu.getId();
        this.coordenada = new CoordenadaMongo(espiritu.getCoordenada());
    }
}

