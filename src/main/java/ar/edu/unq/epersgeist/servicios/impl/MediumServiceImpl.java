package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoEsLibreException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;

    public MediumServiceImpl(MediumDAO medium, EspirituDAO espirituDAO) {
        this.mediumDAO = medium;
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Medium crear(Medium medium){
        return HibernateTransactionRunner.runTrx(() -> mediumDAO.crear(medium));
    }

    @Override
    public Medium recuperar(Long id){
        return HibernateTransactionRunner.runTrx(() -> mediumDAO.recuperar(id));
    }

    @Override
    public List<Medium> recuperarTodos(){
        return HibernateTransactionRunner.runTrx(mediumDAO::recuperarTodos);
    }

    @Override
    public void actualizar(Medium medium){
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.actualizar(medium);
            return null;
        });
    }

    @Override
    public void eliminar(Medium medium){
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.eliminar(medium);
            return null;
        });
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId){
        return HibernateTransactionRunner.runTrx(() -> {
                    Medium invocador = mediumDAO.recuperar(mediumId);
                    Espiritu espirituAInvocar = espirituDAO.recuperar(espirituId);
                        invocador.invocar(espirituAInvocar);
                        mediumDAO.actualizar(invocador);
                        espirituDAO.actualizar(espirituAInvocar);
                        return espirituAInvocar;
                });
    }

}
