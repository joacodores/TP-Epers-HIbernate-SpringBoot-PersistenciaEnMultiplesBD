package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioMasCorruptoDTO;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAO extends CrudRepository<Ubicacion, Long> {

    @Query("""
                SELECT new ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioMasCorruptoDTO(
                    u.nombre,
                    new ar.edu.unq.epersgeist.controller.dto.medium.RecuperarMediumDTO(
                        m.id, m.nombre, m.manaMax, m.mana, u.id, null, null
                    ),
                    SUM(CASE WHEN e.class = EspirituDemoniaco THEN 1 ELSE 0 END),
                    SUM(CASE WHEN e.class = EspirituDemoniaco AND e.owner IS NULL THEN 1 ELSE 0 END)
                )
                FROM Ubicacion u
                JOIN u.espiritus e
                LEFT JOIN Medium m ON m.id = e.owner.id
                WHERE u.class = Santuario
                GROUP BY u.id, u.nombre, m.id, m.nombre, m.manaMax, m.mana
                ORDER BY (SUM(CASE WHEN e.class = EspirituDemoniaco THEN 1 ELSE 0 END) 
                          - SUM(CASE WHEN e.class = EspirituAngelical THEN 1 ELSE 0 END)) DESC
            """)
    ReporteSantuarioMasCorruptoDTO obtenerReporteSantuarioMasCorrupto();

}
