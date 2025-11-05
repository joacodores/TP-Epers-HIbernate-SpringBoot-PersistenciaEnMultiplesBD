package ar.edu.unq.epersgeist.servicios.impl;

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
        Ubicacion ubicacion = ubicacionRepository.recuperar(reporte.getUbicacionId()).orElseThrow(RuntimeException::new);
        var espiritusEnUbi = espirituRepository.espiritusEn(reporte.getUbicacionId());
        int totalDemonios = (int) espiritusEnUbi.stream().filter(e -> e instanceof EspirituDemoniaco).count();
        int demoniosLibres = (int) espiritusEnUbi.stream().filter(e -> e instanceof EspirituDemoniaco && e.getOwner() == null).count();
        Long ownerId = espiritusEnUbi.stream()
                .filter(e -> e instanceof EspirituDemoniaco && e.getOwner() != null)
                .map(e -> e.getOwner().getId())
                .collect(java.util.stream.Collectors.groupingBy(id -> id, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.<Long, Long>comparingByValue().thenComparingLong(java.util.Map.Entry::getKey))
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
        Medium medium = ownerId != null ? mediumRepository.recuperar(ownerId).orElse(null) : null;
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                totalDemonios,
                demoniosLibres
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
