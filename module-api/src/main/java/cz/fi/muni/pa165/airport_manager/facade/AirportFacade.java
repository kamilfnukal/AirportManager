package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirportDto;

import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents facade API used for operations with airport entity.
 */

public interface AirportFacade {

    /**
     * Creates an airport.
     * @param airport airport to create
     * @param email email of a user who is creating this airport
     * @throws IllegalArgumentException airport is null, or email is null
     */
    void createAirport(AirportDto airport, String email);

    /**
     * Finds all airports
     * @return list of all airports
     */
    List<AirportDto> findAllAirports();

    /**
     * Finds an airport with given id
     * @param id id of airport
     * @throws IllegalArgumentException id is null
     * @return airport if exists, otherwise null
     */
    AirportDto findAirportById(Long id);

    /**
     * Updates an existing airport
     * @param airport airport to update
     * @throws IllegalArgumentException airport is null
     */
    void updateAirport(AirportDto airport);

    /**
     * Deletes an airport with given id
     * @param airportId id of airport to delete
     */
    void removeAirport(Long airportId);

}
