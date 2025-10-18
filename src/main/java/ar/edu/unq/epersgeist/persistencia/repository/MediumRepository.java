package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    Medium crear(Medium medium);

    Optional<Medium> recuperar(Long mediumId);

    List<Medium> recuperarTodos();

    void actualizar(Medium medium);

    void eliminar(Long mediumId);

    void eliminarTodo();

    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
}
