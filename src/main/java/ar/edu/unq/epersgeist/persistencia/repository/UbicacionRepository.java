package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {

    Ubicacion crear(Ubicacion ubicacion);

    Optional<Ubicacion> recuperar(Long ubicacionId);

    void actualizar(Ubicacion ubicacion);

    void eliminar(Long ubicacionId);

    List<Ubicacion> recuperarTodos();
    //Optional<Ubicacion> recuperarConConexiones(Long ubicacionId);

    void conectar(Long idOrigen, Long idDestino, Long costo);

    Boolean estanConectadas(Long idOrigen, Long idDestino);

    void eliminarTodo();

    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);

    List<Ubicacion> caminoMasRentable(Long idOrigen, Long idDestino);

    List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia);
}
