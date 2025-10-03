package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.CrearEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/espiritu")
public class EspirituController {
    private final MediumService mediumService;
    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituController(EspirituService espirituService, UbicacionService ubicacionService, MediumService mediumService) {
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
        this.mediumService = mediumService;
    }

    @PostMapping
    public ResponseEntity<RecuperarEspirituDTO> crearEspiritu(@RequestBody CrearEspirituDTO espirituDTO) {
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(espirituDTO.ubicacionId());
        if (ubicacion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var espiritu = espirituService.crear(espirituDTO.aModelo(ubicacion.get()));
        var dto = RecuperarEspirituDTO.desdeModelo(espiritu);
        URI location = URI.create("/espiritu/" + espiritu.getId());
        return ResponseEntity
                .created(location)
                .body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarEspirituDTO> recuperarEspiritu(@PathVariable("id") Long id) {
        Optional<Espiritu> espirituOptional = espirituService.recuperar(id);
        if (espirituOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var dto = RecuperarEspirituDTO.desdeModelo(espirituOptional.get());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarEspirituDTO> actualizarEspiritu(@PathVariable("id") Long id,
                                                                   @RequestBody ActualizarEspirituDTO espirituDTO) {
        var espirituOptional = espirituService.recuperar(id);
        if (espirituOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var espiritu = espirituOptional.get();
        espiritu.setNombre(espirituDTO.nombre());
        espirituService.actualizar(espiritu);
        var dto = RecuperarEspirituDTO.desdeModelo(espiritu);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<RecuperarEspirituDTO>> recuperarTodosLosEspiritus() {
        var espiritusRecuperados = espirituService.recuperarTodos();
        var dtos = espiritusRecuperados.stream()
                .map(RecuperarEspirituDTO::desdeModelo)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/demonios")
    public ResponseEntity<List<RecuperarEspirituDTO>> recuperarDemonios(@RequestParam Sort.Direction direccion,
                                                                        @RequestParam Integer pagina,
                                                                        @RequestParam Integer cantidadPorPagina) {
        var demonios = espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina);
        var dtos = demonios.stream()
                .map(RecuperarEspirituDTO::desdeModelo)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/conectar/{mediumId}")
    public ResponseEntity<RecuperarEspirituDTO> conectar(@PathVariable Long id,
                                                         @PathVariable Long mediumId) {
        var espirituRecuperado = espirituService.recuperar(id);
        if (espirituRecuperado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        espirituService.conectar(id, mediumId);
        var espirituActualizado = espirituService.recuperar(id)
                .orElseThrow(NoSuchElementException::new);
        return ResponseEntity.ok(RecuperarEspirituDTO.desdeModelo(espirituActualizado));
    }

    @GetMapping("/{mediumId}/espiritus")
    public ResponseEntity<List<RecuperarEspirituDTO>> espiritusConectadosA(@PathVariable Long mediumId) {
        var mediumRecurepado = mediumService.recuperar(mediumId);
        if (mediumRecurepado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Espiritu> espiritus = mediumService.espiritus(mediumId);
        List<RecuperarEspirituDTO> espiritusRecuperados = espiritus.stream().map(RecuperarEspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok(espiritusRecuperados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspiritu(@PathVariable Long id) {
        var espirituRecuperado = espirituService.recuperar(id);
        if (espirituRecuperado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        espirituService.eliminar(espirituRecuperado.get().getId());
        return ResponseEntity.noContent().build(); // 204
    }
}
