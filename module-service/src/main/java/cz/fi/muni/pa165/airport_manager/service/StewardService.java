package cz.fi.muni.pa165.airport_manager.service;

import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Matej Michálek
 * This class represents service methods used for operations with steward entity.
 */

public interface StewardService {

    /**
     * Creates a steward.
     * @param steward steward to create
     * @param email email of a user who is creating this airplane
     * @throws AirportManagerServiceException steward is null
     */
    void createSteward(Steward steward, String email);

    /**
     * Finds all stewards
     * @return list of all stewards
     */
    List<Steward> findAllStewards();

    /**
     * Finds a steward with given id
     * @param id id of steward
     * @throws AirportManagerServiceException id is null
     * @return steward if exists, otherwise null
     */
    Steward findStewardById(Long id);

    /**
     * Updates an existing steward
     * @param steward steward to update
     * @throws AirportManagerServiceException steward is null
     */
    void updateSteward(Steward steward);

    /**
     * Deletes a steward with given id.
     * Firstly, it removes reference from admin who created this steward.
     * Secondly, it removes references to given steward from flights.
     * Finally, it deletes a steward.
     * @param stewardId id of steward to delete
     * @throws AirportManagerServiceException stewardId is null
     */
    void removeSteward(Long stewardId);

    /**
     * Finds all stewards available for given period
     * @param from beginning of period
     * @param to end of period
     * @throws AirportManagerServiceException from or to is null, or from is after to
     * @return list of all available stewards
     */
    List<Steward> findAllAvailableStewardsForPeriod(LocalDateTime from, LocalDateTime to);

}
