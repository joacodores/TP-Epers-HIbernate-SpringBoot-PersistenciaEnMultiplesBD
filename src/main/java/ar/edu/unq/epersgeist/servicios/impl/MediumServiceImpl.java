package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    //private final MediumDAO mediumDAO;
    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;
    private final UbicacionRepository ubicacionRepository;

    public MediumServiceImpl(MediumRepository mediumRepository, EspirituRepository espirituRepository, UbicacionRepository ubicacionRepository) {
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Medium crear(Medium medium) {
        return mediumRepository.crear(medium);
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return mediumRepository.recuperar(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {

        return mediumRepository.recuperarTodos();
    }

    @Override
    public void actualizar(Medium medium) {
        mediumRepository.actualizar(medium);
    }

    @Override
    public void eliminar(Long mediumId) {
        mediumRepository.eliminar(mediumId);
    }

    @Override
    public void eliminarTodo() {
        mediumRepository.eliminarTodo();
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium exorcista = mediumRepository.recuperar(idMediumExorcista).orElseThrow(() -> new MediumNoEncontradoException("Medium no encontrado"));
        Medium mediumAExorcizar = mediumRepository.recuperar(idMediumAExorcizar).orElseThrow(() -> new MediumNoEncontradoException("Medium no encontrado"));
        if (exorcista.getUbicacion().equals(mediumAExorcizar.getUbicacion())) {
            exorcista.exorcizar(mediumAExorcizar);
            mediumRepository.actualizar(exorcista);
            mediumRepository.actualizar(mediumAExorcizar);
        }
    }

    @Override
    public void descansar(Long mediumId) {
         Medium medium = mediumRepository.recuperar(mediumId).orElseThrow(() -> new  MediumNoEncontradoException("Medium no encontrado"));
         medium.descansar();
         mediumRepository.actualizar(medium);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Medium invocador = mediumRepository.recuperar(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        Espiritu espirituAInvocar = espirituRepository.recuperar(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(""));
        Ubicacion ubicacionDeInvocacion = invocador.getUbicacion();
        invocador.invocar(espirituAInvocar);
        espirituRepository.actualizar(espirituAInvocar);
        ubicacionRepository.actualizar(ubicacionDeInvocacion);
        return espirituAInvocar;
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        Medium medium = mediumRepository.recuperar(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        return medium.getEspiritus();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = mediumRepository.recuperar(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        Ubicacion ubicacion = ubicacionRepository.recuperar(ubicacionId).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        medium.mover(ubicacion);
        mediumRepository.actualizar(medium);
    }

}
