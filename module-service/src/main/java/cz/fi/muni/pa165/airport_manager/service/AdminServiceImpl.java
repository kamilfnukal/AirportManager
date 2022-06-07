package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents service layer for Admin entity.
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public void createAdmin(Admin admin) {
        if(admin == null)
            throw new AirportManagerServiceException("Admin is null.");
        adminDao.create(admin);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return adminDao.findAll();
    }

    @Override
    public Admin findAdminById(Long id) {
        if(id == null)
            throw new AirportManagerServiceException("ID is null.");
        return adminDao.findById(id);
    }

    @Override
    public Admin findByEmail(String email) {
        if(email == null)
            throw new AirportManagerServiceException("Email is null.");
        return adminDao.findByEmail(email);
    }

    @Override
    public void updateAdmin(Admin admin) {
        if(admin == null)
            throw new AirportManagerServiceException("Admin is null.");
        try{
            adminDao.update(admin);
        } catch (ObjectNotFoundException e){
            throw new AirportManagerServiceException("Admin to update could not be found.", e);
        }
    }

    @Override
    public void removeAdmin(Long adminId) {
        if(adminId == null)
            throw new AirportManagerServiceException("Admin ID is null.");
        try{
            adminDao.remove(adminId);
        } catch (ObjectNotFoundException e){
            throw new AirportManagerServiceException("Admin to remove could not be found.", e);
        }
    }

}
