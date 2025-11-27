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
import ar.edu.unq.epersgeist.persistencia.sql.NightBringerSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituDemoniacoSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.NightBringerSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final NightBringerSQLDAO nightBringerSQLDAO;
    private static final Logger logger = LoggerFactory.getLogger(EspirituRepositoryImpl.class);

    public EspirituRepositoryImpl(EspirituSQLDAO espirituSQLDAO, EspirituMongoDAO espirituMongoDAO, NightBringerSQLDAO nightBringerSQLDAO) {
        this.espirituSQLDAO = espirituSQLDAO;
        this.espirituMongoDAO = espirituMongoDAO;
        this.nightBringerSQLDAO = nightBringerSQLDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        EspirituSQL espirituSQL;
        if (espiritu.esAngelical()) {
            espirituSQL = new EspirituAngelicalSQL(espiritu);
        } else {
            espirituSQL = new EspirituDemoniacoSQL(espiritu);
        }
        
        // Si el espíritu tiene un NightBringer, establecer la referencia correcta desde la BD
        if (espiritu.getNightBringer() != null && espiritu.getNightBringer().getId() != null) {
            NightBringerSQL nightBringerSQL = nightBringerSQLDAO.findById(espiritu.getNightBringer().getId())
                    .orElseThrow(() -> new RuntimeException("NightBringer no encontrado con ID: " + espiritu.getNightBringer().getId()));
            espirituSQL.setNightBringer(nightBringerSQL);
            logger.info("Asignando NightBringer {} ({}) al espíritu '{}'", nightBringerSQL.getNombre(), nightBringerSQL.getId(), espiritu.getNombre());
        } else {
            logger.warn("Se está intentando persistir el espíritu '{}' sin NightBringer asociado (nightBringerId={})",
                    espiritu.getNombre(),
                    espiritu.getNightBringer() != null ? espiritu.getNightBringer().getId() : null);
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
        List<EspirituMongo> espirituMongoList = espirituMongoDAO.findByEspirituId(espirituId);
        if (espirituMongoList.isEmpty()) {
            throw new EspirituNoEncontradoException("");
        }
        return Optional.of(Espiritu.from(espirituSQL, espirituMongoList.getFirst()));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        var iterable = espirituSQLDAO.findAll();
        List<EspirituSQL> espiritusSQLS = StreamSupport.stream(iterable.spliterator(), false).toList();
        return espiritusSQLS.stream().map(espirituSQL -> {
            List<EspirituMongo> espirituMongoList = espirituMongoDAO.findByEspirituId(espirituSQL.getId());
            if (espirituMongoList.isEmpty()) {
                throw new EspirituNoEncontradoException("");
            }
            return Espiritu.from(espirituSQL, espirituMongoList.getFirst());
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
            List<EspirituMongo> espirituMongoList = espirituMongoDAO.findByEspirituId(espirituSQL.getId());
            if (espirituMongoList.isEmpty()) {
                throw new EspirituNoEncontradoException("");
            }
            return Espiritu.from(espirituSQL, espirituMongoList.getFirst());
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

        // Mantener la referencia al NightBringer en la entidad SQL, si aplica
        if (espiritu.getNightBringer() != null && espiritu.getNightBringer().getId() != null) {
            NightBringerSQL nightBringerSQL = nightBringerSQLDAO.findById(espiritu.getNightBringer().getId())
                    .orElseThrow(() -> new RuntimeException("NightBringer no encontrado con ID: " + espiritu.getNightBringer().getId()));
            espirituSQL.setNightBringer(nightBringerSQL);
            logger.info("[UPDATE] Manteniendo NightBringer {} ({}) en el espíritu '{}' (ID={})",
                    nightBringerSQL.getNombre(), nightBringerSQL.getId(), espiritu.getNombre(), espiritu.getId());
        } else {
            logger.warn("[UPDATE] Espíritu '{}' (ID={}) llegó a actualizarse sin NightBringer. Se persistirá en null si no se corrige antes.",
                    espiritu.getNombre(), espiritu.getId());
        }

        EspirituMongo espirituMongo = new EspirituMongo(espiritu);

        espirituMongoDAO.save(espirituMongo);
        espirituSQLDAO.save(espirituSQL);
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituSQLDAO.deleteById(espirituId);
        espirituMongoDAO.deleteByEspirituId(espirituId);
    }

    @Override
    public void eliminarTodo() {
        espirituSQLDAO.deleteAll();
        espirituMongoDAO.deleteAll();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        List<EspirituSQL> espiritusSQL = espirituSQLDAO.espiritusEn(ubicacionId);
        return espiritusSQL.stream()
                .filter(espirituSQL -> espirituSQL.getNightBringer() != null) // Filtrar espíritus corruptos
                .map(espirituSQL -> {
                    List<EspirituMongo> espirituMongoList = espirituMongoDAO.findByEspirituId(espirituSQL.getId());
                    if (espirituMongoList.isEmpty()) {
                        throw new EspirituNoEncontradoException("");
                    }
                    return Espiritu.from(espirituSQL, espirituMongoList.getFirst());
                }).toList();
    }

    @Override
    public List<ReporteSantuarioMasCorruptoProjection> obtenerReporteSantuarioMasCorrupto() {
        return espirituSQLDAO.obtenerReporteSantuarioMasCorrupto();
    }

    @Override
    public void eliminarEspiritusSinNightBringer() {
        espirituSQLDAO.eliminarEspiritusSinNightBringer();
    }

}
