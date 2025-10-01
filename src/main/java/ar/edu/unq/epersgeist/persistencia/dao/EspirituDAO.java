package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends CrudRepository<Espiritu, Long> {

    @Query(
            "from Espiritu e where e.ubicacion.id = :ubicacionId"
    )
    List<Espiritu> espiritusEn(@Param("ubicacionId") Long ubicacionId);


    @Query(
            "from Espiritu e where e.id = :idDelEspiritu"
    )
    Espiritu recuperar(Long idDelEspiritu);

    @Query("from Espiritu e")
    Page<Espiritu> espiritusDemoniacos(Pageable pageable);
}