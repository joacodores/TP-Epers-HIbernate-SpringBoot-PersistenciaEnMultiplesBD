package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private EspirituDAO espirituDAO;
    public EspirituServiceImpl(EspirituDAO espirituDAO) {
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {

        return espirituDAO.crear(espiritu);
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return espirituDAO.recuperar(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.recuperarTodos();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        espirituDAO.actualizar(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituDAO.eliminar(espirituId);
    }

    @Override
    public Medium conectar(Long espirituId, Medium medium) {
        Espiritu espiritu = espirituDAO.recuperar(espirituId);
        medium.conectarseAEspiritu(espiritu);
        this.actualizar(espiritu);
        return medium;
    };
}
