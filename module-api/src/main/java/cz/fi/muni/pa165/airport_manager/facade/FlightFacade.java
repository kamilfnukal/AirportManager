package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;
import cz.fi.muni.pa165.airport_manager.dto.AirportDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import cz.fi.muni.pa165.airport_manager.dto.FlightDto;

import java.util.List;

/**
 * @author Martin Kalina
 * This class represents facade API used for operations with flight entity.
 */

public interface FlightFacade {

    /**
     * Creates a flight.
     * @param flight flight to create
     * @param email email of a user who is creating this flight
     * @throws IllegalArgumentException flight is null
     */
    void createFlight(FlightCreateDto flight, String email);

    /**
     * Finds all flights sorted by departureTime
     * @throws IllegalArgumentException flight is null
     * @return list of all flights
     */
    List<FlightDto> findAllFlights();

    /**
     * Finds a flight with given id
     * @param id id of flight
     * @throws IllegalArgumentException flight is null
     * @return flight if exists, otherwise null
     */
    FlightDto findFlightById(Long id);

    /**
     * Updates an existing flight
     * @param flight flight to update
     * @throws IllegalArgumentException flight is null
     */
    void updateFlight(FlightCreateDto flight);

    /**
     * Deletes an existing flight
     * @param flight flight to delete
     * @throws IllegalArgumentException flight is null
     */
    void removeFlight(FlightDto flight);

    /**
     * This method is used to get departure flights based on the origin airport
     * @param origin airportDto
     * @throws IllegalArgumentException origin is null
     */
    List<FlightDto> getDepartureFlights(AirportDto origin);

    /**
     * This method is used to get arrival flights based on the destination airport
     * @param destination airportDto
     * @throws IllegalArgumentException destination is null
     */
    List<FlightDto> getArrivalFlights(AirportDto destination);

    /**
     * This method is used to get flights based on the airplane
     * @param airplane airplane
     * @throws IllegalArgumentException airplane is null
     */
    List<FlightDto> getFlightsByAirplane(AirplaneDto airplane);

}