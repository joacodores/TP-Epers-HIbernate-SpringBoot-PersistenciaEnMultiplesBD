package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    Ubicacion crear(Ubicacion ubicacion);

    Optional<Ubicacion> recuperar(Long ubicacionId);

    void actualizar(Ubicacion ubicacion);

    void eliminar(Ubicacion ubicacion);

    List<Ubicacion> recuperarTodos();

    void eliminarTodo();

    List<Espiritu> espiritusEn(Long ubicacionId);

    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);

}
