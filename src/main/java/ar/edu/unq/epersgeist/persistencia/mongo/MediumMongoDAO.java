package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.MediumMongo;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediumMongoDAO extends MongoRepository<MediumMongo, String> {
    Optional<MediumMongo> findByMediumId(Long mediumId);
}
