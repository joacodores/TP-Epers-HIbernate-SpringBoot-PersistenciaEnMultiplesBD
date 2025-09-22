package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.CrearEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/espiritu")
public class EspirituController {

    @PostMapping
    public ResponseEntity<RecuperarEspirituDTO> crearEspiritu(@RequestBody CrearEspirituDTO espirituDTO) {
        // TODO: Implementar
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarEspirituDTO> recuperarEspiritu(@PathVariable("id") Long id) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarEspirituDTO> actualizarEspiritu(@PathVariable("id") Long id,
                                                                   @RequestBody ActualizarEspirituDTO espirituDTO) {
        // TODO: Implementar
        return null;
    }

    @GetMapping
    public ResponseEntity<List<RecuperarEspirituDTO>> recuperarTodosLosEspiritus() {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/demonios")
    public ResponseEntity<List<RecuperarEspirituDTO>> recuperarDemonios(@RequestParam Sort.Direction direccion,
                                                                        @RequestParam Integer pagina,
                                                                        @RequestParam Integer cantidadPorPagina) {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @PatchMapping("/{id}/conectar/{mediumId}")
    public ResponseEntity<RecuperarEspirituDTO> conectar(@PathVariable Long id,
                                                         @PathVariable Long mediumId) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mediumId}/espiritus")
    public ResponseEntity<List<RecuperarEspirituDTO>> espiritusConectadosA(@PathVariable Long mediumId) {
        // TODO: Implementar
        return ResponseEntity.ok(List.of());
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<RecuperarEspirituDTO> invocar(@PathVariable Long mediumId,
                                                        @PathVariable Long espirituId) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{mediumId}/mover/{ubicacionId}")
    public ResponseEntity<RecuperarEspirituDTO> mover(@PathVariable Long mediumId,
                                                      @PathVariable Long ubicacionId) {
        // TODO: Implementar
        return ResponseEntity.ok().build();
    }

}
