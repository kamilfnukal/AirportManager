package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Martin Kalina
 * This class represents service methods used for operations with flight entity.
 */

public interface FlightService {

    /**
     * Creates a flight.
     * @param flight flight to create
     * @param email email of a user who is creating this flight
     * @throws AirportManagerServiceException flight is null
     */
    void createFlight(Flight flight, String email);

    /**
     * Finds all flights
     * @return list of all flights
     */
    List<Flight> findAllFlights();

    /**
     * Finds a flight with given id
     * @param id id of flight
     * @throws AirportManagerServiceException if id null
     * @return flight if exists, otherwise null
     */
    Flight findFlightById(Long id);

    /**
     * Updates an existing flight
     * @param flight flight to update
     * @throws AirportManagerServiceException flight is null
     */
    void updateFlight(Flight flight);

    /**
     * Deletes an existing flight.
     * Firstly, it removes reference from manager who created this flight.
     * Finally, it deletes given flight.
     * @param flight flight to delete
     * @throws AirportManagerServiceException flight is null
     */
    void removeFlight(Flight flight);

    /**
     * This method is used to get departure flights based on the origin airport
     * @param origin airport
     * @throws AirportManagerServiceException origin is null
     */
    List<Flight> getDepartureFlights(Airport origin);

    /**
     * This method is used to get arrival flights based on the destination airport
     * @param destination airport
     * @throws AirportManagerServiceException destination is null
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