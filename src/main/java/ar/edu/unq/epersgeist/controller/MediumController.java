package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.CrearMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
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

    public MediumController(MediumService mediumService, UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
        this.mediumService = mediumService;
    }


    @PostMapping
    public ResponseEntity<RecuperarMediumDTO> crearMedium(@RequestBody CrearMediumDTO mediumDTO) {
        var ubicacion = ubicacionService.recuperar(mediumDTO.ubicacionId());
        if(ubicacion.isEmpty()){
            return ResponseEntity.badRequest().build(); //implementar mensaje "La ubicacion no es válida"
        }
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
        var mediumRecuperado =  mediumService.recuperar(id);
        if (mediumRecuperado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var dto = RecuperarMediumDTO.desdeModelo(mediumRecuperado.get());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sinEspiritusEn/{ubicacionId}")
    public ResponseEntity<List<RecuperarMediumDTO>> mediumsSinEspiritusEn(@PathVariable Long ubicacionId) {
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(ubicacionId);
        if (ubicacion.isEmpty()){return ResponseEntity.notFound().build();} //checkeo que la ubi exista porque el ubiService no maneja ese error
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(ubicacionId);
        var dtos = mediumsSinEspiritusEn.stream().map(RecuperarMediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecuperarMediumDTO> actualizarMedium(@PathVariable Long id,
                                                               @RequestBody ActualizarMediumDTO mediumDTO) {
        //checkeamos que exista
        var mediumAActualizar = mediumService.recuperar(id);
        if(mediumAActualizar.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        mediumService.actualizar(mediumDTO.aModelo(id));
        var mediumActualizado = mediumService.recuperar(id);
        if(mediumActualizado.isEmpty()){
           return ResponseEntity.internalServerError().build();
        }
        var dto = RecuperarMediumDTO.desdeModelo(mediumActualizado.get());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{mediumExorcistaId}/exorcizar/{mediumPoseidoId}")
    public ResponseEntity<RecuperarMediumDTO> exorcizar(@PathVariable Long mediumExorcistaId,
                                                        @PathVariable Long mediumPoseidoId) {
        var mediumExorcista = mediumService.recuperar(mediumExorcistaId);
        var mediumPoseido = mediumService.recuperar(mediumPoseidoId);
        if(mediumExorcista.isEmpty() || mediumPoseido.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        mediumService.exorcizar(mediumExorcistaId, mediumPoseidoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/descansar/{id}")
    public ResponseEntity<RecuperarMediumDTO> descansar(@PathVariable Long id) {
        var medium = mediumService.recuperar(id);
        if(medium.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        mediumService.descansar(medium.get().getId());
        return ResponseEntity.ok().build();
    }



}
