package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.servicios.enums.Direccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends CrudRepository<Espiritu, Long> {
    /*Espiritu crear(Espiritu espiritu);



    List<Espiritu> recuperarTodos();

    void actualizar(Espiritu espiritu);

    void eliminar(Espiritu espiritu);

    void eliminarTodo();

    List<Espiritu> espiritusDemoniacos(Direccion direccion, Integer pagina, Integer cantidadPorPagina);
    */
    @Query(
            "FROM Espiritu e where e.ubicacion.id = :ubicacionId"
    )
    List<Espiritu> espiritusEn(@Param("ubicacionId") Long ubicacionId);


    @Query(
            "From Espiritu e where e.id = :idDelEspiritu"
    )
    Espiritu recuperar(Long idDelEspiritu);
}