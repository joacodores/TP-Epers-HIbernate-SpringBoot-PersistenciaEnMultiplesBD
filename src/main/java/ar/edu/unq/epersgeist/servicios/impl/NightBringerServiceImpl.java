package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.messaging.RealtimePublisher;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.NightBringer;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.NightBringerRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.NightBringerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NightBringerServiceImpl implements NightBringerService {

    private final NightBringerRepository nightBringerRepository;
    private final EspirituRepository espirituRepository;
    private final UbicacionRepository ubicacionRepository;
    private final RealtimePublisher realtimePublisher;


    public  NightBringerServiceImpl(NightBringerRepository nightBringerRepository, EspirituRepository espirituRepository, UbicacionRepository ubicacionRepository, RealtimePublisher realtimePublisher) {
        this.nightBringerRepository = nightBringerRepository;
        this.espirituRepository = espirituRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @Override
    public NightBringer crear(NightBringer nightBringer) {
        return nightBringerRepository.crear(nightBringer);
    }

    @Override
    public NightBringer recuperar(Long nightBringerId) {
        return nightBringerRepository.recuperar(nightBringerId);
    }

    @Override
    public List<NightBringer> recuperarTodos() {
        return nightBringerRepository.recuperarTodos();
    }

    @Override
    public void actualizar(NightBringer nightBringer) {nightBringerRepository.actualizar(nightBringer);}

    @Override
    public Espiritu spawnearEspirituEnUbicacion(Long nightbringerId, Long ubicacionId, String nombreEspiritu) {

        NightBringer n = nightBringerRepository.recuperar(nightbringerId);
        Optional<Ubicacion> ubicacion = ubicacionRepository.recuperar(ubicacionId);
        EspirituDemoniaco e = n.spawnearEspirituEnUbicacion(ubicacion.get(), nombreEspiritu);

        espirituRepository.crear(e);
        ubicacionRepository.actualizar(ubicacion.get());
        espirituRepository.actualizar(e);
        nightBringerRepository.actualizar(n);

        realtimePublisher.publishEspiritu(ubicacionId, e);

        return e;
    }

    @Override
    public void eliminar(Long nightbringerId) {
        nightBringerRepository.eliminar(nightbringerId);
    }

    @Override
    public void eliminarTodo() {nightBringerRepository.eliminarTodo();}
}
