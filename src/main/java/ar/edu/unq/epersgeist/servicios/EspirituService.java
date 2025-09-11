package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.servicios.enums.Direccion;

import java.util.List;

public interface EspirituService {
    Espiritu crear(Espiritu espiritu);
    Espiritu recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void actualizar(Espiritu espiritu);
    void eliminar(Espiritu espiritu);
    void eliminarTodo();
    /*Medium conectar(Long espirituId, Medium medium);*/
    List<Espiritu> espiritusDemoniacos(Direccion direccion, Integer pagina, Integer cantidadPorPagina);
}

