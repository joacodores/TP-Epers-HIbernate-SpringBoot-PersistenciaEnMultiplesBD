package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    private final UbicacionDAO ubicacionDAO;

    public EstadisticaServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO, UbicacionDAO ubicacionDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        List<ReporteSantuarioMasCorruptoProjection> data = espirituDAO.obtenerReporteSantuarioMasCorrupto();
        if (data.isEmpty())
            throw new NoHaySantuarioCorruptoException("No existe ningún santuario que tenga más espíritus demoníacos que angelicales");
        ReporteSantuarioMasCorruptoProjection reporte = data.getFirst();
        Medium medium = mediumDAO.recuperar(reporte.getOwnerId());
        Ubicacion ubicacion = ubicacionDAO.findById(reporte.getUbicacionId()).orElseThrow(RuntimeException::new);
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                reporte.getTotalDemonios(),
                reporte.getDemoniosLibres()
        );
    }

}
