package ar.edu.unq.epersgeist.persistencia.neo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@RelationshipProperties
public class ConexionPsionicaNeo4J {

    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    @TargetNode
    private UbicacionNeo4J destino;

    @Getter
    @Setter
    private Integer costo;

    public ConexionPsionicaNeo4J() {
    }

    public ConexionPsionicaNeo4J(UbicacionNeo4J destino, Integer costo) {
        this.destino = destino;
        this.costo = Math.max(0, Math.min(100, costo));
    }

}
