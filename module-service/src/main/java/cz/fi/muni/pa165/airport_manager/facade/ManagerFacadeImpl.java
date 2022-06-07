package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.ManagerDto;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.service.ManagerService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents implementation of ManagerFacade
 */

@Service
public class ManagerFacadeImpl implements ManagerFacade {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createManager(ManagerDto manager) {
        if(manager == null)
            throw new IllegalArgumentException("Manager DTO is null");
        var mappedManager = beanMappingService.mapTo(manager, Manager.class);
        managerService.createManager(mappedManager);
    }

    @Override
    public List<ManagerDto> findAllManagers() {
        return beanMappingService.mapTo(managerService.findAllManagers(), ManagerDto.class);
    }

    @Override
    public ManagerDto findManagerById(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is null");

        var manager = managerService.findManagerById(id);
        return manager == null ? null : beanMappingService.mapTo(manager, ManagerDto.class);
    }

    @Override
    public ManagerDto findByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is null");

        var manager = managerService.findByEmail(email);
        return manager == null ? null : beanMappingService.mapTo(manager, ManagerDto.class);
    }

    @Override
    public void updateManager(ManagerDto manager) {
        if(manager == null)
            throw new IllegalArgumentException("Manager DTO is null");
        var mappedManager = beanMappingService.mapTo(manager, Manager.class);
        managerService.updateManager(mappedManager);
    }

    @Override
    public void removeManager(Long managerId) {
        managerService.removeManager(managerId);
    }

}
