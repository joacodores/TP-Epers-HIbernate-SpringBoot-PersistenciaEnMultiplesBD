package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioMasCorruptoDTO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/estadistica")
public class EstadisticaController {

    private EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/santuarioCorrupto")
    public ResponseEntity<ReporteSantuarioMasCorruptoDTO> estadisticaSantuarios() {
        var reporte = estadisticaService.santuarioCorrupto();
        return ResponseEntity.ok(reporte);
    }

}
