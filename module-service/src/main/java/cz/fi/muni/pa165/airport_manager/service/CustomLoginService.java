package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Pavel Sklenar
 */
public interface CustomLoginService {

    /**
     * creates User (spring security package) from Manager or Admin Entitites
     * @param email email of user
     * @throws AirportManagerServiceException when user with given email does not exist
     * @return new user based on spring security package
     */
    public UserDetails loadUserByUsername(String email);

}
