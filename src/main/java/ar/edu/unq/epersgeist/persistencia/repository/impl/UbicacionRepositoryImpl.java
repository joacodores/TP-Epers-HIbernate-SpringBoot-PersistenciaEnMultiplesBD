package ar.edu.unq.epersgeist.persistencia.repository.impl;

import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Coordenada;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.mongo.UbicacionMongoDAO;
import ar.edu.unq.epersgeist.persistencia.mongo.entity.UbicacionMongo;
import ar.edu.unq.epersgeist.persistencia.neo.UbicacionNeo4JDAO;
import ar.edu.unq.epersgeist.persistencia.neo.entity.UbicacionNeo4J;
import ar.edu.unq.epersgeist.persistencia.repository.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.sql.UbicacionSQLDAO;
import ar.edu.unq.epersgeist.persistencia.sql.entity.CementerioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.SantuarioSQL;
import ar.edu.unq.epersgeist.persistencia.sql.entity.UbicacionSQL;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

@Component
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private final UbicacionSQLDAO ubicacionSQLDAO;
    private final UbicacionNeo4JDAO ubicacionNeo4JDAO;
    private final UbicacionMongoDAO ubicacionMongoDAO;

    public UbicacionRepositoryImpl(UbicacionSQLDAO ubicacionSQLDAO, UbicacionNeo4JDAO ubicacionNeo4JDAO, UbicacionMongoDAO ubicacionMongoDAO) {
        this.ubicacionSQLDAO = ubicacionSQLDAO;
        this.ubicacionNeo4JDAO = ubicacionNeo4JDAO;
        this.ubicacionMongoDAO = ubicacionMongoDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        UbicacionSQL ubicacionSQL;
        if (ubicacion.esSantuario()) {
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

        UbicacionMongo ubicacionMongo = new UbicacionMongo(ubicacion);
        ubicacionMongoDAO.insert(ubicacionMongo);
        return ubicacion;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        UbicacionSQL ubicacionSQL = ubicacionSQLDAO.findById(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        UbicacionNeo4J ubicacionNeo4j = ubicacionNeo4JDAO.findById(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        UbicacionMongo ubicacionMongo = ubicacionMongoDAO.findById(String.valueOf(ubicacionId))
                .orElseThrow(() -> new UbicacionNoEncontradaException(""));
        return Optional.of(Ubicacion.from(ubicacionSQL, ubicacionNeo4j, ubicacionMongo));
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        UbicacionSQL ubicacionSQL;
        if (ubicacion.esSantuario()) {
            ubicacionSQL = new SantuarioSQL(ubicacion);
        } else {
            ubicacionSQL = new CementerioSQL(ubicacion);
        }
        ubicacionSQL.setUpdatedAt(new Date());
        ubicacionSQLDAO.save(ubicacionSQL);
        UbicacionNeo4J ubicacionNeo = new UbicacionNeo4J(ubicacion);
        ubicacionNeo4JDAO.save(ubicacionNeo);
        // Neo reemplaza el nodo existente con el mismo id y lo actualiza
    }

    @Override
    public void eliminar(Long ubicacionId) {
        ubicacionSQLDAO.deleteById(ubicacionId);
        ubicacionNeo4JDAO.deleteById(ubicacionId);
        ubicacionMongoDAO.deleteById(String.valueOf(ubicacionId));
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        var iterable = ubicacionSQLDAO.findAll();
        List<UbicacionSQL> ubicacionesSQL = StreamSupport.stream(iterable.spliterator(), false).toList();
        return ubicacionesSQL.stream().map(ubicacionSQL -> {
            if (ubicacionSQL instanceof SantuarioSQL) {
                return Santuario.from(ubicacionSQL);
            } else {
                return Cementerio.from(ubicacionSQL);
            }
        }).toList();
    }

    @Override
    public void conectar(Long idOrigen, Long idDestino, Long costo) {
        //validar que existen
        ubicacionNeo4JDAO.findById(idOrigen)
                .orElseThrow(() -> new UbicacionNoEncontradaException("origen"));
        ubicacionNeo4JDAO.findById(idDestino)
                .orElseThrow(() -> new UbicacionNoEncontradaException("destino"));
        Integer costoAInt = Math.toIntExact(costo);
        ubicacionNeo4JDAO.conectar(idOrigen, idDestino, costoAInt);
    }

    public Boolean estanConectadas(Long idOrigen, Long idDestino) {
        ubicacionNeo4JDAO.findById(idOrigen)
                .orElseThrow(() -> new UbicacionNoEncontradaException("origen"));
        ubicacionNeo4JDAO.findById(idDestino)
                .orElseThrow(() -> new UbicacionNoEncontradaException("destino"));
        return ubicacionNeo4JDAO.estanConectadasDirecto(idOrigen, idDestino);
    }

    @Override
    public void eliminarTodo() {
        ubicacionSQLDAO.deleteAll();
        ubicacionNeo4JDAO.deleteAll();
        ubicacionMongoDAO.deleteAll();
    }

    private List<Ubicacion> findPath(Long idOrigen, Long idDestino, BiFunction<Long, Long, List<UbicacionNeo4J>> finder) {
        return finder.apply(idOrigen, idDestino)
                .stream()
                .map(UbicacionNeo4J::toModel)
                .toList();
    }

    @Override
    public List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino) {
        return findPath(idOrigen, idDestino, ubicacionNeo4JDAO::findShortestPath);
    }

    @Override
    public List<Ubicacion> caminoMasRentable(Long idOrigen, Long idDestino) {
        return findPath(idOrigen, idDestino, ubicacionNeo4JDAO::findMostRentablePath);
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia) {
        List<UbicacionSQL> ubicaciones = ubicacionSQLDAO.ubicacionesSobrecargadas(umbralDeEnergia);
        return ubicaciones.stream()
                .map(ubicacionSQL -> {
                    UbicacionNeo4J ubiNeo = ubicacionNeo4JDAO.findById(ubicacionSQL.getId())
                            .orElseThrow(() -> new UbicacionNoEncontradaException("ubicacion"));
                    UbicacionMongo ubiMongo = ubicacionMongoDAO.findById(String.valueOf(ubicacionSQL.getId()))
                            .orElseThrow(() -> new UbicacionNoEncontradaException("ubicacion"));
                    if (ubicacionSQL instanceof SantuarioSQL) {
                        return Santuario.from(ubicacionSQL, ubiNeo, ubiMongo);
                    } else {
                        return Cementerio.from(ubicacionSQL, ubiNeo, ubiMongo);
                    }
                })
                .toList();
    }

    public Map<String, Object> toGeoJsonPoint(Coordenada coord) {
        return Map.of(
                "type", "Point",
                "coordinates", List.of(coord.getLongitud(), coord.getLatitud()) // lon, lat
        );
    }

    @Override
    public boolean estaDentroDe(Long ubicacionId, Coordenada coord) {
        Map<String, Object> point = toGeoJsonPoint(coord);

        List<UbicacionMongo> resultado = ubicacionMongoDAO.findUbicacionesQueContienen(point);
        return resultado.stream()
                .anyMatch(u -> u.getId().equals(String.valueOf(ubicacionId)));
    }
}
