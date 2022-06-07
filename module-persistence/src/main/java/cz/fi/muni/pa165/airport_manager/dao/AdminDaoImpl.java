package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Pavel Sklenar
 */

@Repository
@Transactional
public class AdminDaoImpl implements AdminDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Admin admin) {
        em.persist(admin);
    }

    @Override
    public List<Admin> findAll() {
        return em.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }

    @Override
    public Admin findById(Long id) {
        return em.find(Admin.class, id);
    }

    @Override
    public Admin findByEmail(String email) {
        return em.createQuery("SELECT admin FROM Admin admin where admin.email = :email", Admin.class)
                .setParameter("email", email).getSingleResult();
    }

    @Override
    public Admin findAdminOfAirplane(Long airplaneId) {
        try {
            return em.createQuery("SELECT admin FROM Admin admin INNER JOIN admin.airplanes a ON a.id = :airplaneId", Admin.class)
                .setParameter("airplaneId", airplaneId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Admin findAdminOfSteward(Long stewardId) {
        try {
            return em.createQuery("SELECT admin FROM Admin admin INNER JOIN admin.stewards s ON s.id = :stewardId", Admin.class)
                    .setParameter("stewardId", stewardId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Admin findAdminOfAirport(Long airportId) {
        try {
            return em.createQuery("SELECT admin FROM Admin admin INNER JOIN admin.airports a ON a.id = :airportId", Admin.class)
                    .setParameter("airportId", airportId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Admin admin) {
        if (findById(admin.getId()) == null) throw new ObjectNotFoundException(Admin.class, admin.getId());

        em.merge(admin);
    }

    @Override
    public void remove(Long adminId) {
        var adminToDelete = findById(adminId);

        if (adminToDelete == null) throw new ObjectNotFoundException(Admin.class, adminId);

        em.remove(adminToDelete);
    }

}
