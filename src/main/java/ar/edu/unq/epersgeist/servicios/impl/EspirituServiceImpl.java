package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO,  MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu){
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.crear(espiritu));
    }

    @Override
    public void eliminar(Espiritu espiritu){
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminar(espiritu);
            return null;
        });
    }
    @Override
    public Espiritu recuperar(Long id){
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.recuperar(id));
    }

    @Override
    public List<Espiritu> recuperarTodos(){
        return HibernateTransactionRunner.runTrx(espirituDAO::recuperarTodos);
    }

    @Override
    public void actualizar(Espiritu espiritu){
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
    public Medium conectar(Long espirituId, Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> {
            Medium medium = mediumDAO.recuperar(mediumId);
            Espiritu espiritu = espirituDAO.recuperar(espirituId);
            medium.conectarseAEspiritu(espiritu);
            mediumDAO.actualizar(medium);
            espirituDAO.actualizar(espiritu);
            return medium;
        });

    };



}
