package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.estadistica.*;
import ar.edu.unq.epersgeist.controller.exceptions.TipoNuloException;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/estadistica")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/santuarioCorrupto")
    public ResponseEntity<ReporteSantuarioMasCorruptoDTO> estadisticaSantuarios() {
        var reporte = estadisticaService.santuarioCorrupto();
        return ResponseEntity.ok(ReporteSantuarioMasCorruptoDTO.desdeModelo(reporte));
    }

    @PostMapping("/sensores/normalizar")
    public ResponseEntity<Map<String, Object>> normalizar() {
        estadisticaService.normalizeSensorData();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Normalización completada correctamente.");
        response.put("sourceCollection", "epersgeist_unnormalized");
        response.put("targetCollection", "epersgeist_normalized");
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sensores/cantidadPorTipo")
    public ResponseEntity<List<ReporteCantidadPorTipoDTO>> cantidadMedicionesPorTipoDeSensor() {
        var reportes = estadisticaService.obtenerCantidadPorTipoDeSensor();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/cantidadPorSensor")
    public ResponseEntity<List<ReporteCantidadPorSensorDTO>> cantidadMedicionesPorSensor() {
        var reportes = estadisticaService.obtenerCantidadPorSensor();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/promedioPorTipo")
    public ResponseEntity<List<ReportePromedioPorTipoDTO>> promedioPorTipoDeSensor() {
        var reportes = estadisticaService.obtenerValoresPromedioPorTipoDeSensor();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/promedioPorSensor")
    public ResponseEntity<List<ReportePromedioPorSensorDTO>> promedioPorSensor() {
        var reportes = estadisticaService.obtenerPromedioPorSensor();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/promedioDiarioPorTipo")
    public ResponseEntity<List<ReportePromedioDiarioPorTipoDTO>> promedioDiarioPorTipo() {
        var reportes = estadisticaService.obtenerPromedioDiarioPorTipo();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/medicionesAnomalas")
    public ResponseEntity<List<SensorDTO>> medicionesAnomalas(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max
    ) {
        if (tipo == null || tipo.isEmpty()) throw new TipoNuloException("Debe informar tipo de sensor");
        var reportes = estadisticaService.obtenerMedicionesAnomalas(tipo, min, max);
        var dtos = reportes.stream().map(SensorDTO::desdeModelo).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/sensores/promedioDiarioPorSensor")
    public ResponseEntity<List<ReportePromedioDiarioPorSensorDTO>> promedioDiarioPorSensor() {
        var reportes = estadisticaService.obtenerPromedioDiarioPorSensor();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/sensores/variabilidad")
    public ResponseEntity<List<ReporteVariabilidadSensorDTO>> sensoresMasVariables() {
        var reportes = estadisticaService.obtenerLosDiezSensoresMasVariables();
        return ResponseEntity.ok(reportes);
    }

}
