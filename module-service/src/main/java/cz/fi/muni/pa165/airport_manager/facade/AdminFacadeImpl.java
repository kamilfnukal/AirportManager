package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AdminDto;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.service.AdminService;
import cz.fi.muni.pa165.airport_manager.service.BeanMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kamil Fňukal
 * This class represents implementation of AdminFacade
 */

@Service
public class AdminFacadeImpl implements AdminFacade {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createAdmin(AdminDto admin) {
        if(admin == null)
            throw new IllegalArgumentException("Admin DTO is null");
        var mappedAdmin = beanMappingService.mapTo(admin, Admin.class);
        adminService.createAdmin(mappedAdmin);
    }

    @Override
    public List<AdminDto> findAllAdmins() {
        return beanMappingService.mapTo(adminService.findAllAdmins(), AdminDto.class);
    }

    @Override
    public AdminDto findAdminById(Long id) {
        if (id == null) throw new IllegalArgumentException("ID is null.");

        var admin = adminService.findAdminById(id);
        return admin == null ? null : beanMappingService.mapTo(admin, AdminDto.class);
    }

    @Override
    public AdminDto findByEmail(String email) {
        if (email == null) throw new IllegalArgumentException("Email is null.");

        var admin = adminService.findByEmail(email);
        return admin == null ? null : beanMappingService.mapTo(admin, AdminDto.class);
    }

    @Override
    public void updateAdmin(AdminDto admin) {
        if(admin == null)
            throw new IllegalArgumentException("Admin DTO is null");
        var mappedAdmin = beanMappingService.mapTo(admin, Admin.class);
        adminService.updateAdmin(mappedAdmin);
    }

    @Override
    public void removeAdmin(Long adminId) {
        adminService.removeAdmin(adminId);
    }

}
