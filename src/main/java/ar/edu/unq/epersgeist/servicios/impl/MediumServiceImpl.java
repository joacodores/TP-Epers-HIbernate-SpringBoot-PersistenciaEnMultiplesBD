package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exceptions.EspirituNoEsLibreException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;

    public MediumServiceImpl(MediumDAO medium, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO) {
        this.mediumDAO = medium;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public Medium crear(Medium medium) {
        return HibernateTransactionRunner.runTrx(() -> mediumDAO.crear(medium));
    }

    @Override
    public Medium recuperar(Long id) {
        return HibernateTransactionRunner.runTrx(() -> mediumDAO.recuperar(id));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(mediumDAO::recuperarTodos);
    }

    @Override
    public void actualizar(Medium medium) {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.actualizar(medium);
            return null;
        });
    }

    @Override
    public void eliminar(Medium medium) {
        HibernateTransactionRunner.runTrx(() -> {
            medium.vaciarEspiritus();
            mediumDAO.eliminar(medium);
            return null;
        });
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminarTodo();
            mediumDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        HibernateTransactionRunner.runTrx(() -> {
            Medium exorcista = mediumDAO.recuperar(idMediumExorcista);
            Medium mediumAExorcizar = mediumDAO.recuperar(idMediumAExorcizar);
            exorcista.exorcizar(mediumAExorcizar);
            mediumDAO.actualizar(exorcista);
            mediumDAO.actualizar(mediumAExorcizar);
            return null;
        });
    }

    @Override
    public void descansar(Long mediumId) {
        HibernateTransactionRunner.runTrx(() -> {
            Medium mediumADescansar = mediumDAO.recuperar(mediumId);
            mediumADescansar.descansar();
            mediumDAO.actualizar(mediumADescansar);
            return null;
        });
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return HibernateTransactionRunner.runTrx(() -> {
            Medium invocador = mediumDAO.recuperar(mediumId);
            Espiritu espirituAInvocar = espirituDAO.recuperar(espirituId);

            Ubicacion ubiDeInvocacion = invocador.getUbicacion();

            invocador.invocar(espirituAInvocar);

            mediumDAO.actualizar(invocador);
            espirituDAO.actualizar(espirituAInvocar);
            ubicacionDAO.actualizar(ubiDeInvocacion);
            return espirituAInvocar;
        });
    }

    public List<Espiritu> espiritus(Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> mediumDAO.recuperar(mediumId).getEspiritus());
    }

}
