package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.FlightDao;
import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Martin Kalina
 * This class represents service layer for Flight entity.
 */

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Autowired
    private ManagerDao managerDao;

    @Override
    public void createFlight(Flight flight, String email) {
        if (flight == null)
            throw new AirportManagerServiceException("Flight is null.");
        if(email == null)
            throw new IllegalArgumentException("Email is null");
        flightDao.create(flight);

        var manager = managerDao.findByEmail(email);
        manager.addFlight(flight);
        managerDao.update(manager);
    }

    @Override
    public List<Flight> findAllFlights() {
        return flightDao.findAll();
    }

    @Override
    public Flight findFlightById(Long id) {
        if (id == null)
            throw new AirportManagerServiceException("ID is null.");
        return flightDao.findById(id);
    }

    @Override
    public void updateFlight(Flight flight) {
        if (flight == null)
            throw new AirportManagerServiceException("Flight is null.");
        try {
            flightDao.update(flight);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException("Flight to update could not be found.", e);
        }
    }

    @Override
    public void removeFlight(Flight flight) {
        if (flight == null)
            throw new AirportManagerServiceException("Flight is null.");
        try {
            var manager = managerDao.findManagerOfFlight(flight.getId());
            manager.removeFlight(flight);
            managerDao.update(manager);

            flightDao.remove(flight);
        } catch (ObjectNotFoundException e) {
            throw new AirportManagerServiceException("Flight to remove could not be found.", e);
        }
    }

    @Override
    public List<Flight> getDepartureFlights(Airport origin) {
        if (origin == null)
            throw new AirportManagerServiceException("Origin is null.");
        return flightDao.getDepartureFlights(origin);
    }

    @Override
    public List<Flight> getArrivalFlights(Airport destination) {
        if (destination == null)
            throw new AirportManagerServiceException("Destination is null.");
        return flightDao.getArrivalFlights(destination);
    }

    @Override
    public List<Flight> getFlightsByAirplane(Airplane airplane) {
        if (airplane == null)
            throw new AirportManagerServiceException("Airplane is null.");
        return flightDao.getFlightsByAirplane(airplane);
    }

    @Override
    public List<Flight> getFlightsForPeriod(LocalDateTime from, LocalDateTime to) {
        return flightDao.getFlightsForPeriod(from, to);
    }

}
