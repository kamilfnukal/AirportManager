package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents service methods used for operations with airport entity.
 */

public interface AirportService {

    /**
     * Creates an airport.
     * @param airport airport to create
     * @param email email of a user who is creating this airport
     * @throws AirportManagerServiceException airport is null, or email is null, or when airport with given IATA code already exists
     */
    void createAirport(Airport airport, String email);

    /**
     * Finds all airports
     * @return list of all airports
     */
    List<Airport> findAllAirports();

    /**
     * Finds an airport with given id
     * @param id id of airport
     * @throws AirportManagerServiceException id is null
     * @return airport if exists, otherwise null
     */
    Airport findAirportById(Long id);

    /**
     * Updates an existing airport
     * @param airport airport to update
     * @throws AirportManagerServiceException airport is null, or when airport with given IATA code already exists
     */
    void updateAirport(Airport airport);

    /**
     * Deletes an airport with given id
     * Firstly, it removes reference from admin who created this airport.
     * Secondly, it deletes flights which correspond to this airport.
     * Finally, it deletes an airport.
     * @param airportId id of airport to delete
     * @throws AirportManagerServiceException airportId is null
     */
    void removeAirport(Long airportId);

}