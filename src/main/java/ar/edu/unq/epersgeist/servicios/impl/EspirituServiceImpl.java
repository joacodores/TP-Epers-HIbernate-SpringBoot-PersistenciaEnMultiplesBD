package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituDominadoException;
import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoPuedeSerDominadoException;
import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;
    private final UbicacionRepository ubicacionRepository;

    public EspirituServiceImpl(EspirituRepository espirituRepository, MediumRepository mediumRepository, UbicacionRepository ubicacionRepository) {
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return espirituRepository.crear(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituRepository.eliminar(espirituId);
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return espirituRepository.recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituRepository.recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        espirituRepository.actualizar(espiritu);
    }

    @Override
    public void eliminarTodo() {
        espirituRepository.eliminarTodo();
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Sort.Direction direccion, Integer pagina, Integer cantidadPorPagina) {
        return espirituRepository.espiritusDemoniacos(direccion, pagina, cantidadPorPagina);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = espirituRepository.recuperar(espirituId)
                .orElseThrow(() -> new EspirituNoEncontradoException(""));
        if(espiritu.estaSiendoDominado()) {
            throw new EspirituDominadoException("El espiritu esta siendo dominado");
        }
        Medium medium = mediumRepository.recuperar(mediumId)
                .orElseThrow(() -> new MediumNoEncontradoException(""));
        medium.conectarseAEspiritu(espiritu);
        mediumRepository.actualizar(medium);
        espirituRepository.actualizar(espiritu);
        return medium;
    }

    @Override
    public void dominar(Long espirituDominanteId, Long espirituADominarId) {
        Espiritu espirituADominar = espirituRepository.recuperar(espirituADominarId).orElseThrow(() -> new EspirituNoEncontradoException(""));
        Espiritu espirituDominante = espirituRepository.recuperar(espirituDominanteId).orElseThrow(() -> new EspirituNoEncontradoException(""));

        espirituDominante.dominar(espirituADominar);

        espirituRepository.actualizar(espirituDominante);
        espirituRepository.actualizar(espirituADominar);
    }

}
