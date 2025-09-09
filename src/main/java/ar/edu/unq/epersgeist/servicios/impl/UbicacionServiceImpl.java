package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;
    private final EspirituDAO espirituDAO;

    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.crear(ubicacion));
    }

    @Override
    public Ubicacion recuperar(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.recuperar(ubicacionId));
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.actualizar(ubicacion);
            return null;
        });
    }

    @Override
    public void eliminar(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.eliminar(ubicacion);
            return null;
        });
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(ubicacionDAO::recuperarTodos);
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> {
            return espirituDAO.espiritusEn(ubicacionId);
        });
    }
}
