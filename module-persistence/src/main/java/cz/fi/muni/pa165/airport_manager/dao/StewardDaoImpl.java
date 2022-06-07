package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Pavel Sklenář
 */

@Repository
@Transactional
public class StewardDaoImpl implements StewardDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Steward steward) {
        em.persist(steward);
    }

    @Override
    public List<Steward> findAll() {
        return em.createQuery("SELECT s FROM Steward s", Steward.class).getResultList();
    }

    @Override
    public Steward findById(Long id) {
        return em.find(Steward.class, id);
    }

    @Override
    public void update(Steward steward) {
        if (findById(steward.getId()) == null) throw new ObjectNotFoundException(Steward.class, steward.getId());

        em.merge(steward);
    }

    @Override
    public void remove(Long stewardId) {
        var stewardToDelete = findById(stewardId);

        if (stewardToDelete == null) throw new ObjectNotFoundException(Steward.class, stewardId);

        em.remove(stewardToDelete);
    }

}
