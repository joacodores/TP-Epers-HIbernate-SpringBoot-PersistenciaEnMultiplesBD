package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.EspirituSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.MediumSQLDAO;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.servicios.EstadisticaService;
import ar.edu.unq.epersgeist.servicios.exceptions.NoHaySantuarioCorruptoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;
    private final UbicacionRepository ubicacionRepository;

    public EstadisticaServiceImpl(EspirituRepository espirituRepository, MediumRepository mediumRepository, UbicacionRepository ubicacionRepository) {
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        List<ReporteSantuarioMasCorruptoProjection> data = espirituRepository.obtenerReporteSantuarioMasCorrupto();
        if (data.isEmpty())
            throw new NoHaySantuarioCorruptoException("No existe ningún santuario que tenga más espíritus demoníacos que angelicales");
        ReporteSantuarioMasCorruptoProjection reporte = data.getFirst();
        Medium medium = mediumRepository.recuperar(reporte.getOwnerId()).orElseThrow(() -> new MediumNoEncontradoException(""));
        Ubicacion ubicacion = ubicacionRepository.recuperar(reporte.getUbicacionId()).orElseThrow(RuntimeException::new);
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                reporte.getTotalDemonios(),
                reporte.getDemoniosLibres()
        );
    }

}
