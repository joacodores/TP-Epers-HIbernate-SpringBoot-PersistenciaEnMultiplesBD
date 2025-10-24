package ar.edu.unq.epersgeist.persistencia.sql;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionSQLDAO extends CrudRepository<UbicacionSQL, Long> {

    @Query(
            value="FROM UbicacionSQL where energia < :umbralDeEnergia"
    )
    List<UbicacionSQL> ubicacionesSobrecargadas(@Param("umbralDeEnergia") Integer umbralDeEnergia);
}
