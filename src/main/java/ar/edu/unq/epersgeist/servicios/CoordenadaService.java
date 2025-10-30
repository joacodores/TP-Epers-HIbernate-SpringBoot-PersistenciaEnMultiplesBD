package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Coordenada;

import java.util.List;

public interface CoordenadaService {
    Coordenada crear(Coordenada coordenada);

    Coordenada recuperar(Long coordenadaId);

    List<Coordenada> recuperarTodos();

    void eliminar(Long coordenadaId);

    void eliminarTodo();

    double distanciaEnKm(Long idCoordA, Long idCoordB);

}
