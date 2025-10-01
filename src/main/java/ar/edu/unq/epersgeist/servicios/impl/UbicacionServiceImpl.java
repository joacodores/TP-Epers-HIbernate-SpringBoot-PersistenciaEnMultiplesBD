package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO,  MediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        System.out.println("Creando una ubicacion");
        return ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionDAO.findById(ubicacionId);
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        ubicacionDAO.save(ubicacion);
    }

    @Override
    public void eliminar(Long ubicacionId) {
        ubicacionDAO.deleteById(ubicacionId);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        Iterable<Ubicacion> iterable = ubicacionDAO.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void eliminarTodo() {
        ubicacionDAO.deleteAll();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return espirituDAO.espiritusEn(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId){
        return mediumDAO.mediumsSinEspiritusEn(ubicacionId);

    }

}
