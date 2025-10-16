package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import ar.edu.unq.epersgeist.persistencia.repository.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.EspirituSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituAngelicalSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituDemoniacoSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.EspirituSQL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class EspirituRepositoryImpl implements EspirituRepository {

    private final EspirituSQLDAO espirituSQLDAO;

    public EspirituRepositoryImpl(EspirituSQLDAO espirituSQLDAO) {
        this.espirituSQLDAO = espirituSQLDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        EspirituSQL espirituSQL;
        if(espiritu.esAngelical()) {
            espirituSQL = new EspirituAngelicalSQL(espiritu);
        } else {
            espirituSQL = new EspirituDemoniacoSQL(espiritu);
        }
        espirituSQLDAO.save(espirituSQL);
        espiritu.setId(espirituSQL.getId());
        return espiritu;
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        EspirituSQL espirituSQL = espirituSQLDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(""));
        if(espirituSQL instanceof EspirituAngelicalSQL) {
            return Optional.of(new EspirituAngelical(espirituSQL));
        } else {
            return Optional.of(new EspirituDemoniaco(espirituSQL));
        }
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        var iterable = espirituSQLDAO.findAll();
        List<EspirituSQL> espiritusSQLS = StreamSupport.stream(iterable.spliterator(), false).toList();
        return espiritusSQLS.stream().map(espirituSQL -> {
            if(espirituSQL instanceof EspirituAngelicalSQL) {
                return new EspirituAngelical(espirituSQL);
            } else {
                return new EspirituDemoniaco(espirituSQL);
            }
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
            if(espirituSQL instanceof EspirituAngelicalSQL) {
                return new EspirituAngelical(espirituSQL);
            } else {
                return new EspirituDemoniaco(espirituSQL);
            }
        }).toList();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        EspirituSQL espirituSQL = espirituSQLDAO.findById(espiritu.getId())
                .orElseThrow(() -> new EspirituNoEncontradoException(""));
        Espiritu espirituAActualizar;
        if(espirituSQL instanceof EspirituAngelicalSQL) {
            espirituAActualizar = new EspirituAngelical(espirituSQL);
        } else {
            espirituAActualizar = new EspirituDemoniaco(espirituSQL);
        }

        if(espiritu.getNombre() != null ) {
            espirituAActualizar.setNombre(espiritu.getNombre());
        }

        espirituAActualizar.setUpdatedAt();

        if(espirituAActualizar.esAngelical()) {
            espirituSQLDAO.save(new EspirituAngelicalSQL(espirituAActualizar));
        } else {
            espirituSQLDAO.save(new EspirituDemoniacoSQL(espirituAActualizar));
        }
    }

    @Override
    public void eliminar(Long espirituId) {
        EspirituSQL espirituSQL = espirituSQLDAO.findById(espirituId)
                .orElseThrow(() -> new EspirituNoEncontradoException(""));
        Espiritu espiritu;
        if(espirituSQL instanceof EspirituAngelicalSQL) {
            espiritu =  new EspirituAngelical(espirituSQL);
        } else {
            espiritu = new EspirituDemoniaco(espirituSQL);
        }

        espiritu.getUbicacion().eliminarEspiritu(espiritu);
        espirituSQLDAO.deleteById(espirituId);
    }

    @Override
    public void eliminarTodo() {
        espirituSQLDAO.deleteAll();
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        List<EspirituSQL> espiritusSQL = espirituSQLDAO.espiritusEn(ubicacionId);
        return espiritusSQL.stream().map(espirituSQL -> {
            if(espirituSQL instanceof EspirituAngelicalSQL) {
                return new EspirituAngelical(espirituSQL);
            } else {
                return new EspirituDemoniaco(espirituSQL);
            }
        }).toList();
    }

    @Override
    public List<ReporteSantuarioMasCorruptoProjection> obtenerReporteSantuarioMasCorrupto() {
        return espirituSQLDAO.obtenerReporteSantuarioMasCorrupto();
    }
}
