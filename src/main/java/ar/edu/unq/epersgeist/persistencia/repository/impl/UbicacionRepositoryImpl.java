package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.neo.UbicacionNeo4JDAO;
import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionSQLDAO ubicacionSQLDAO;
    private UbicacionNeo4JDAO ubicacionNeo4JDAO;

    public UbicacionRepositoryImpl(UbicacionSQLDAO ubicacionSQLDAO, UbicacionNeo4JDAO ubicacionNeo4JDAO) {
        this.ubicacionSQLDAO = ubicacionSQLDAO;
        this.ubicacionNeo4JDAO = ubicacionNeo4JDAO;
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


        UbicacionNeo4J ubicacionNeo = new UbicacionNeo4J(ubicacion);
        ubicacionNeo4JDAO.save(ubicacionNeo);

        return ubicacion;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        UbicacionSQL ubicacionSQL = ubicacionSQLDAO.findById(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));

        UbicacionNeo4J ubicacionNeo4j = ubicacionNeo4JDAO.findById(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        return Optional.of(Ubicacion.from(ubicacionSQL, ubicacionNeo4j));

    }

    @Override
    public void actualizar(Ubicacion ubicacion) {

        UbicacionSQL ubicacionSQL = ubicacionSQLDAO.findById(ubicacion.getId()).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        ubicacionSQL.setNombre(ubicacion.getNombre());
        ubicacionSQL.setEnergia(ubicacion.getEnergia());
        ubicacionSQL.setCosto(ubicacion.getCosto());
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

        UbicacionNeo4J ubicacionNeo = new UbicacionNeo4J(ubicacion);
        ubicacionNeo4JDAO.save(ubicacionNeo);
        // Neo reemplaza el nodo existente con el mismo id y lo actualiza

    }

    @Override
    public void eliminar(Long ubicacionId) {

        ubicacionSQLDAO.deleteById(ubicacionId);
        ubicacionNeo4JDAO.deleteById(ubicacionId);
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
        ubicacionNeo4JDAO.deleteAll();
    }
}
