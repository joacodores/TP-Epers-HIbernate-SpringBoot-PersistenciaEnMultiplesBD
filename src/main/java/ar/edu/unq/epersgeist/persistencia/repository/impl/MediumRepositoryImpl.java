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
        MediumSQL mediumSQL = mediumSQLDAO.findById(medium.getId())
                .orElseThrow(() -> new MediumNoEncontradoException(""));
        mediumSQL.setNombre(medium.getNombre());
        mediumSQL.setManaMax(medium.getManaMax());
        mediumSQL.setMana(medium.getMana());
        if(medium.getUbicacion().esSantuario()) {
            mediumSQL.setUbicacion(new SantuarioSQL(medium.getUbicacion()));
        } else {
            mediumSQL.setUbicacion(new CementerioSQL(medium.getUbicacion()));
        }
        mediumSQL.setEspiritus(medium.getEspiritus().stream().map(espiritu -> {
            if(espiritu.esAngelical()) {
                return new EspirituAngelicalSQL(espiritu);
            } else {
                return new EspirituDemoniacoSQL(espiritu);
            }
        }).collect(Collectors.toCollection(ArrayList::new)));
        mediumSQLDAO.save(mediumSQL);

    }

    @Override
    public void eliminar(Long mediumId) {
        MediumSQL mediumSQL = mediumSQLDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        Medium mediumExistente = Medium.from(mediumSQL);
        mediumExistente.getUbicacion().eliminarMedium(mediumExistente);
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
