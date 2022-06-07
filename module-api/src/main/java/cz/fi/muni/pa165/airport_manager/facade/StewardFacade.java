package cz.fi.muni.pa165.airport_manager.facade;

import cz.fi.muni.pa165.airport_manager.dto.StewardDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Matej Michalek
 * This class represents facade API used for operations with steward entity.
 */

public interface StewardFacade {

    /**
     * Creates a steward.
     * @param steward steward to create
     * @throws IllegalArgumentException steward is null
     */
    void createSteward(StewardDto steward, String email);

    /**
     * Finds all stewards
     * @return list of all stewards
     */
    List<StewardDto> findAllStewards();

    /**
     * Finds a steward with given id
     * @param id id of steward
     * @throws IllegalArgumentException id is null
     * @return steward if exists, otherwise null
     */
    StewardDto findStewardById(Long id);

    /**
     * Updates an existing steward
     * @param steward steward to update
     * @throws IllegalArgumentException steward is null
     */
    void updateSteward(StewardDto steward);

    /**
     * Deletes a steward with given id
     * @param stewardId id of steward to delete
     */
    void removeSteward(Long stewardId);

    /**
     * Finds all stewards available for given period
     * @param from beginning of period
     * @param to end of period
     * @return list of all available stewards
     */
    List<StewardDto> findAllAvailableStewardsForPeriod(LocalDateTime from, LocalDateTime to);
}
