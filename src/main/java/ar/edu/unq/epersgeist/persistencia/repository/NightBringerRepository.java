package ar.edu.unq.epersgeist.persistencia.repository;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.NightBringer;

import java.util.List;

public interface NightBringerRepository {

    NightBringer crear(NightBringer nightBringer);
    NightBringer recuperar(Long nightBringerId);
    List<NightBringer> recuperarTodos();
    void actualizar(NightBringer nightBringer);
    void eliminar(Long nightBringerId);
}
