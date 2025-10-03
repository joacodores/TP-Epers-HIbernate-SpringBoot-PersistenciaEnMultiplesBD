package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.exceptions.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.controller.exceptions.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;

    public MediumServiceImpl(MediumDAO medium, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO) {
        this.mediumDAO = medium;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public Medium crear(Medium medium) {
        return mediumDAO.save(medium);
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return mediumDAO.findById(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {
        Iterable<Medium> iterable = mediumDAO.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void actualizar(Medium medium) {
        Medium mediumExistente = mediumDAO.findById(medium.getId())
                .orElseThrow(() -> new MediumNoEncontradoException(""));
        Optional.ofNullable(medium.getNombre()).ifPresent(mediumExistente::setNombre);
        Optional.ofNullable(medium.getManaMax()).ifPresent(mediumExistente::setManaMax);
        Optional.ofNullable(medium.getMana()).ifPresent(mediumExistente::setMana);
        mediumDAO.save(mediumExistente);
    }

    @Override
    public void eliminar(Long mediumId) {
        mediumDAO.deleteById(mediumId);
    }

    @Override
    public void eliminarTodo() {
        //checkear que los espiritus se desvinculan con medium
        mediumDAO.deleteAll();
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium exorcista = mediumDAO.findById(idMediumExorcista).orElseThrow(() -> new MediumNoEncontradoException("exorcista"));
        Medium mediumAExorcizar = mediumDAO.findById(idMediumAExorcizar).orElseThrow(() -> new MediumNoEncontradoException("a exorcizar"));
        if (exorcista.getUbicacion().equals(mediumAExorcizar.getUbicacion())) {
            exorcista.exorcizar(mediumAExorcizar);
            mediumDAO.save(exorcista);
            mediumDAO.save(mediumAExorcizar);
        }
    }

    @Override
    public void descansar(Long mediumId) {
        Medium mediumADescansar = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        mediumADescansar.descansar();
        mediumDAO.save(mediumADescansar);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Medium invocador = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException("invocador"));
        Espiritu espirituAInvocar = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException("a invocar"));
        Ubicacion ubiDeInvocacion = invocador.getUbicacion();
        invocador.invocar(espirituAInvocar);
        mediumDAO.save(invocador);
        espirituDAO.save(espirituAInvocar);
        ubicacionDAO.save(ubiDeInvocacion);
        return espirituAInvocar;
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        return medium.getEspiritus();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(""));
        Ubicacion ubicacion = ubicacionDAO.findById(ubicacionId).orElseThrow(() -> new UbicacionNoEncontradaException(""));
        medium.mover(ubicacion);
        mediumDAO.save(medium);
    }

}
