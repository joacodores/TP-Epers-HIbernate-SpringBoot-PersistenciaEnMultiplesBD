package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.CrearMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import ar.edu.unq.epersgeist.controller.exceptions.ActualizarRecursoException;
import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoValidaException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/medium")
public class MediumController {

    private final MediumService mediumService;
    private final UbicacionService ubicacionService;
    private final EspirituService espirituService;

    public MediumController(MediumService mediumService, UbicacionService ubicacionService, EspirituService espirituService) {
        this.ubicacionService = ubicacionService;
        this.mediumService = mediumService;
        this.espirituService = espirituService;
    }

    @PostMapping
    public ResponseEntity<RecuperarMediumDTO> crearMedium(@RequestBody CrearMediumDTO mediumDTO) {
        var ubicacion = ubicacionService.recuperar(mediumDTO.ubicacionId());
        if (ubicacion.isEmpty()) throw new UbicacionNoValidaException("");
        var mediumCreado = mediumService.crear(mediumDTO.aModelo(ubicacion.get()));
        var dto = RecuperarMediumDTO.desdeModelo(mediumCreado);
        URI location = URI.create("/medium/" + mediumCreado.getId());
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<RecuperarMediumDTO>> recuperarTodosLosMedium() {
        List<Medium> mediumsRecuperados = mediumService.recuperarTodos();
        var dtos = mediumsRecuperados.stream().map(RecuperarMediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarMediumDTO> recuperarMedium(@PathVariable Long id) {
        var mediumRecuperado = mediumService.recuperar(id);
        if (mediumRecuperado.isEmpty()) throw new MediumNoEncontradoException("");
        var dto = RecuperarMediumDTO.desdeModelo(mediumRecuperado.get());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarMediumDTO> actualizarMedium(@PathVariable Long id,
                                                               @RequestBody ActualizarMediumDTO mediumDTO) {
        var mediumAActualizar = mediumService.recuperar(id);
        if (mediumAActualizar.isEmpty()) throw new MediumNoEncontradoException("");
        var medium = mediumAActualizar.get();
        mediumDTO.actualizarMedium(medium, id);
        mediumService.actualizar(medium);
        var mediumActualizado = mediumService.recuperar(id).orElseThrow(() -> new ActualizarRecursoException("del medium"));
        var dto = RecuperarMediumDTO.desdeModelo(mediumActualizado);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedium(@PathVariable Long id) {
        var mediumRecuperado = mediumService.recuperar(id);
        if (mediumRecuperado.isEmpty()) throw new MediumNoEncontradoException("");
        mediumService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodosLosMediums() {
        mediumService.eliminarTodo();
        return ResponseEntity.noContent().build(); // 204 incluso si ya estaba vacío
    }

    @PatchMapping("/{mediumExorcistaId}/exorcizar/{mediumPoseidoId}")
    public ResponseEntity<RecuperarMediumDTO> exorcizar(@PathVariable Long mediumExorcistaId,
                                                        @PathVariable Long mediumPoseidoId) {
        mediumService.exorcizar(mediumExorcistaId, mediumPoseidoId);
        var exorcistaActualizado = mediumService.recuperar(mediumExorcistaId)
                .orElseThrow(() -> new ActualizarRecursoException("del exorcista"));
        return ResponseEntity.ok(
                RecuperarMediumDTO.desdeModelo(exorcistaActualizado)
        );
    }

    @PatchMapping("/descansar/{id}")
    public ResponseEntity<RecuperarMediumDTO> descansar(@PathVariable Long id) {
        mediumService.descansar(id);
        var mediumActualizado = mediumService.recuperar(id)
                .orElseThrow(() -> new ActualizarRecursoException("del medium"));
        return ResponseEntity.ok(
                RecuperarMediumDTO.desdeModelo(mediumActualizado)
        );
    }

    @PatchMapping("/{mediumId}/invocar/{espirituId}")
    public ResponseEntity<RecuperarEspirituDTO> invocar(@PathVariable Long mediumId,
                                                        @PathVariable Long espirituId) {
        mediumService.invocar(mediumId, espirituId);
        var espirituActualizado = espirituService.recuperar(espirituId)
                .orElseThrow(() -> new ActualizarRecursoException("del espiritu"));
        return ResponseEntity.ok(
                RecuperarEspirituDTO.desdeModelo(espirituActualizado)
        );
    }

    @GetMapping("/sinEspiritusEn/{ubicacionId}")
    public ResponseEntity<List<RecuperarMediumDTO>> mediumsSinEspiritusEn(@PathVariable Long ubicacionId) {
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(ubicacionId);
        if (ubicacion.isEmpty()) throw new UbicacionNoEncontradaException("");
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(ubicacionId);
        var dtos = mediumsSinEspiritusEn.stream().map(RecuperarMediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{mediumId}/mover/{latitud}/{longitud}")
    public ResponseEntity<RecuperarMediumDTO> mover(@PathVariable Long mediumId,
                                                    @PathVariable Double latitud,
                                                    @PathVariable Double longitud) {
        mediumService.mover(mediumId, latitud, longitud);
        var mediumActualizado = mediumService.recuperar(mediumId)
                .orElseThrow(() -> new ActualizarRecursoException("del medium"));
        var dto = RecuperarMediumDTO.desdeModelo(mediumActualizado);
        return ResponseEntity.ok(dto);
    }

}
