package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.List;

/**
 * @author Martin Kalina
 */

public interface AirplaneDao {
    /**
     * Creates an airplane.
     * @param airplane airplane to create
     * @throws InvalidDataAccessApiUsageException when airplane is null
     * @throws JpaSystemException when airplane with given serialNumber already exists
     */
    void create(Airplane airplane);

    /**
     * Finds all airplanes
     * @return list of all airplanes
     */
    List<Airplane> findAll();

    /**
     * Finds an airplane with given id
     * @param id id of airplane
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return airplane if exists, otherwise null
     */
    Airplane findById(Long id);

    /**
     * Updates an existing airplane
     * @param airplane airplane to update
     * @throws ObjectNotFoundException when airplane doesn't exist
     * @throws NullPointerException when airplane is null
     * @throws JpaSystemException when airplane with updated serialNumber already exists
     */
    void update(Airplane airplane);

    /**
     * Deletes an airplane with given id
     * @param airplaneId id of airplane to delete
     * @throws ObjectNotFoundException when airplane with given id doesn't exist
     */
    void remove(Long airplaneId);

}
