package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.SensorNormalizado;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.SensorRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;
    private final UbicacionRepository ubicacionRepository;
    private final SensorRepository sensorRepository;

    public EstadisticaServiceImpl(EspirituRepository espirituRepository, MediumRepository mediumRepository, UbicacionRepository ubicacionRepository, SensorRepository sensorRepository) {
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        List<ReporteSantuarioMasCorruptoProjection> data = espirituRepository.obtenerReporteSantuarioMasCorrupto();
        if (data.isEmpty())
            throw new NoHaySantuarioCorruptoException("No existe ningún santuario que tenga más espíritus demoníacos que angelicales");
        ReporteSantuarioMasCorruptoProjection reporte = data.getFirst();
        Medium medium = reporte.getOwnerId() != null ? mediumRepository.recuperar(reporte.getOwnerId()).orElse(null) : null;
        Ubicacion ubicacion = ubicacionRepository.recuperar(reporte.getUbicacionId())
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                reporte.getTotalDemonios(),
                reporte.getDemoniosLibres()
        );
    }

    @Override
    public void normalizeSensorData() {
        sensorRepository.normalizeData();
    }

    @Override
    public List<ReportePromedioPorTipoDTO> obtenerValoresPromedioPorTipoDeSensor() {
        return sensorRepository.obtenerPromediosPorTipo();
    }

    @Override
    public List<SensorNormalizado> obtenerMedicionesAnomalas(String tipo, Double min, Double max) {
        if (min == null) min = Double.NEGATIVE_INFINITY;
        if (max == null) max = Double.POSITIVE_INFINITY;
        return sensorRepository.obtenerMedicionesAnomalas(tipo, min, max);
    }

    @Override
    public List<ReporteCantidadPorTipoDTO> obtenerCantidadPorTipoDeSensor() {
        return sensorRepository.obtenerCantidadPorTipo();
    }

    @Override
    public List<ReportePromedioPorSensorDTO> obtenerPromedioPorSensor() {
        return sensorRepository.obtenerPromedioPorSensor();
    }

    @Override
    public List<ReportePromedioDiarioPorTipoDTO> obtenerPromedioDiarioPorTipo() {
        return sensorRepository.obtenerPromedioDiarioPorTipo();
    }

    @Override
    public List<ReportePromedioDiarioPorSensorDTO> obtenerPromedioDiarioPorSensor() {
        return sensorRepository.obtenerPromedioDiarioPorSensor();
    }

    @Override
    public List<ReporteVariabilidadSensorDTO> obtenerLosDiezSensoresMasVariables() {
        return sensorRepository.obtenerLosDiezSensoresMasVariables();
    }

    @Override
    public List<ReporteCantidadPorSensorDTO> obtenerCantidadPorSensor() {
        return sensorRepository.obtenerCantidadPorSensor();
    }

}
