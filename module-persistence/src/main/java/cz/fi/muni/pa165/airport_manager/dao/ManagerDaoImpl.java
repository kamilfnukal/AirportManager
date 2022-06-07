package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Matej Michálek
 */

@Repository
@Transactional
public class ManagerDaoImpl implements ManagerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Manager manager) {
        em.persist(manager);
    }

    @Override
    public List<Manager> findAll() {
        return em.createQuery("SELECT m FROM Manager m", Manager.class).getResultList();
    }

    @Override
    public Manager findById(Long id) {
        return em.find(Manager.class, id);
    }

    @Override
    public Manager findByEmail(String email) {
        return em.createQuery("SELECT manager FROM Manager manager where manager.email = :email", Manager.class)
                .setParameter("email", email).getSingleResult();
    }

    @Override
    public Manager findManagerOfFlight(Long flightId) {
        try {
            return em.createQuery("SELECT manager FROM Manager manager INNER JOIN manager.flights f ON f.id = :flightId", Manager.class)
                    .setParameter("flightId", flightId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Manager manager) {
        if (findById(manager.getId()) == null) throw new ObjectNotFoundException(Manager.class, manager.getId());

        em.merge(manager);
    }

    @Override
    public void remove(Long managerId) {
        var managerToDelete = findById(managerId);

        if (managerToDelete == null) throw new ObjectNotFoundException(Manager.class, managerId);

        em.remove(managerToDelete);
    }

}
