package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends CrudRepository<Medium, Long> {

    @Query(
            "FROM Medium m where m.ubicacion.id = :ubicacionId and m.espiritus is empty "
    )
    List<Medium> mediumsSinEspiritusEn(@Param("ubicacionId") Long ubicacionId);
}
