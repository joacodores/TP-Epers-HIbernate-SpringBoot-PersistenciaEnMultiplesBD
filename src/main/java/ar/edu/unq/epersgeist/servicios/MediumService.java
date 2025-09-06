package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface MediumService {
    Medium crear(Medium medium);
    Medium recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void eliminar(Medium medium);
    void eliminarTodo();
}
