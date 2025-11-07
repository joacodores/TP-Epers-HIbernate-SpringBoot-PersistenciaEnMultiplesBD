package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspirituService {

    Espiritu crear(Espiritu espiritu);

    Optional<Espiritu> recuperar(Long espirituId);

    List<Espiritu> recuperarTodos();

    List<Espiritu> espiritusDemoniacos(Sort.Direction direccion, Integer pagina, Integer cantidadPorPagina);

    void actualizar(Espiritu espiritu);

    Medium conectar(Long espirituId, Long mediumId);

    void eliminar(Long espirituId);

    void eliminarTodo();

    void dominar(Long espirituDominanteId, Long espirituADominarId);

}