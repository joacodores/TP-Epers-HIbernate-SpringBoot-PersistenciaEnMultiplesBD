package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.sql.EspirituSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.sql.MediumSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MediumRepositoryImpl implements MediumRepository {
    private final MediumSQLDAO mediumSQLDAO;


    public MediumRepositoryImpl(MediumSQLDAO mediumSQLDAO) {
        this.mediumSQLDAO = mediumSQLDAO;
    }

    @Override
    public Medium crear(Medium medium) {
        MediumSQL mediumSQL = new MediumSQL(medium);
        mediumSQLDAO.save(mediumSQL);
        medium.setId(mediumSQL.getId());
        return medium;
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        MediumSQL mediumSQL = mediumSQLDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));

        return Optional.of(new Medium(mediumSQL));
    }

    @Override
    public List<Medium> recuperarTodos() {
        var iterable = mediumSQLDAO.findAll();
        List<MediumSQL> mediumsSQLS = StreamSupport.stream(iterable.spliterator(), false).toList();
        return mediumsSQLS.stream().map(Medium::from).collect(Collectors.toList());
    }

    @Override
    public void actualizar(Medium medium) {
        MediumSQL mediumSQL = new MediumSQL(medium);
        mediumSQL.setUpdatedAt(new Date());
        mediumSQLDAO.save(mediumSQL);

    }

    @Override
    public void eliminar(Long mediumId) {
        mediumSQLDAO.deleteById(mediumId);
    }

    @Override
    public void eliminarTodo() {
        mediumSQLDAO.deleteAll();
    }


    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        List<MediumSQL> mediumsSQL = mediumSQLDAO.mediumsSinEspiritusEn(ubicacionId);
        return mediumsSQL.stream().map(Medium::from).collect(Collectors.toList());
    }
}
