package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.CoordenadaNoEncontradaException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.persistencia.repository.CoordenadaRepository;
import ar.edu.unq.epersgeist.persistencia.sql.CoordenadaSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.CoordenadaSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class CoordenadaRepositoryImpl implements CoordenadaRepository {

    private final CoordenadaSQLDAO coordenadaSQLDAO;

    public CoordenadaRepositoryImpl(CoordenadaSQLDAO coordenadaSQLDAO) {
        this.coordenadaSQLDAO = coordenadaSQLDAO;
    }

    @Override
    public Coordenada crear(Coordenada coordenada) {
         CoordenadaSQL c = coordenadaSQLDAO.save(new CoordenadaSQL(coordenada));
         coordenada.setId(c.getId());
         return coordenada;
    }

    @Override
    public Coordenada recuperar(Long coordenadaId) {
        CoordenadaSQL coordenadaSQL = coordenadaSQLDAO.findById(coordenadaId).orElseThrow(() -> new CoordenadaNoEncontradaException(""));
        return new Coordenada(coordenadaSQL);
    }

    @Override
    public List<Coordenada> recuperarTodos() {
        var iterable = coordenadaSQLDAO.findAll();
        List<CoordenadaSQL> coordenadasSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return coordenadasSQL.stream().map(Coordenada::new).toList();
    }

    @Override
    public void eliminar(Long coordenadaId) {
        coordenadaSQLDAO.deleteById(coordenadaId);
    }

    @Override
    public void eliminarTodo() {
        coordenadaSQLDAO.deleteAll();
    }
}
