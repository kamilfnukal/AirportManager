package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents service layer for Manager entity.
 */

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerDao managerDao;

    @Override
    public void createManager(Manager manager) {
        if(manager == null)
            throw new AirportManagerServiceException("Manager is null.");
        managerDao.create(manager);
    }

    @Override
    public List<Manager> findAllManagers() {
        return managerDao.findAll();
    }

    @Override
    public Manager findManagerById(Long id) {
        if(id == null)
            throw new AirportManagerServiceException("Id is null.");
        return managerDao.findById(id);
    }

    @Override
    public Manager findByEmail(String email) {
        if(email == null)
            throw new AirportManagerServiceException("Email is null.");
        return managerDao.findByEmail(email);
    }

    @Override
    public void updateManager(Manager manager) {
        if(manager == null)
            throw new AirportManagerServiceException("Manager is null.");
        try{
            managerDao.update(manager);
        } catch (ObjectNotFoundException e){
            throw new AirportManagerServiceException("Manager to update could not be found.", e);
        }
    }

    @Override
    public void removeManager(Long managerId) {
        if(managerId == null)
            throw new AirportManagerServiceException("Manager ID is null.");
        try{
            managerDao.remove(managerId);
        } catch (ObjectNotFoundException e){
            throw new AirportManagerServiceException("Manager to remove could not be found.", e);
        }
    }

}
