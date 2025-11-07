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
        List<MediumMongo> mediumMongoList = mediumMongoDAO.findByMediumId(mediumId);
        if(mediumMongoList.isEmpty()){
            throw new MediumNoEncontradoException("Medium no encontrado");
        }

        return Optional.of(Medium.from(mediumSQL, mediumMongoList.getFirst()));
    }

    @Override
    public List<Medium> recuperarTodos() {
        var iterable = mediumSQLDAO.findAll();
        List<MediumSQL> mediumsSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return mediumsSQL.stream()
                .map(sql -> {
                    List<MediumMongo> mediumMongoList = mediumMongoDAO.findByMediumId(sql.getId());
                    if(mediumMongoList.isEmpty()){
                        throw new MediumNoEncontradoException("Medium no encontrado");
                    }
                    return Medium.from(sql, mediumMongoList.getFirst());
                })
                .toList();
    }

    @Override
    public void actualizar(Medium medium) {
        MediumSQL mediumSQL = new MediumSQL(medium);
        MediumMongo mediumMongo = new MediumMongo(medium);
        mediumSQL.setUpdatedAt(new Date());
        mediumSQLDAO.save(mediumSQL);
        mediumMongoDAO.save(mediumMongo);
    }

    @Override
    public void eliminar(Long mediumId) {
        mediumSQLDAO.deleteById(mediumId);
        mediumMongoDAO.deleteByMediumId(mediumId);
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
                    List<MediumMongo> mediumMongoList = mediumMongoDAO.findByMediumId(sql.getId());
                    if(mediumMongoList.isEmpty()){
                        throw new MediumNoEncontradoException("Medium no encontrado");
                    }
                    return Medium.from(sql, mediumMongoList.getFirst());
                })
                .toList();
    }

}
