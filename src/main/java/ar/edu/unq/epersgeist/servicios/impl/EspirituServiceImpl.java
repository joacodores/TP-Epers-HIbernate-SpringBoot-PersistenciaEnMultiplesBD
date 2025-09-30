package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return espirituDAO.save(espiritu);
    }
    /*
    @Override
    public void eliminar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminar(espiritu);
            return null;
        });
    }
    */
    @Override
    public Optional<Espiritu> recuperar(Long ubicacionId) {
        return espirituDAO.findById(ubicacionId);
    }


    @Override
    public List<Espiritu> recuperarTodos() {
        Iterable<Espiritu> iterable = espirituDAO.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        espirituDAO.save(espiritu);
    }

    /*
    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminarTodo();
            return null;
        });
    }


    @Override
    public Page<Espiritu> espiritusDemoniacos(Pageable pageable){
        return espirituDAO.findAll(pageable);
    }

    */

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = espirituDAO.recuperar(espirituId);
        Medium medium = mediumDAO.recuperar(mediumId);
        medium.conectarseAEspiritu(espiritu);
        espirituDAO.save(espiritu);
        return medium;
    }

}
