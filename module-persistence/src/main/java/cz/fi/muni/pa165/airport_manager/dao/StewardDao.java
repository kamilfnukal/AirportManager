package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

/**
 * @author Pavel Sklenář
 */

public interface StewardDao {

    /**
     * Creates a steward of airport.
     * @param steward steward to create
     * @throws InvalidDataAccessApiUsageException when steward is null
     */
    void create(Steward steward);

    /**
     * Finds all stewards
     * @return list of all stewards
     */
    List<Steward> findAll();

    /**
     * Finds a steward with given id
     * @param id id of steward
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return steward if exists, otherwise null
     */
    Steward findById(Long id);

    /**
     * Updates an existing steward
     * @param steward steward to update
     * @throws ObjectNotFoundException when steward doesn't exist
     * @throws NullPointerException when steward is null
     */
    void update(Steward steward);

    /**
     * Deletes a steward with given id
     * @param stewardId id of steward to delete
     * @throws ObjectNotFoundException when steward with given id doesn't exist
     */
    void remove(Long stewardId);

}
