package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

/**
 * @author Pavel Sklenar
 */

public interface AdminDao {
    /**
     * Creates an admin.
     * @param admin admin to create
     * @throws InvalidDataAccessApiUsageException when admin is null
     */
    void create(Admin admin);

    /**
     * Finds all managers
     * @return list of all users
     */
    List<Admin> findAll();

    /**
     * Finds a admin with given id
     * @param id id of admin
     * @throws InvalidDataAccessApiUsageException when id is null
     * @return admin if exists, otherwise null
     */
    Admin findById(Long id);

    /**
     * Finds a admin with given email
     * @param email email of admin
     * @throws InvalidDataAccessApiUsageException when email is null
     * @return admin if exists, otherwise null
     */
    Admin findByEmail(String email);

    /**
     * Finds an admin who created airplane with given id
     * @param airplaneId id of airplane
     * @throws InvalidDataAccessApiUsageException when airplaneId is null
     * @return admin if exists, otherwise null
     */
    Admin findAdminOfAirplane(Long airplaneId);

    /**
     * Finds an admin who created steward with given id
     * @param stewardId id of steward
     * @throws InvalidDataAccessApiUsageException when steward is null
     * @return admin if exists, otherwise null
     */
    Admin findAdminOfSteward(Long stewardId);

    /**
     * Finds an admin who created airport with given id
     * @param airportId id of airport
     * @throws InvalidDataAccessApiUsageException when airportId is null
     * @return admin if exists, otherwise null
     */
    Admin findAdminOfAirport(Long airportId);

    /**
     * Updates an existing admin
     * @param admin admin to update
     * @throws ObjectNotFoundException when admin doesn't exist
     * @throws NullPointerException when admin is null
     */
    void update(Admin admin);

    /**
     * Deletes an admin with given id
     * @param adminId id of admin to delete
     * @throws ObjectNotFoundException when admin with given id doesn't exist
     */
    void remove(Long adminId);

}
