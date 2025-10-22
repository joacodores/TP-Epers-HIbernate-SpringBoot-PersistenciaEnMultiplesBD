package ar.edu.unq.epersgeist.persistencia.neo;

import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UbicacionNeo4JDAO extends Neo4jRepository<UbicacionNeo4J, Long> {

    @Query("""
        MATCH (o:Ubicacion {id:$idOrigen})
        MATCH (d:Ubicacion {id:$idDestino})
        MERGE (o)-[r:ubicacionesConectadas]->(d)
        SET r.costo = $costo
    """)
    void conectar(Long idOrigen, Long idDestino, Integer costo);

    @Query("""
        MATCH (:Ubicacion {id:$idOrigen})-[:ubicacionesConectadas]->(:Ubicacion {id:$idDestino})
        RETURN count(*) > 0
    """)
    Boolean estanConectadasDirecto(Long idOrigen, Long idDestino);

//    @Query("""
//        MATCH (u:Ubicacion {id:$id})
//        OPTIONAL MATCH (u)-[r:ubicacionesConectadas]->(d:Ubicacion)
//        RETURN u, collect(r), collect(d)
//    """)
//    Optional<UbicacionNeo4J> findByIdWithConexiones(Long id);



}
