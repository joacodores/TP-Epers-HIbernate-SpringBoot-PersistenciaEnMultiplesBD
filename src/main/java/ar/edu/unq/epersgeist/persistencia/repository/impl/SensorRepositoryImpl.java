package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.persistencia.mongo.ReporteSensoresDAO;
import ar.edu.unq.epersgeist.persistencia.mongo.SensorDAO;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;
import ar.edu.unq.epersgeist.persistencia.repository.SensorRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorRepositoryImpl implements SensorRepository {

    private final SensorDAO sensorDAO;
    private final ReporteSensoresDAO reporteSensoresDAO;

    public SensorRepositoryImpl(SensorDAO sensorDAO, ReporteSensoresDAO reporteSensoresDAO) {
        this.sensorDAO = sensorDAO;
        this.reporteSensoresDAO = reporteSensoresDAO;
    }

    @Override
    public void normalizeData() {
        sensorDAO.normalizeData();
    }

    @Override
    public List<ReportePromedioPorTipoDTO> obtenerPromediosPorTipo() {
        return reporteSensoresDAO.obtenerPromedioPorTipo();
    }

    @Override
    public List<SensorNormalizado> obtenerMedicionesAnomalas(String tipo, Double min, Double max) {
        return reporteSensoresDAO.obtenerMedicionesAnomalas(tipo, min, max);
    }

    @Override
    public List<ReporteCantidadPorTipoDTO> obtenerCantidadPorTipo() {
        return reporteSensoresDAO.obtenerCantidadPorTipo();
    }

    @Override
    public List<ReportePromedioPorSensorDTO> obtenerPromedioPorSensor() {
        return reporteSensoresDAO.obtenerPromedioPorSensor();
    }

    @Override
    public List<ReportePromedioDiarioPorTipoDTO> obtenerPromedioDiarioPorTipo() {
        return reporteSensoresDAO.obtenerPromedioDiarioPorTipo();
    }

    @Override
    public List<ReportePromedioDiarioPorSensorDTO> obtenerPromedioDiarioPorSensor() {
        return reporteSensoresDAO.obtenerPromedioDiarioPorSensor();
    }

    @Override
    public List<ReporteVariabilidadSensorDTO> obtenerLosDiezSensoresMasVariables() {
        return reporteSensoresDAO.obtenerLosDiezSensoresMasVariables();
    }

    @Override
    public List<ReporteCantidadPorSensorDTO> obtenerCantidadPorSensor() {
        return reporteSensoresDAO.obtenerCantidadPorSensor();
    }

}
