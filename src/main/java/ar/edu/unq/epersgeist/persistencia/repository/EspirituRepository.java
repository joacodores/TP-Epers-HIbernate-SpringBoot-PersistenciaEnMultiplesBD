package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.ReporteSantuarioMasCorruptoProjection;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface EspirituRepository {
    Espiritu crear(Espiritu espiritu);

    Optional<Espiritu> recuperar(Long espirituId);

    List<Espiritu> recuperarTodos();

    List<Espiritu> espiritusDemoniacos(Sort.Direction direccion, Integer pagina, Integer cantidadPorPagina);

    void actualizar(Espiritu espiritu);

    void eliminar(Long espirituId);

    void eliminarTodo();

    List<Espiritu> espiritusEn(Long ubicacionId);

    List<ReporteSantuarioMasCorruptoProjection> obtenerReporteSantuarioMasCorrupto();
}
