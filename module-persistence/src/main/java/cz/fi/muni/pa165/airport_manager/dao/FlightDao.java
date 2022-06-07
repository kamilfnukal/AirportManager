package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Sklenář
 */

public interface FlightDao {

    /**
     * Creates a flight.
     * @param flight flight to create
     * @throws InvalidDataAccessApiUsageException when flight is null
     */
    void create(Flight flight);

    /**
     * Finds all flights
     * @return list of all flights
     */
    List<Flight> findAll();

    /**
     * Finds a flight with given id
     * @param id id of flight
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return flight if exists, otherwise null
     */
    Flight findById(Long id);

    /**
     * Updates an existing flight
     * @param flight flight to update
     * @throws NullPointerException when flight is null
     * @throws ObjectNotFoundException when flight doesn't exist
     */
    void update(Flight flight);

    /**
     * Deletes an existing flight
     * @param flight flight to delete
     * @throws NullPointerException when flight is null
     * @throws ObjectNotFoundException when flight doesn't exist
     */
    void remove(Flight flight);

    /**
     * This method is used to get departure flights based on the origin airport
     * @param origin airport
     */
    List<Flight> getDepartureFlights(Airport origin);

    /**
     * This method is used to get arrival flights based on the destination airport
     * @param destination airport
     */
    List<Flight> getArrivalFlights(Airport destination);

    /**
     * This method is used to get flights based on the airplane
     * @param airplane airplane
     */
    List<Flight> getFlightsByAirplane(Airplane airplane);

    /**
     * This method is used to get flights for a given period
     * @param from beginning of period
     * @param to end of period
     * @return list of all flights in given period
     */
    List<Flight> getFlightsForPeriod(LocalDateTime from, LocalDateTime to);

}
