package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.persistencia.repository.CoordenadaRepository;
import ar.edu.unq.epersgeist.servicios.CoordenadaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoordenadaServiceImpl implements CoordenadaService {

    private final CoordenadaRepository coordenadaRepository;

    public  CoordenadaServiceImpl(CoordenadaRepository coordenadaRepository) {
        this.coordenadaRepository = coordenadaRepository;
    }

    @Override
    public Coordenada crear(Coordenada coordenada) {

        return coordenadaRepository.crear(coordenada);
    }

    @Override
    public Coordenada recuperar(Long coordenadaId) {
        return coordenadaRepository.recuperar(coordenadaId);
    }

    @Override
    public List<Coordenada> recuperarTodos() {
        return  coordenadaRepository.recuperarTodos();
    }

    @Override
    public void eliminar(Long coordenadaId) {
        coordenadaRepository.eliminar(coordenadaId);

    }

    @Override
    public void eliminarTodo() {
        coordenadaRepository.eliminarTodo();
    }

    @Override
    public double distanciaEnKm(Long idCoordA, Long idCoordB) {

        Coordenada coordA = this.recuperar(idCoordA);
        Coordenada coordB = this.recuperar(idCoordB);

        return coordA.distanciaEnKm(coordB);
    }
}
