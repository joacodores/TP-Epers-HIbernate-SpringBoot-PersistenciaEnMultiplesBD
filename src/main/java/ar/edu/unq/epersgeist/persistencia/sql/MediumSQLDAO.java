package ar.edu.unq.epersgeist.persistencia.sql;

import ar.edu.unq.epersgeist.persistencia.sql.entity.MediumSQL;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumSQLDAO extends CrudRepository<MediumSQL, Long> {

    @Query(
            "from MediumSQL m where m.ubicacion.id = :ubicacionId and m.espiritus is empty "
    )
    List<MediumSQL> mediumsSinEspiritusEn(@Param("ubicacionId") Long ubicacionId);

    @Query(
            "from MediumSQL m where m.id = :mediumId"
    )
    MediumSQL recuperar(Long mediumId);

}
