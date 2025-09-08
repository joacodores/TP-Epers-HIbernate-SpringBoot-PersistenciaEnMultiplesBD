package ar.edu.unq.epersgeist.persistencia.dao.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateSessionContext;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateMediumDAO extends HibernateDAO<Medium> implements MediumDAO {

    public HibernateMediumDAO() {
        super(Medium.class);
    }

    public Medium crear(Medium medium) {
        super.guardar(medium);
        return medium;
    }

    public List<Medium> recuperarTodos() {
        Session session = HibernateSessionContext.getCurrentSession();
        String hql = "select m from Medium m";
        Query<Medium> query = session.createQuery(hql, Medium.class);
        return query.getResultList();
    }

    public void descansar(Long mediumId) {
        Session session = HibernateSessionContext.getCurrentSession();
        String hql = "select m from Medium m where m.id = :mediumId";
        Medium medium = session.createQuery(hql, Medium.class).setParameter("mediumId", mediumId).getSingleResult();
        medium.aumentarMana(15);
        medium.recuperar_PuntosDeConexionATodosLosEspiritus(5);
    }
}
