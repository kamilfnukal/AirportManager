package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.dao.AdminDao;
import cz.fi.muni.pa165.airport_manager.dao.ManagerDao;
import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author Pavel Sklenar
 * Class which is used for customizable user details in case of logging into information system
 */
@Service
public class CustomLoginServiceImpl implements UserDetailsService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private ManagerDao managerDao;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try{
            final Admin admin = adminDao.findByEmail(email);
            if (admin == null) {
                throw new AirportManagerServiceException("Admin is null.");
            }
            return User.withUsername(admin.getEmail()).password(admin.getPassword()).authorities("ADMIN").build();
        }catch (Exception e){
            final Manager manager = managerDao.findByEmail(email);
            if(manager == null){
                throw new AirportManagerServiceException("Manager is null.");
            }
            return User.withUsername(manager.getEmail()).password(manager.getPassword()).authorities("MANAGER").build();
        }
    }

}
