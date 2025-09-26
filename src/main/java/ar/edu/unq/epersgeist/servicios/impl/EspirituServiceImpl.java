package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.enums.Direccion;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    //private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO/*, MediumDAO mediumDAO*/) {
        this.espirituDAO = espirituDAO;
        //this.mediumDAO = mediumDAO;
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

    @Override
    public Espiritu recuperar(Long id) {
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.recuperar(id));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(espirituDAO::recuperarTodos);
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.actualizar(espiritu);
            return null;
        });
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion direccion, Integer pagina, Integer cantidadPorPagina) {
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.espiritusDemoniacos(direccion, pagina, cantidadPorPagina));
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> {
            Medium medium = mediumDAO.recuperar(mediumId);
            Espiritu espiritu = espirituDAO.recuperar(espirituId);
            medium.conectarseAEspiritu(espiritu);
            espirituDAO.actualizar(espiritu);
            mediumDAO.actualizar(medium);
            return medium;
        });

    }*/

}
