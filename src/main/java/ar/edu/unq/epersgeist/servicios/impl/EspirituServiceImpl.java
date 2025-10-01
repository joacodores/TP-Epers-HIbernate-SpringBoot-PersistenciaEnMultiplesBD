package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return espirituDAO.save(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituDAO.deleteById(espirituId);
    }

    @Override
    public Optional<Espiritu> recuperar(Long ubicacionId) {
        return espirituDAO.findById(ubicacionId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        Iterable<Espiritu> iterable = espirituDAO.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        Optional<Espiritu> espirituAActualizar = espirituDAO.findById(espiritu.getId());
        if (espiritu.getNombre() != null) {
            espirituAActualizar.get().setNombre(espiritu.getNombre());
        }
        espirituDAO.save(espirituAActualizar.get());
    }

    @Override
    public void eliminarTodo() {
        espirituDAO.deleteAll();
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Sort.Direction direccion, Integer pagina, Integer cantidadPorPagina) {
        Pageable pageable = PageRequest.of(
                pagina - 1,
                cantidadPorPagina,
                Sort.by(direccion, "nivelDeConexion")
        );
        return espirituDAO.espiritusDemoniacos(pageable).getContent();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = espirituDAO.recuperar(espirituId);
        Medium medium = mediumDAO.recuperar(mediumId);
        medium.conectarseAEspiritu(espiritu);
        espirituDAO.save(espiritu);
        return medium;
    }
}
