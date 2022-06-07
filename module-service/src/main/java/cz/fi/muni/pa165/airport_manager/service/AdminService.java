package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents service methods used for operations with admin entity.
 */

public interface AdminService {

    /**
     * Creates an admin
     * @param admin admin to create
     * @throws AirportManagerServiceException admin is null
     */
    void createAdmin(Admin admin);

    /**
     * Finds all admins
     * @return list of all admins
     */
    List<Admin> findAllAdmins();

    /**
     * Finds an admin with given id
     * @param id id of admin
     * @throws AirportManagerServiceException id is null
     * @return admin if exists, otherwise null
     */
    Admin findAdminById(Long id);

    /**
     * Finds an admin with given email
     * @param email email of admin
     * @throws AirportManagerServiceException when email is null
     * @return admin if exists, otherwise null
     */
    Admin findByEmail(String email);

    /**
     * Updates an existing admin
     * @param admin admin to update
     * @throws AirportManagerServiceException admin is null
     */
    void updateAdmin(Admin admin);

    /**
     * Deletes an admin with given id
     * @param adminId id of admin to delete
     * @throws AirportManagerServiceException adminId is null
     */
    void removeAdmin(Long adminId);

}