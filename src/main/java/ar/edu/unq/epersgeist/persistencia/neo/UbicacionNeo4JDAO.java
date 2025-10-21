package ar.edu.unq.epersgeist.persistencia.neo;

import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UbicacionNeo4JDAO extends Neo4jRepository<UbicacionNeo4J, Long> {

    @Query("MATCH (u:Ubicacion {id: $id}) " +
            "OPTIONAL MATCH (u)-[:AMIGO]->(a:Ubicacion) " +
            "RETURN u, collect(a) as amigos")
    Optional<UbicacionNeo4J> findByIdConConectadas(@Param("id") Long id);


}
