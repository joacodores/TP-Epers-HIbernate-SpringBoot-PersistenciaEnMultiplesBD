package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumService {
    Medium crear(Medium medium);

    Optional<Medium> recuperar(Long mediumId);

    List<Medium> recuperarTodos();

    void actualizar(Medium medium);

    void eliminar(Long mediumId);

    void eliminarTodo();

    Espiritu invocar(Long mediumId, Long espirituId);

    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);

    void descansar(Long mediumId);

    List<Espiritu> espiritus(Long mediumId);

    void mover(Long mediumId, Long ubicacionId);
}
