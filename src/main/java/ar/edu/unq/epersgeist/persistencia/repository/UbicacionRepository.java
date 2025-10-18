package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    Ubicacion crear(Ubicacion ubicacion);

    Optional<Ubicacion> recuperar(Long ubicacionId);

    void actualizar(Ubicacion ubicacion);

    void eliminar(Long ubicacionId);

    List<Ubicacion> recuperarTodos();

    void eliminarTodo();
}
