package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.MediumMongo;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MediumMongoDAO extends MongoRepository<MediumMongo, String> {
    List<MediumMongo> findByMediumId(Long mediumId);

    void deleteByMediumId(Long mediumId);
}
