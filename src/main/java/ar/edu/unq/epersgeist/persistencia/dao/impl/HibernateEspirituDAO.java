package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateSessionContext;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateEspirituDAO extends HibernateDAO<Espiritu> implements EspirituDAO {
    public HibernateEspirituDAO() {
        super(Espiritu.class);
    }


    public Espiritu crear(Espiritu espiritu) {
        super.guardar(espiritu);
        return espiritu;
    }


    public List<Espiritu> recuperarTodos() {
        Session session = HibernateSessionContext.getCurrentSession();
        String hql = "select e from Espiritu e";
        Query<Espiritu> query = session.createQuery(hql, Espiritu.class);
        return query.getResultList();
    }


}
