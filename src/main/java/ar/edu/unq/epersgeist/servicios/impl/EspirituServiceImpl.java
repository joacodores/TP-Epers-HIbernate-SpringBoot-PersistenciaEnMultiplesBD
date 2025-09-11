package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.enums.Direccion;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO) {
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.crear(espiritu));
    }

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

     /*@Override
    public Medium conectar(Long espirituId, Medium medium) {
        Espiritu espiritu = espirituDAO.recuperar(espirituId);
        medium.conectarseAEspiritu(espiritu);
        this.actualizar(espiritu);
        return medium;
    };*/
}
