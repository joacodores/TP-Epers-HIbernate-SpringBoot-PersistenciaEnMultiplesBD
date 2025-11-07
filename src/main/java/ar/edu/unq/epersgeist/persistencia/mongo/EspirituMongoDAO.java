package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.EspirituMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EspirituMongoDAO extends MongoRepository<EspirituMongo, String> {
    List<EspirituMongo> findByEspirituId(Long espirituId);

    void deleteByEspirituId(Long espirituId);
}
