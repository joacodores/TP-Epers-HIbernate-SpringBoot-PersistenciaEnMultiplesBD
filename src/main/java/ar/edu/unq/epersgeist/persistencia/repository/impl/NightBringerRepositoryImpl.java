package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.NightBringerNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.NightBringer;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repository.NightBringerRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.NightBringerSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.NightBringerSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class NightBringerRepositoryImpl implements NightBringerRepository {
    private final UbicacionSQLDAO ubicacionSQLDAO;
    private final UbicacionRepository ubicacionRepository;
    private NightBringerSQLDAO nightBringerSQLDAO;
    private static final Logger logger = LoggerFactory.getLogger(NightBringerRepositoryImpl.class);

    public NightBringerRepositoryImpl(NightBringerSQLDAO nightBringerSQLDAO, UbicacionSQLDAO ubicacionSQLDAO, UbicacionRepository ubicacionRepository) {
        this.nightBringerSQLDAO = nightBringerSQLDAO;
        this.ubicacionSQLDAO = ubicacionSQLDAO;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public NightBringer crear(NightBringer nightBringer) {
        NightBringerSQL nightBringerSQL = new NightBringerSQL(nightBringer.getNombre());
        nightBringerSQLDAO.save(nightBringerSQL);
        nightBringer.setId(nightBringerSQL.getId());
        return nightBringer;
    }

    @Override
    public NightBringer recuperar(Long nightBringerId) {
        NightBringerSQL nightBringerSQL = nightBringerSQLDAO.findById(nightBringerId).orElseThrow(() -> new NightBringerNoEncontradoException(""));
        return new NightBringer(nightBringerSQL);

    }

    @Override
    public List<NightBringer> recuperarTodos() {
       var iterable = nightBringerSQLDAO.findAll();
        List<NightBringerSQL> nightbringersSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return nightbringersSQL.stream().map(nSQL ->{
            return new NightBringer(nSQL);
        }).toList();
    }

    @Override
    public void actualizar(NightBringer nightBringer) {
        logger.info("[NB UPDATE] Persistiendo NightBringer {} ({}) con {} espíritus asociados",
                nightBringer.getNombre(), nightBringer.getId(), nightBringer.getEspiritus().size());
        NightBringerSQL nightBringerSQL = new NightBringerSQL(nightBringer);
        nightBringerSQLDAO.save(nightBringerSQL);
    }


    @Override
    public void eliminar(Long nightBringerId) {
        nightBringerSQLDAO.deleteById(nightBringerId);
    }

    @Override
    public void eliminarTodo() {nightBringerSQLDAO.deleteAll();}

}
