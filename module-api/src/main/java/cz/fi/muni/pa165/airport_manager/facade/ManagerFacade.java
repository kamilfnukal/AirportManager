package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.ManagerDto;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents facade API used for operations with manager entity.
 */

public interface ManagerFacade {

    /**
     * Creates a manager
     * @param manager manager to create
     * @throws IllegalArgumentException manager is null
     */
    void createManager(ManagerDto manager);

    /**
     * Finds all managers
     * @return list of all managers
     */
    List<ManagerDto> findAllManagers();

    /**
     * Finds a manager with given id
     * @param id id of manager
     * @throws IllegalArgumentException id is null
     * @return manager if exists, otherwise null
     */
    ManagerDto findManagerById(Long id);

    /**
     * Finds a manager with given email
     * @param email email of manager
     * @throws IllegalArgumentException when email is null
     * @return manager if exists, otherwise null
     */
    ManagerDto findByEmail(String email);

    /**
     * Updates an existing manager
     * @param manager manager to update
     * @throws IllegalArgumentException manager is null
     */
    void updateManager(ManagerDto manager);

    /**
     * Deletes a manager with given id
     * @param managerId id of manager to delete
     */
    void removeManager(Long managerId);

}
