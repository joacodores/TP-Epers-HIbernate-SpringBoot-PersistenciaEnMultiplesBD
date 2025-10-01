package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioMasCorruptoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/estadistica")
public class EstadisticaController {
    @GetMapping("/santuarioCorrupto")
    public ResponseEntity<ReporteSantuarioMasCorruptoDTO> estadisticaSantuarios() {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }
}
