package ar.edu.unq.epersgeist.persistencia.mongo;

import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteSensoresDAO extends MongoRepository<SensorNormalizado, String> {

    @Aggregation(pipeline = {
            "{ $group: { _id: '$tipo', promedio_valor: { $avg: '$valor' }, minimo:  { $min: '$valor' }, maximo:  { $max:  '$valor' }, unidad: { $first: '$unidad' }, total_mediciones: { $sum: 1 } } }",
            "{ $project: { _id: 0, tipo: '$_id', promedio_valor: { $round: ['$promedio_valor', 2] }, minimo: { $round: ['$minimo', 2] }, maximo: { $round: ['$maximo', 2] }, unidad: 1, total_mediciones: 1 } }"
    })
    List<ReportePromedioPorTipoDTO> obtenerPromedioPorTipo();

    @Aggregation(pipeline = {
            "{ $match: { $and: [ { tipo: ?0 }, { valor: { $gte: ?1, $lte: ?2 } } ] } }",
            "{ $project: { _id: 0, sensor_id: 1, tipo: 1, valor: 1, unidad: 1, fecha: 1 } }"
    })
    List<SensorNormalizado> obtenerMedicionesAnomalas(String tipo, Double min, Double max);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$tipo', cantidad: { $sum: 1 } } }",
            "{ $project: { _id: 0, tipo: '$_id', cantidad: 1 } }"
    })
    List<ReporteCantidadPorTipoDTO> obtenerCantidadPorTipo();

    @Aggregation(pipeline = {
            "{ $group: { _id: '$sensor_id', cantidad: { $sum: 1 } } }",
            "{ $project: { _id: 0, sensor_id: '$_id', cantidad: 1 } }"
    })
    List<ReporteCantidadPorSensorDTO> obtenerCantidadPorSensor();

    @Aggregation(pipeline = {
            "{ $group: { _id: '$sensor_id', promedio: { $avg: '$valor' }, maximo: { $max: '$valor'}, minimo:  { $min: '$valor' }, tipo: { $first: '$tipo' }, total_mediciones: { $sum: 1 } } }",
            "{ $project: { _id: 0, sensor_id: '$_id', tipo: 1, promedio: { $round: ['$promedio', 2] }, maximo: { $round: ['$maximo', 2] }, minimo: { $round: ['$minimo', 2] }, total_mediciones: 1 } }"
    })
    List<ReportePromedioPorSensorDTO> obtenerPromedioPorSensor();

    @Aggregation(pipeline = {
            "{ $group: { " +
                    "    _id: { " +
                    "        tipo: '$tipo', " +
                    "        dia: { $dateToString: { format: '%Y-%m-%d', date: { $toDate: '$fecha' } } } " +
                    "    }, " +
                    "    promedio: { $avg: '$valor' }, " +
                    "    maximo: { $max: '$valor' }, " +
                    "    minimo: { $min: '$valor' }, " +
                    "    unidad: { $first: '$unidad' }, " +
                    "    total_mediciones: { $sum: 1 } " +
                    "} }",
            "{ $project: { " +
                    "    _id: 0, " +
                    "    tipo: '$_id.tipo', " +
                    "    fecha: '$_id.dia', " +
                    "    promedio: { $round: ['$promedio', 2] }, " +
                    "    maximo: { $round: ['$maximo', 2] }, " +
                    "    minimo: { $round: ['$minimo', 2] }, " +
                    "    unidad: 1, " +
                    "    total_mediciones: 1 " +
                    "} }",
            "{ $sort: { fecha: 1 } }"
    })
    List<ReportePromedioDiarioPorTipoDTO> obtenerPromedioDiarioPorTipo();

    @Aggregation(pipeline = {
            "{ $group: { " +
                    "    _id: { " +
                    "        sensor_id: '$sensor_id', " +
                    "        fecha: { $dateToString: { format: '%Y-%m-%d', date: { $toDate: '$fecha' } } } " +
                    "    }, " +
                    "    promedio: { $avg: '$valor' }, " +
                    "    maximo: { $max: '$valor' }, " +
                    "    minimo: { $min: '$valor' }, " +
                    "    tipo: { $first: '$tipo' }, " +
                    "    unidad: { $first: '$unidad' }, " +
                    "    total_mediciones: { $sum: 1 } " +
                    "} }",
            "{ $project: { " +
                    "    _id: 0, " +
                    "    sensor_id: '$_id.sensor_id', " +
                    "    fecha: '$_id.fecha', " +
                    "    promedio: { $round: ['$promedio', 2] }, " +
                    "    maximo: { $round: ['$maximo', 2] }, " +
                    "    minimo: { $round: ['$minimo', 2] }, " +
                    "    tipo: 1, " +
                    "    unidad: 1, " +
                    "    total_mediciones: 1 " +
                    "} }",
            "{ $sort: { fecha: 1 } }"
    })
    List<ReportePromedioDiarioPorSensorDTO> obtenerPromedioDiarioPorSensor();

    @Aggregation(pipeline = {
            "{ $group: { " +
                    "   _id: '$sensor_id', " +
                    "   desviacion: { $stdDevPop: '$valor' }, " +
                    "   promedio: { $avg: '$valor' }, " +
                    "   tipo: { $first: '$tipo' }, " +
                    "   total: { $sum: 1 } " +
                    "} }",
            "{ $sort: { desviacion: -1 } }",
            "{ $limit: 10 }",
            "{ $project: { " +
                    "   _id: 0, " +
                    "   sensorId: '$_id', " +
                    "   tipo: 1, " +
                    "   desviacion: { $round: ['$desviacion', 2] }, " +
                    "   promedio: { $round: ['$promedio', 2] }, " +
                    "   total: 1 " +
                    "} }"
    })
    List<ReporteVariabilidadSensorDTO> obtenerLosDiezSensoresMasVariables();

}
