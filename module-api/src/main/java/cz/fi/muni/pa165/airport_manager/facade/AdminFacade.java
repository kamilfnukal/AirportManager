package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AdminDto;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents facade API used for operations with admin entity.
 */

public interface AdminFacade {

    /**
     * Creates an admin
     * @param admin admin to create
     * @throws IllegalArgumentException admin is null
     */
    void createAdmin(AdminDto admin);

    /**
     * Finds all admins
     * @return list of all admins
     */
    List<AdminDto> findAllAdmins();

    /**
     * Finds an admin with given id
     * @param id id of admin
     * @throws IllegalArgumentException id is null
     * @return admin if exists, otherwise null
     */
    AdminDto findAdminById(Long id);

    /**
     * Finds a admin with given email
     * @param email email of admin
     * @throws IllegalArgumentException when email is null
     * @return admin if exists, otherwise null
     */
    AdminDto findByEmail(String email);

    /**
     * Updates an existing admin
     * @param admin admin to update
     * @throws IllegalArgumentException admin is null
     */
    void updateAdmin(AdminDto admin);

    /**
     * Deletes an admin with given id
     * @param adminId id of admin to delete
     */
    void removeAdmin(Long adminId);

}
