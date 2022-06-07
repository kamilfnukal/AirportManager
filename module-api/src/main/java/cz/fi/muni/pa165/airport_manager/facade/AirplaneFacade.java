package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.AirplaneDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Sklenar
 * This class represents facade API used for operations with airplane entity.
 */

public interface AirplaneFacade {

    /**
     * Creates an airplane.
     * @param airplane airplane to create
     * @param email email of a user who is creating this airplane
     * @throws IllegalArgumentException airplane is null, or email is null
     */
    void createAirplane(AirplaneDto airplane, String email);

    /**
     * Finds all airplanes
     * @return list of all airplanes
     */
    List<AirplaneDto> findAllAirplanes();

    /**
     * Finds an airplane with given id
     * @param id id of airplane
     * @throws IllegalArgumentException id is null
     * @return airplane if exists, otherwise null
     */
    AirplaneDto findAirplaneById(Long id);

    /**
     * Updates an existing airplane
     * @param airplane airplane to update
     * @throws IllegalArgumentException airplane is null
     */
    void updateAirplane(AirplaneDto airplane);

    /**
     * Deletes an airplane with given id
     * @param airplaneId id of airplane to delete
     */
    void removeAirplane(Long airplaneId);


    /**
     * Finds all airplanes available for given period
     * @param from beginning of period
     * @param to end of period
     * @return list of all available airplanes
     */
    List<AirplaneDto> findAllAvailableAirplanesForPeriod(LocalDateTime from, LocalDateTime to);
}
