package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionSQLDAO ubicacionSQLDAO;

    public UbicacionRepositoryImpl(UbicacionSQLDAO ubicacionSQLDAO) {
        this.ubicacionSQLDAO = ubicacionSQLDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        UbicacionSQL ubicacionSQL;
        if(ubicacion.esSantuario()) {
            ubicacionSQL = new SantuarioSQL(ubicacion);
        } else {
            ubicacionSQL = new CementerioSQL(ubicacion);
        }
        ubicacionSQLDAO.save(ubicacionSQL);
        ubicacion.setId(ubicacionSQL.getId());
        ubicacion.getEspiritus().forEach(e -> e.setUbicacion(ubicacion));
        ubicacion.getMediums().forEach(m -> m.setUbicacion(ubicacion));

        return ubicacion;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        UbicacionSQL ubicacionSQL = ubicacionSQLDAO.findById(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        if(ubicacionSQL instanceof SantuarioSQL) {
            return Optional.of(Santuario.from(ubicacionSQL));
        } else {
            return Optional.of(Cementerio.from(ubicacionSQL));
        }
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {

        UbicacionSQL ubicacionSQL = ubicacionSQLDAO.findById(ubicacion.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        ubicacionSQL.setNombre(ubicacion.getNombre());
        ubicacionSQL.setEnergia(ubicacion.getEnergia());
        ubicacionSQL.setUpdatedAt(new Date());
        ubicacionSQL.setMediums(ubicacion.getMediums().stream().map(MediumSQL::new).collect(Collectors.toCollection(ArrayList::new)));
        ubicacionSQL.setEspiritus(ubicacion.getEspiritus().stream().map(espiritu -> {
            if(espiritu.esAngelical()) {
                return new EspirituAngelicalSQL(espiritu);
            } else {
                return new EspirituDemoniacoSQL(espiritu);
            }
        }).collect(Collectors.toCollection(ArrayList::new)));


        ubicacionSQLDAO.save(ubicacionSQL);
    }

    @Override
    public void eliminar(Long ubicacionId) {
        ubicacionSQLDAO.deleteById(ubicacionId);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        var iterable = ubicacionSQLDAO.findAll();
        List<UbicacionSQL> ubicacionesSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return ubicacionesSQL.stream().map(ubicacionSQL -> {
            if(ubicacionSQL instanceof SantuarioSQL) {
                return Santuario.from(ubicacionSQL);
            } else {
                return Cementerio.from(ubicacionSQL);
            }
        }).toList();
    }

    @Override
    public void eliminarTodo() {
        ubicacionSQLDAO.deleteAll();
    }
}
