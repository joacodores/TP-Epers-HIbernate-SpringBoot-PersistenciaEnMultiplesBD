package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;

import java.util.List;

public interface SensorRepository {

    void normalizeData();

    List<ReportePromedioPorTipoDTO> obtenerPromediosPorTipo();

    List<SensorNormalizado> obtenerMedicionesAnomalas(String tipo, Double min, Double max);

    List<ReporteCantidadPorTipoDTO> obtenerCantidadPorTipo();

    List<ReporteCantidadPorSensorDTO> obtenerCantidadPorSensor();

    List<ReportePromedioPorSensorDTO> obtenerPromedioPorSensor();

    List<ReportePromedioDiarioPorTipoDTO> obtenerPromedioDiarioPorTipo();

    List<ReportePromedioDiarioPorSensorDTO> obtenerPromedioDiarioPorSensor();

    List<ReporteVariabilidadSensorDTO> obtenerLosDiezSensoresMasVariables();

}
