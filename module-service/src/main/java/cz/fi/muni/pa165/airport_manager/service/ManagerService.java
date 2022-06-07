package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents service methods used for operations with manager entity.
 */

public interface ManagerService {

    /**
     * Creates n manager
     * @param manager manager to create
     * @throws AirportManagerServiceException manager is null
     */
    void createManager(Manager manager);

    /**
     * Finds all managers
     * @return list of all managers
     */
    List<Manager> findAllManagers();

    /**
     * Finds a manager with given id
     * @param id id of manager
     * @throws AirportManagerServiceException id is null
     * @return manager if exists, otherwise null
     */
    Manager findManagerById(Long id);

    /**
     * Finds a manager with given email
     * @param email email of manager
     * @throws AirportManagerServiceException when email is null
     * @return manager if exists, otherwise null
     */
    Manager findByEmail(String email);

    /**
     * Updates an existing manager
     * @param manager manager to update
     * @throws AirportManagerServiceException manager is null
     */
    void updateManager(Manager manager);

    /**
     * Deletes a manager with given id
     * @param managerId id of manager to delete
     * @throws AirportManagerServiceException managerId is null
     */
    void removeManager(Long managerId);

}