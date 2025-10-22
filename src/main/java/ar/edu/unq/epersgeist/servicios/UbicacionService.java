package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    Ubicacion crear(Ubicacion ubicacion);

    Optional<Ubicacion> recuperar(Long ubicacionId);

    void actualizar(Ubicacion ubicacion);

    void eliminar(Long ubicacionId);

    List<Ubicacion> recuperarTodos();

    void eliminarTodo();

    List<Espiritu> espiritusEn(Long ubicacionId);

    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);

    void conectar(Long idOrigen, Long idDestino, Long costo);

    public Boolean estanConectadas(Long idOrigen, Long idDestino);
}
