package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateSessionContext;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {

    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        super.guardar(ubicacion);
        return ubicacion;
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        Session session = HibernateSessionContext.getCurrentSession();
        String hql = "select u from Ubicacion u order by u.nombre asc";
        Query<Ubicacion> query = session.createQuery(hql, Ubicacion.class);
        return query.getResultList();
    }
}
