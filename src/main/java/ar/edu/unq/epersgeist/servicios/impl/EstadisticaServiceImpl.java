package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioMasCorruptoDTO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionDAO ubicacionDAO;

    public EstadisticaServiceImpl(UbicacionDAO ubicacionDAO) {
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public ReporteSantuarioMasCorruptoDTO santuarioCorrupto() {
        return ubicacionDAO.obtenerReporteSantuarioMasCorrupto();
    }

}
