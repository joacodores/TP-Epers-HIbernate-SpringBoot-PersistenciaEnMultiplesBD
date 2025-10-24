package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.servicios.exceptions.UbicacionesNoConectadasException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, EspirituRepository espirituRepository, MediumRepository mediumRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        return ubicacionRepository.crear(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionRepository.recuperar(ubicacionId);
    }
//
//    @Override
//    public Optional<Ubicacion> recuperarConConexiones(Long ubicacionId){return ubicacionRepository.recuperarConConexiones(ubicacionId);}

    @Override
    public void actualizar(Ubicacion ubicacion) {
        ubicacionRepository.actualizar(ubicacion);
    }

    @Override
    public void eliminar(Long ubicacionId) {
        ubicacionRepository.eliminar(ubicacionId);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionRepository.recuperarTodos();
    }

    @Override
    public void eliminarTodo() {
        ubicacionRepository.eliminarTodo();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return espirituRepository.espiritusEn(ubicacionId);
    }

    @Override
    public void conectar(Long idOrigen, Long idDestino, Long costo) {
        ubicacionRepository.conectar(idOrigen, idDestino, costo);
    }

    @Override
    public Boolean estanConectadas(Long idOrigen, Long idDestino) {
        return ubicacionRepository.estanConectadas(idOrigen, idDestino);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return mediumRepository.mediumsSinEspiritusEn(ubicacionId);
    }

    private List<Ubicacion> findPath(
            Long idOrigen,
            Long idDestino,
            BiFunction<Long, Long, List<Ubicacion>> finder
    ) {
        return idOrigen.equals(idDestino)
                ? List.of(recuperar(idOrigen)
                .orElseThrow(() -> new UbicacionNoEncontradaException("")))
                : Optional.ofNullable(finder.apply(idOrigen, idDestino))
                .filter(c -> !c.isEmpty())
                .orElseThrow(() -> new UbicacionesNoConectadasException(idOrigen, idDestino));
    }

    public List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino) {
        return findPath(idOrigen, idDestino, ubicacionRepository::caminoMasCorto);
    }

    public List<Ubicacion> caminoMasRentable(Long idOrigen, Long idDestino) {
        return findPath(idOrigen, idDestino, ubicacionRepository::caminoMasRentable);
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia) {
        return ubicacionRepository.ubicacionesSobrecargadas(umbralDeEnergia);
    }

}
