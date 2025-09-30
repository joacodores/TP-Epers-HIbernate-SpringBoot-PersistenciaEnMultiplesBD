package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;

import ar.edu.unq.epersgeist.servicios.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;
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
        Optional<Medium> mediumAActualizar = mediumDAO.findById(medium.getId());

        if (medium.getNombre() != null) { mediumAActualizar.get().setNombre(medium.getNombre());}
        if (medium.getManaMax() != null) { mediumAActualizar.get().setManaMax(medium.getManaMax());}
        if (medium.getMana() != null) { mediumAActualizar.get().setMana(medium.getMana());}

        mediumDAO.save(mediumAActualizar.get());
    }

    @Override
    public void eliminar(Medium medium) {

        //List<Espiritu> espiritusDeMedium = this.espiritus(medium.getId());
        //checkear que los espiritus se desvinculan con medium
        mediumDAO.delete(medium);
    }

    @Override
    public void eliminarTodo() {
        //checkear que los espiritus se desvinculan con medium
        mediumDAO.deleteAll();

    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium exorcista = mediumDAO.findById(idMediumExorcista).orElseThrow(() -> new NoSuchElementException("Medium not found with id: " + idMediumExorcista));
        Medium mediumAExorcizar = mediumDAO.findById(idMediumAExorcizar).orElseThrow(() -> new NoSuchElementException("Medium not found with id: " + idMediumAExorcizar));
        exorcista.exorcizar(mediumAExorcizar);
        mediumDAO.save(exorcista);
        mediumDAO.save(mediumAExorcizar);
    }

    @Override
    public void descansar(Long mediumId) {
        Medium mediumADescansar = mediumDAO.findById(mediumId).orElseThrow(() -> new NoSuchElementException("Medium not found with id: " + mediumId));
        mediumADescansar.descansar();
        mediumDAO.save(mediumADescansar);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Medium invocador = mediumDAO.findById(mediumId).orElseThrow(() -> new NoSuchElementException("Medium not found with id: " + mediumId));
        Espiritu espirituAInvocar = espirituDAO.findById(espirituId).orElseThrow(() -> new NoSuchElementException("Espiritu not found with id: " + espirituId));
        Ubicacion ubiDeInvocacion = invocador.getUbicacion();
        invocador.invocar(espirituAInvocar);
        mediumDAO.save(invocador);
        espirituDAO.save(espirituAInvocar);
        ubicacionDAO.save(ubiDeInvocacion);
        return espirituAInvocar;
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new NoSuchElementException("Medium not found with id: " + mediumId));
        return medium.getEspiritus();
    }

}
