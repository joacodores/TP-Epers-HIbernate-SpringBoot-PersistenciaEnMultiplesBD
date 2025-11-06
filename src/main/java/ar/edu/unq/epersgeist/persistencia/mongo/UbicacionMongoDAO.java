package ar.edu.unq.epersgeist.persistencia.mongo;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UbicacionMongoDAO extends MongoRepository<UbicacionMongo, String>{
}
