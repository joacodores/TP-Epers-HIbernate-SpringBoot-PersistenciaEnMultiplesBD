package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
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
        Ubicacion ubicacion = ubicacionRepository.recuperar(reporte.getUbicacionId()).orElseThrow(RuntimeException::new);
        var espiritusEnUbi = espirituRepository.espiritusEn(reporte.getUbicacionId());
        int totalDemonios = (int) espiritusEnUbi.stream().filter(e -> e instanceof EspirituDemoniaco).count();
        int demoniosLibres = (int) espiritusEnUbi.stream().filter(e -> e instanceof EspirituDemoniaco && e.getOwner() == null).count();
        Long ownerId = espiritusEnUbi.stream()
                .filter(e -> e instanceof EspirituDemoniaco && e.getOwner() != null)
                .map(e -> e.getOwner().getId())
                .collect(java.util.stream.Collectors.groupingBy(id -> id, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.<Long, Long>comparingByValue().thenComparingLong(java.util.Map.Entry::getKey))
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
        Medium medium = ownerId != null ? mediumRepository.recuperar(ownerId).orElse(null) : null;
        return new ReporteSantuarioMasCorrupto(
                ubicacion.getNombre(),
                medium,
                totalDemonios,
                demoniosLibres
        );
    }

}
