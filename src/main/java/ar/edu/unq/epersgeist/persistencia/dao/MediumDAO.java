package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediumDAO extends CrudRepository<Medium, Long> {
    /*Medium crear(Medium medium);

    Medium recuperar(Long mediumId);

    void actualizar(Medium medium);

    void eliminar(Medium medium);

    List<Medium> recuperarTodos();

    void eliminarTodo();*/

    @Query(
            "FROM Medium m where m.ubicacion.id = :ubicacionId and m.espiritus is empty "
    )
    List<Medium> mediumsSinEspiritusEn(@Param("ubicacionId") Long ubicacionId);
}
