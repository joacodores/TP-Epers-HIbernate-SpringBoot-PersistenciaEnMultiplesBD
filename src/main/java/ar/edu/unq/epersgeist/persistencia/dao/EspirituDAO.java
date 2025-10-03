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

    @Query("from EspirituDemoniaco e")
    Page<Espiritu> espiritusDemoniacos(Pageable pageable);

    @Query(value = """
            SELECT
              g.ubicacion_id AS ubicacionId,
              (
                SELECT e2.owner_id
                FROM espiritu e2
                WHERE e2.dtype = 'EspirituDemoniaco'
                  AND e2.ubicacion_id = g.ubicacion_id
                  AND e2.owner_id IS NOT NULL
                GROUP BY e2.owner_id
                ORDER BY COUNT(*) DESC, e2.owner_id
                LIMIT 1
              ) AS ownerId,
              (
                SELECT COUNT(*)
                FROM espiritu ed
                WHERE ed.dtype = 'EspirituDemoniaco'
                  AND ed.ubicacion_id = g.ubicacion_id
              ) AS totalDemonios,
              (
                SELECT COUNT(*)
                FROM espiritu ef
                WHERE ef.dtype = 'EspirituDemoniaco'
                  AND ef.ubicacion_id = g.ubicacion_id
                  AND ef.owner_id IS NULL
              ) AS demoniosLibres
            FROM (
              SELECT
                u.id AS ubicacion_id
              FROM ubicacion u
              LEFT JOIN espiritu e ON e.ubicacion_id = u.id
              WHERE u.tipo_ubicacion = 'SANTUARIO'
              GROUP BY u.id
              HAVING
                SUM(CASE WHEN e.dtype = 'EspirituDemoniaco' THEN 1 ELSE 0 END) >
                SUM(CASE WHEN e.dtype = 'EspirituAngelical' THEN 1 ELSE 0 END)
              ORDER BY
                (SUM(CASE WHEN e.dtype = 'EspirituDemoniaco' THEN 1 ELSE 0 END) -
                 SUM(CASE WHEN e.dtype = 'EspirituAngelical' THEN 1 ELSE 0 END)) DESC,
                u.id
              LIMIT 1
            ) g
            """, nativeQuery = true)
    ReporteSantuarioMasCorruptoProjection obtenerReporteSantuarioMasCorrupto();

}