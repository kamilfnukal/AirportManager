package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Martin Kalina
 */

@Repository
@Transactional
public class AirplaneDaoImpl implements AirplaneDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Airplane airplane) {
        em.persist(airplane);
    }

    @Override
    public List<Airplane> findAll() {
        return em.createQuery("SELECT a FROM Airplane a", Airplane.class).getResultList();
    }

    @Override
    public Airplane findById(Long id) {
        return em.find(Airplane.class, id);
    }

    @Override
    public void update(Airplane airplane) {
        if (findById(airplane.getId()) == null) throw new ObjectNotFoundException(Airplane.class, airplane.getId());
        em.merge(airplane);
    }

    @Override
    public void remove(Long airplaneId) {
        var planeToDelete = findById(airplaneId);

        if (planeToDelete == null) throw new ObjectNotFoundException(Airplane.class, airplaneId);

        em.remove(planeToDelete);
    }

}
