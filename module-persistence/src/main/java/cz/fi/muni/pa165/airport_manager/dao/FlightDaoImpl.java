package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Sklenář
 */

@Repository
@Transactional
public class FlightDaoImpl implements FlightDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Flight flight) {
        em.persist(flight);
    }

    @Override
    public List<Flight> findAll() {
        return em.createQuery("SELECT f FROM Flight f", Flight.class).getResultList();
    }

    @Override
    public Flight findById(Long id) {
        return em.find(Flight.class, id);
    }

    @Override
    public void update(Flight flight) {
        if (findById(flight.getId()) == null) throw new ObjectNotFoundException(Flight.class, flight.getId());
        em.merge(flight);
    }

    @Override
    public void remove(Flight flight) {
        var flightToDelete = findById(flight.getId());

        if (flightToDelete == null) throw new ObjectNotFoundException(Flight.class, flight.getId());

        em.remove(flightToDelete);
    }

    @Override
    public List<Flight> getDepartureFlights(Airport origin) {
        return em.createQuery("SELECT flights FROM Flight flights where flights.origin = :origin", Flight.class)
                .setParameter("origin", origin)
                .getResultList();
    }

    @Override
    public List<Flight> getArrivalFlights(Airport destination) {
        return em.createQuery("SELECT flights FROM Flight flights where flights.destination = :destination", Flight.class)
                .setParameter("destination", destination)
                .getResultList();
    }

    @Override
    public List<Flight> getFlightsByAirplane(Airplane airplane) {
        return em.createQuery("SELECT flights FROM Flight flights where flights.airplane = :airplane", Flight.class)
                .setParameter("airplane", airplane)
                .getResultList();    }

    @Override
    public List<Flight> getFlightsForPeriod(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("SELECT flights FROM Flight flights " +
                        "where flights.departureTime between :from and :to " +
                        "or flights.arrivalTime between :from and :to", Flight.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

}
