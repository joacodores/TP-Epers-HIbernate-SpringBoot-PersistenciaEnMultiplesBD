package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface MediumDAO {
    Medium crear(Medium medium);
    Medium recuperar(Long mediumId);
    void actualizar(Medium medium);
    void eliminar(Medium medium);
    List<Medium> recuperarTodos();
    void eliminarTodo();
    void descansar(Long mediumId);
}
