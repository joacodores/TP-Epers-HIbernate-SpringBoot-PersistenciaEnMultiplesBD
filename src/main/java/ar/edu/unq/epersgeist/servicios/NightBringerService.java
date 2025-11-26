package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.NightBringer;

import java.util.List;

public interface NightBringerService {

    NightBringer crear(NightBringer nightBringer);
    NightBringer recuperar(Long nightBringerId);
    List<NightBringer> recuperarTodos();
    void actualizar(NightBringer nightBringer);
    Espiritu spawnearEspirituEnUbicacion(Long nightbringerId, Long ubicacionId, String nombreEspiritu);
    void eliminar(Long nightbringerId);
    void eliminarTodo();
}
