package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorMongo;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDAO extends MongoRepository<SensorMongo, String> {

    @Aggregation(pipeline = {
            "{ $match: { tipo: { $in: ['sonido', 'proximidad', 'temperatura', 'presion'] } } }",
            "{ $set: { " +
                    "  valor: { $switch: { " +
                    "    branches: [" +
                    // ====== SONIDO ======
                    "      { case: { $eq: ['$tipo','sonido'] }, then: { $switch: { " +
                    "          branches: [" +
                    "            { case: { $eq: ['$unidad','sones'] }, then: { $add: [28, { $multiply: [33.2, { $log10: '$valor' }] }] } }" +
                    "          ], default: '$valor' } } }," +
                    // ====== PROXIMIDAD ======
                    "      { case: { $eq: ['$tipo','proximidad'] }, then: { $switch: { " +
                    "          branches: [" +
                    "            { case: { $eq: ['$unidad','kilometros'] }, then: { $multiply: ['$valor', 1000] } }," +
                    "            { case: { $eq: ['$unidad','millas'] }, then: { $multiply: ['$valor', 1609.34] } }," +
                    "            { case: { $eq: ['$unidad','pies'] }, then: { $multiply: ['$valor', 0.3048] } }" +
                    "          ], default: '$valor' } } }," +
                    // ====== TEMPERATURA ======
                    "      { case: { $eq: ['$tipo','temperatura'] }, then: { $switch: { " +
                    "          branches: [" +
                    "            { case: { $eq: ['$unidad','K'] }, then: { $subtract: ['$valor', 273.15] } }," +
                    "            { case: { $eq: ['$unidad','F'] }, then: { $multiply: [ { $subtract: ['$valor', 32] }, { $divide: [5,9] } ] } }" +
                    "          ], default: '$valor' } } }," +
                    // ====== PRESIÓN ======
                    "      { case: { $eq: ['$tipo','presion'] }, then: { $switch: { " +
                    "          branches: [" +
                    "            { case: { $eq: ['$unidad','atm'] }, then: { $multiply: ['$valor', 1013.25] } }," +
                    "            { case: { $eq: ['$unidad','mmHg'] }, then: { $multiply: ['$valor', 1.33322] } }" +
                    "          ], default: '$valor' } } }" +
                    "    ], default: '$valor' } }," +
                    // cambio unidades
                    "  unidad: { $switch: { " +
                    "    branches: [" +
                    "      { case: { $eq: ['$tipo','sonido'] }, then: 'dB' }," +
                    "      { case: { $eq: ['$tipo','proximidad'] }, then: 'metros' }," +
                    "      { case: { $eq: ['$tipo','temperatura'] }, then: 'C' }," +
                    "      { case: { $eq: ['$tipo','presion'] }, then: 'hPa' }" +
                    "    ], default: '$unidad' } }" +
                    "} }",
            // Guardo en epersgeist_normalized
            "{ $merge: { " +
                    "    into: 'epersgeist_normalized', " +
                    "    whenMatched: 'replace', " +
                    "    whenNotMatched: 'insert' " +
                    "} }"
    })
    void normalizeData();

}
