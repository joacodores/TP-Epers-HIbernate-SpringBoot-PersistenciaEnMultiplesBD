package ar.edu.unq.epersgeist.persistencia.sql;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionSQLDAO extends CrudRepository<UbicacionSQL, Long> {

}
