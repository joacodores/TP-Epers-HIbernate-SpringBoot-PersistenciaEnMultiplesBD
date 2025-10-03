package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ReporteSantuarioMasCorruptoProjection data = espirituDAO.obtenerReporteSantuarioMasCorrupto();
        Medium medium = mediumDAO.recuperar(data.getOwnerId());
        Ubicacion ubicacion = ubicacionDAO.findById(data.getUbicacionId()).orElseThrow(RuntimeException::new);
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                data.getTotalDemonios(),
                data.getDemoniosLibres()
        );
    }

}
