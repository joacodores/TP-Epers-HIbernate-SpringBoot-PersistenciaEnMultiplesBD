package ar.edu.unq.epersgeist.persistencia.sql;

import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituSQLDAO extends CrudRepository<EspirituSQL, Long> {

    @Query(
            "from EspirituSQL e where e.ubicacion.id = :ubicacionId"
    )
    List<EspirituSQL> espiritusEn(@Param("ubicacionId") Long ubicacionId);

    @Query(
            "from EspirituSQL e where e.id = :idDelEspiritu"
    )
    EspirituSQL recuperar(Long idDelEspiritu);

    @Query("from EspirituDemoniacoSQL e")
    Page<EspirituSQL> espiritusDemoniacos(Pageable pageable);

    @Query(value = """
            SELECT
              u.id AS ubicacionId,
              (
                SELECT e2.owner_id
                FROM espiritu e2
                WHERE e2.dtype = 'EspirituDemoniaco'
                  AND e2.ubicacion_id = u.id
                  AND e2.owner_id IS NOT NULL
                GROUP BY e2.owner_id
                ORDER BY COUNT(*) DESC, e2.owner_id
                LIMIT 1
              ) AS ownerId,
              (
                SELECT COUNT(*)
                FROM espiritu ed
                WHERE ed.dtype = 'EspirituDemoniaco'
                  AND ed.ubicacion_id = u.id
              ) AS totalDemonios,
              (
                SELECT COUNT(*)
                FROM espiritu ef
                WHERE ef.dtype = 'EspirituDemoniaco'
                  AND ef.ubicacion_id = u.id
                  AND ef.owner_id IS NULL
              ) AS demoniosLibres
            FROM ubicacion u
            WHERE u.tipo_ubicacion = 'SANTUARIO'
              AND (
                (SELECT COUNT(*) FROM espiritu ed WHERE ed.dtype = 'EspirituDemoniaco' AND ed.ubicacion_id = u.id) -
                (SELECT COUNT(*) FROM espiritu ea WHERE ea.dtype = 'EspirituAngelical' AND ea.ubicacion_id = u.id)
              ) > 0
            ORDER BY (
                (SELECT COUNT(*) FROM espiritu ed WHERE ed.dtype = 'EspirituDemoniaco' AND ed.ubicacion_id = u.id) -
                (SELECT COUNT(*) FROM espiritu ea WHERE ea.dtype = 'EspirituAngelical' AND ea.ubicacion_id = u.id)
            ) DESC, u.id
            LIMIT 1
            """, nativeQuery = true)
    List<ReporteSantuarioMasCorruptoProjection> obtenerReporteSantuarioMasCorrupto();

}