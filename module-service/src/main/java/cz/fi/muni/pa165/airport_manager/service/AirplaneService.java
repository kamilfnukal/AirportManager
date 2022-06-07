package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents service methods used for operations with airplane entity.
 */

public interface AirplaneService {

    /**
     * Creates an airplane.
     * @param airplane airplane to create
     * @param email email of a user who is creating this airplane
     * @throws AirportManagerServiceException airplane is null, or email is null, or when airplane with given serialNumber already exists
     */
    void createAirplane(Airplane airplane, String email);

    /**
     * Finds all airplanes
     * @return list of all airplanes
     */
    List<Airplane> findAllAirplanes();

    /**
     * Finds an airplane with given id
     * @param id id of airplane
     * @throws AirportManagerServiceException id is null
     * @return airplane if exists, otherwise null
     */
    Airplane findAirplaneById(Long id);

    /**
     * Updates an existing airplane
     * @param airplane airplane to update
     * @throws AirportManagerServiceException airplane is null, or when airplane with given serialNumber already exists
     */
    void updateAirplane(Airplane airplane);

    /**
     * Deletes an airplane with given id.
     * Firstly, it removes reference from admin who created this airplane.
     * Secondly, it deletes flights which correspond to this airplane.
     * Finally, it deletes an airplane.
     * @param airplaneId id of airplane to delete
     * @throws AirportManagerServiceException airplaneId is null
     */
    void removeAirplane(Long airplaneId);

    /**
     * Finds all airplanes available for given period
     * @param from beginning of period
     * @param to end of period
     * @throws AirportManagerServiceException from or to is null, or from is after to
     * @return list of all available airplanes
     */
    List<Airplane> findAllAvailableAirplanesForPeriod(LocalDateTime from, LocalDateTime to);
}