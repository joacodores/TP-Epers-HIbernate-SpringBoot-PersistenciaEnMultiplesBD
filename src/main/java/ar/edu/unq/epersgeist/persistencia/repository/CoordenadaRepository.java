package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Coordenada;

import java.util.List;

public interface CoordenadaRepository {
    Coordenada crear(Coordenada coordenada);

    Coordenada recuperar(Long coordenadaId);

    List<Coordenada> recuperarTodos();

    void eliminar(Long coordenadaId);

    void eliminarTodo();
}
