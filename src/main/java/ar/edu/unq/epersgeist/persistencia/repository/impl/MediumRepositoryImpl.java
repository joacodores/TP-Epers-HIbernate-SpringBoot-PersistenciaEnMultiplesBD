package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.mongo.MediumMongoDAO;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.MediumMongo;
import ar.edu.unq.epersgeist.persistencia.repository.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.sql.MediumSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.MediumSQL;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MediumRepositoryImpl implements MediumRepository {

    private final MediumSQLDAO mediumSQLDAO;
    private final MediumMongoDAO mediumMongoDAO;

    public MediumRepositoryImpl(MediumSQLDAO mediumSQLDAO, MediumMongoDAO mediumMongoDAO) {
        this.mediumSQLDAO = mediumSQLDAO;
        this.mediumMongoDAO =mediumMongoDAO;
    }

    @Override
    public Medium crear(Medium medium) {
        MediumSQL mediumSQL = new MediumSQL(medium);
        mediumSQLDAO.save(mediumSQL);
        medium.setId(mediumSQL.getId());

        MediumMongo mediumMongo = new MediumMongo(medium);
        mediumMongoDAO.insert(mediumMongo);

        return medium;
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        MediumSQL mediumSQL = mediumSQLDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        MediumMongo mediumMongo = mediumMongoDAO.findByMediumId(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        return Optional.of(Medium.from(mediumSQL, mediumMongo));
    }

    @Override
    public List<Medium> recuperarTodos() {
        var iterable = mediumSQLDAO.findAll();
        List<MediumSQL> mediumsSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return mediumsSQL.stream()
                .map(sql -> {
                    MediumMongo mongo = mediumMongoDAO.findByMediumId(sql.getId())
                            .orElseThrow(() -> new MediumNoEncontradoException(" " ));
                    return Medium.from(sql, mongo);
                })
                .toList();
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
        mediumMongoDAO.findByMediumId(mediumId)
                .ifPresent(mediumMongo -> mediumMongoDAO.deleteById(mediumMongo.getId()));
    }

    @Override
    public void eliminarTodo() {
        mediumSQLDAO.deleteAll();
        mediumMongoDAO.deleteAll();
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        List<MediumSQL> mediumsSQL = mediumSQLDAO.mediumsSinEspiritusEn(ubicacionId);
        return mediumsSQL.stream()
                .map(sql -> {
                    MediumMongo mongo = mediumMongoDAO.findByMediumId(sql.getId())
                            .orElseThrow(() -> new MediumNoEncontradoException(" "));
                    return Medium.from(sql, mongo);
                })
                .toList();
    }

}
