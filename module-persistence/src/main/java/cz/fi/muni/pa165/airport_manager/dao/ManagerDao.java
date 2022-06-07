package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

/**
 * @author Matej Michálek
 */

public interface ManagerDao {
    /**
     * Creates a manager of airport.
     * @param manager manager to create
     * @throws InvalidDataAccessApiUsageException when manager is null
     */
    void create(Manager manager);

    /**
     * Finds all managers
     * @return list of all users
     */
    List<Manager> findAll();

    /**
     * Finds a manager with given id
     * @param id id of manager
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return manager if exists, otherwise null
     */
    Manager findById(Long id);

    /**
     * Finds a manager with given email
     * @param email email of manager
     * @throws InvalidDataAccessApiUsageException when email is null
     * @return manager if exists, otherwise null
     */
    Manager findByEmail(String email);

    /**
     * Finds a manager who created flight with given id
     * @param flightId id of flight
     * @throws InvalidDataAccessApiUsageException when flightId is null
     * @return manager if exists, otherwise null
     */
    Manager findManagerOfFlight(Long flightId);


    /**
     * Updates an existing manager
     * @param manager manager to update
     * @throws ObjectNotFoundException when manager doesn't exist
     * @throws NullPointerException when manager is null
     */
    void update(Manager manager);

    /**
     * Deletes an manager with given id
     * @param managerId id of manager to delete
     * @throws ObjectNotFoundException when manager with given id doesn't exist
     */
    void remove(Long managerId);

}
