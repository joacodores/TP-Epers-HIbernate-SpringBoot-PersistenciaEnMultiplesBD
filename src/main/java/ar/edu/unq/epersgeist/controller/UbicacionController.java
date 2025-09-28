package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.RecuperarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/ubicacion")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    public ResponseEntity<RecuperarUbicacionDTO> crearUbicacion(@RequestBody CrearUbicacionDTO ubicacionRequest) {
        var ubicacionCreada = ubicacionService.crear(ubicacionRequest.aModelo());
        var dto = RecuperarUbicacionDTO.desdeModelo(ubicacionCreada);
        URI location = URI.create("/ubicacion/" + dto.id());
        return ResponseEntity
                .created(location)
                .body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarUbicacionDTO> recuperarUbicacion(@PathVariable Long id) {
        Optional<Ubicacion> ubicacionRecuperada = ubicacionService.recuperar(id);
        if (ubicacionRecuperada.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var dto = RecuperarUbicacionDTO.desdeModelo(ubicacionRecuperada.get());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<RecuperarUbicacionDTO>> recuperarTodasLasUbicaciones() {
        var ubicacionesRecuperadas = ubicacionService.recuperarTodos();
        var dtos = ubicacionesRecuperadas.stream()
                                        .map(RecuperarUbicacionDTO::desdeModelo)
                                        .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<RecuperarEspirituDTO>> espiritusEnUbicacion(@PathVariable Long id) {
        var espiritusEnUbicacion =  ubicacionService.espiritusEn(id);
        var dtos = espiritusEnUbicacion.stream()
                                        .map(RecuperarEspirituDTO::desdeModelo)
                                        .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<RecuperarMediumDTO>> mediumsSinEspiritusEnUbicacion(@PathVariable Long id) {
        var mediumsSinEspiritusEnUbicacion = ubicacionService.mediumsSinEspiritusEn(id);
        var dtos = mediumsSinEspiritusEnUbicacion.stream()
                                                .map(RecuperarMediumDTO::desdeModelo)
                                                .toList();
        return ResponseEntity.ok(dtos);
    }

}
