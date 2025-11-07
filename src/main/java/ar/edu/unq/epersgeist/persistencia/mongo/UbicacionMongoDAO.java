package ar.edu.unq.epersgeist.persistencia.mongo;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;

public interface UbicacionMongoDAO extends MongoRepository<UbicacionMongo, String>{

//    @Aggregation(pipeline = {
//            "{ $addFields: { polygon: { type: 'Polygon', coordinates: [ { $map: { input: '$coordenadas', as: 'c', in: [ '$$c.longitud', '$$c.latitud' ] } } ] } } }",
//            "{ $match: { polygon: { $geoIntersects: { $geometry: ?0 } } } }"
//    })

    @Aggregation(pipeline = {
            "{ $addFields: { ring: { $map: { input: '$coordenadas', as: 'c', in: [ '$$c.longitud', '$$c.latitud' ] } } } }",
            "{ $addFields: { closedRing: { $concatArrays: [ '$ring', [ { $arrayElemAt: [ '$ring', 0 ] } ] ] } } }",
            "{ $addFields: { polygon: { type: 'Polygon', coordinates: [ '$closedRing' ] } } }",
            "{ $match: { polygon: { $geoIntersects: { $geometry: ?0 } } } }"
    })
    List<UbicacionMongo> findUbicacionesQueContienen(Map<String, Object> puntoGeoJson);






    @Query("{ 'polygon': { $geoIntersects: { $geometry: ?0 } } }")
    List<UbicacionMongo> findIntersecting(GeoJsonPoint point);
}
