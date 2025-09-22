package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/ubicacion")
public class UbicacionController {

    @PostMapping
    public ResponseEntity<RecuperarUbicacionDTO> crearUbicacion(@RequestBody CrearUbicacionDTO ubicacionRequest) {
        // TODO: Implementar
        return ResponseEntity.status(201).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarUbicacionDTO> recuperarUbicacion(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RecuperarUbicacionDTO>> recuperarTodasLasUbicaciones() {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<RecuperarEspirituDTO>> espiritusEnUbicacion(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<RecuperarMediumDTO>> mediumsSinEspiritusEnUbicacion(@PathVariable Long id) {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

}
