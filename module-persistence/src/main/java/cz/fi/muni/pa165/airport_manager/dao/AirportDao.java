package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

/**
 * @author Kamil Fňukal
 */

public interface AirportDao {

    /**
     * Creates an airport.
     * @param airport airport to create
     * @throws InvalidDataAccessApiUsageException when airport is null
     */
    void create(Airport airport);

    /**
     * Finds all airports
     * @return list of all airports
     */
    List<Airport> findAll();

    /**
     * Finds a airport with given id
     * @param id id of airport
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return airport if exists, otherwise null
     */
    Airport findById(Long id);

    /**
     * Updates an existing airport
     * @param airport airport to update
     * @throws ObjectNotFoundException when airport doesn't exist
     * @throws NullPointerException when airport is null
     */
    void update(Airport airport);

    /**
     * Deletes an airport with given id
     * @param airportId id of airport to delete
     * @throws ObjectNotFoundException when airport with given id doesn't exist
     */
    void remove(Long airportId);

}
