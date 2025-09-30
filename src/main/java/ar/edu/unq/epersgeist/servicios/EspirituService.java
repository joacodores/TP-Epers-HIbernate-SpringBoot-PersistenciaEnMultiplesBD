package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.enums.Direccion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface EspirituService {
    Espiritu crear(Espiritu espiritu);

    Optional<Espiritu> recuperar(Long espirituId);

    List<Espiritu> recuperarTodos();
    
    //List<Espiritu> espiritusDemoniacos(Direccion direccion, Integer pagina, Integer cantidadPorPagina);

    void actualizar(Espiritu espiritu);


    Medium conectar(Long espirituId, Long mediumId);

    void eliminar(Espiritu espiritu);

    void eliminarTodo();
}