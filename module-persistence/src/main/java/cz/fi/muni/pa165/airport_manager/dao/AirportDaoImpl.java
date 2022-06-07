package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Kamil Fňukal
 */

@Repository
@Transactional
public class AirportDaoImpl implements AirportDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Airport airport) {
        em.persist(airport);
    }

    @Override
    public List<Airport> findAll() {
        return em.createQuery("select a from Airport a", Airport.class).getResultList();
    }

    @Override
    public Airport findById(Long id) {
        return em.find(Airport.class, id);
    }

    @Override
    public void update(Airport airport) {
        if (findById(airport.getId()) == null) throw new ObjectNotFoundException(Airport.class, airport.getId());
        em.merge(airport);
    }

    @Override
    public void remove(Long airportId) {
        var airportToDelete = findById(airportId);
        if (airportToDelete == null) throw new ObjectNotFoundException(Airport.class, airportId);
        em.remove(airportToDelete);
    }

}
