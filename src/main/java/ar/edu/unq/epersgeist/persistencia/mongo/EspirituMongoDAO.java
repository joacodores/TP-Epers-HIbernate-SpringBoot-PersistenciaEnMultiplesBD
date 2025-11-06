package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.EspirituMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EspirituMongoDAO extends MongoRepository<EspirituMongo, String> {
    Optional<EspirituMongo> findByEspirituId(Long espirituId);
}
