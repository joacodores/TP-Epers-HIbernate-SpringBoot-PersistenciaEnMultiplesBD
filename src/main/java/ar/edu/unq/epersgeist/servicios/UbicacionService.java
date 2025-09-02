package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface UbicacionService {
    Ubicacion crear(Ubicacion ubicacion);
    Ubicacion recuperar(Long ubicacionId);
    void actualizar(Ubicacion ubicacion);
    void eliminar(Ubicacion ubicacion);
    List<Ubicacion> recuperarTodos();
    void eliminarTodo();
}
