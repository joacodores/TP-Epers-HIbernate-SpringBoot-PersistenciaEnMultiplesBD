package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.CrearMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/medium")
public class MediumController {

    @PostMapping
    public ResponseEntity<RecuperarMediumDTO> crearMedium(@RequestBody CrearMediumDTO mediumDTO) {
        // TODO: Implementar
        return ResponseEntity.status(201).body(null);
    }

    @GetMapping
    public ResponseEntity<List<RecuperarMediumDTO>> recuperarTodosLosMedium() {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarMediumDTO> recuperarMedium(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarMediumDTO> actualizarMedium(@PathVariable Long id,
                                                               @RequestBody ActualizarMediumDTO mediumDTO) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{mediumExorcistaId}/exorcisar/{mediumPoseidoId}")
    public ResponseEntity<RecuperarMediumDTO> exorcizar(@PathVariable Long mediumExorcistaId,
                                                        @PathVariable Long mediumPoseidoId) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/descansar/{id}")
    public ResponseEntity<RecuperarMediumDTO> descansar(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

}
