package ar.edu.unq.epersgeist.controller;



import ar.edu.unq.epersgeist.controller.dto.espiritu.RecuperarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.nightBringer.ActualizarNightBringerDTO;
import ar.edu.unq.epersgeist.controller.dto.nightBringer.CrearNightBringerDTO;
import ar.edu.unq.epersgeist.controller.dto.nightBringer.RecuperarNightBringerDTO;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.NightBringer;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.NightBringerService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/nightBringer")
public class NightBringerController {

    private final NightBringerService nightBringerService;
    private final UbicacionService ubicacionService;

    public NightBringerController(NightBringerService nightBringerService, UbicacionService ubicacionService) {
        this.nightBringerService = nightBringerService;
        this.ubicacionService =  ubicacionService;
    }

    @PostMapping
    public ResponseEntity<RecuperarNightBringerDTO> crearNightBringer(@RequestBody CrearNightBringerDTO nightBringerRequest) {
        var nightBringerCreado = nightBringerService.crear(nightBringerRequest.aModelo());
        var dto = RecuperarNightBringerDTO.desdeModelo(nightBringerCreado);
        URI location = URI.create("/nightBringer/" + dto.id());
        return ResponseEntity
                .created(location)
                .body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperarNightBringerDTO> recuperarNightBringer(@PathVariable Long id) {
        NightBringer nightBringerRecuperado = nightBringerService.recuperar(id);

        var dto = RecuperarNightBringerDTO.desdeModelo(nightBringerRecuperado);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<RecuperarNightBringerDTO>> recuperarTodosLosNightBringers() {
        var nightBringersRecuperados = nightBringerService.recuperarTodos();
        var dtos = nightBringersRecuperados.stream()
                .map(RecuperarNightBringerDTO::desdeModelo)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarNightBringerDTO> actualizarNightBringer(@PathVariable Long id,
                                                                     @RequestBody ActualizarNightBringerDTO nightBringerDTO) {
        var nightBringerAActualizar = nightBringerService.recuperar(id);

        nightBringerDTO.actualizarNightBringer(nightBringerAActualizar, id);
        nightBringerService.actualizar(nightBringerAActualizar);
        var nightBringerActualizado = nightBringerService.recuperar(id);
        var dto = RecuperarNightBringerDTO.desdeModelo(nightBringerActualizado);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNightBringer(@PathVariable Long id) {
        var nightBringerRecuperado = nightBringerService.recuperar(id);

        nightBringerService.eliminar(nightBringerRecuperado.getId());
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodosLosNightBringers() {
        nightBringerService.eliminarTodo();
        return ResponseEntity.noContent().build(); // 204 incluso si ya estaba vacío
    }

    @PatchMapping("/{nightBringerId}/spawnearEspirituEnUbicacion/{nombreEspiritu}/{ubicacionId}")
    public ResponseEntity<RecuperarEspirituDTO> spawnearEspirituEnUbicacion(@PathVariable Long nightBringerId,
                                                                            @PathVariable String nombreEspiritu,
                                                                            @PathVariable Long ubicacionId) {
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(ubicacionId);
        if (ubicacion.isEmpty()) throw new UbicacionNoEncontradaException("");

        Espiritu espiritu = nightBringerService.spawnearEspirituEnUbicacion(nightBringerId, ubicacionId, nombreEspiritu);
        var dto = RecuperarEspirituDTO.desdeModelo(espiritu);
        return ResponseEntity.ok(dto);
    }
}
