package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.mongo.EspirituMongoDAO;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.EspirituMongo;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.sql.EspirituSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituDemoniacoSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituSQLDAO espirituSQLDAO;
    private final EspirituMongoDAO espirituMongoDAO;

    public EspirituRepositoryImpl(EspirituSQLDAO espirituSQLDAO, EspirituMongoDAO espirituMongoDAO) {
        this.espirituSQLDAO = espirituSQLDAO;
        this.espirituMongoDAO = espirituMongoDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        EspirituSQL espirituSQL;
        if (espiritu.esAngelical()) {
            espirituSQL = new EspirituAngelicalSQL(espiritu);
        } else {
            espirituSQL = new EspirituDemoniacoSQL(espiritu);
        }
        espirituSQLDAO.save(espirituSQL);
        espiritu.setId(espirituSQL.getId());
        EspirituMongo espirituMongo = new EspirituMongo(espiritu);
        espirituMongoDAO.insert(espirituMongo);
        return espiritu;
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        EspirituSQL espirituSQL = espirituSQLDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(""));
        EspirituMongo espirituMongo = espirituMongoDAO.findByEspirituId(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(""));
        return Optional.of(Espiritu.from(espirituSQL, espirituMongo));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        var iterable = espirituSQLDAO.findAll();
        List<EspirituSQL> espiritusSQLS = StreamSupport.stream(iterable.spliterator(), false).toList();
        return espiritusSQLS.stream().map(espirituSQL -> {
            EspirituMongo espirituMongo = espirituMongoDAO.findByEspirituId(espirituSQL.getId()).orElseThrow(() -> new EspirituNoEncontradoException(""));
            return Espiritu.from(espirituSQL, espirituMongo);
        }).toList();
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Sort.Direction direccion, Integer pagina, Integer cantidadPorPagina) {
        Pageable pageable = PageRequest.of(
                pagina - 1,
                cantidadPorPagina,
                Sort.by(direccion, "nivelDeConexion")
        );
        List<EspirituSQL> espirituSQLS = espirituSQLDAO.espiritusDemoniacos(pageable).getContent();
        return espirituSQLS.stream().map(espirituSQL -> {
            EspirituMongo espirituMongo = espirituMongoDAO.findByEspirituId(espirituSQL.getId()).orElseThrow(() -> new EspirituNoEncontradoException(""));
            return Espiritu.from(espirituSQL, espirituMongo);
        }).toList();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        EspirituSQL espirituSQL;
        if (espiritu.esAngelical()) {
            espirituSQL = new EspirituAngelicalSQL(espiritu);
        } else {
            espirituSQL = new EspirituDemoniacoSQL(espiritu);
        }
        espirituSQL.setUpdatedAt(new Date());
        espirituSQLDAO.save(espirituSQL);
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituSQLDAO.deleteById(espirituId);
        espirituMongoDAO.findByEspirituId(espirituId)
                .ifPresent(espirituMongo -> espirituMongoDAO.deleteById(espirituMongo.getId()));
    }

    @Override
    public void eliminarTodo() {
        espirituSQLDAO.deleteAll(); espirituMongoDAO.deleteAll();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        List<EspirituSQL> espiritusSQL = espirituSQLDAO.espiritusEn(ubicacionId);
        return espiritusSQL.stream().map(espirituSQL -> {
            EspirituMongo espirituMongo = espirituMongoDAO.findByEspirituId(espirituSQL.getId()).orElseThrow(() -> new EspirituNoEncontradoException(""));
            return Espiritu.from(espirituSQL, espirituMongo);
        }).toList();
    }

    @Override
    public List<ReporteSantuarioMasCorruptoProjection> obtenerReporteSantuarioMasCorrupto() {
        return espirituSQLDAO.obtenerReporteSantuarioMasCorrupto();
    }

}
