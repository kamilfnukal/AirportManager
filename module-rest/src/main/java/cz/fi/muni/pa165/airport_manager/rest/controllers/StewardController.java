package cz.fi.muni.pa165.airport_manager.rest.controllers;

import cz.fi.muni.pa165.airport_manager.dto.StewardDto;
import cz.fi.muni.pa165.airport_manager.exception.AirportManagerServiceException;
import cz.fi.muni.pa165.airport_manager.facade.StewardFacade;
import cz.fi.muni.pa165.airport_manager.rest.exceptions.IllegalEmailException;
import cz.fi.muni.pa165.airport_manager.rest.exceptions.ResourceAlreadyExistingException;
import cz.fi.muni.pa165.airport_manager.rest.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST Controller for Stewards
 * @author Martin Kalina
 */

@RestController
@RequestMapping("/rest/stewards")
public class StewardController {

    @Autowired
    private StewardFacade stewardFacade;


    /**
     * Get list of Stewards
     * curl -i -X GET http://localhost:9090/pa165/rest/stewards
     *
     * @return List<StewardDto>
     */
    @GetMapping()
    public final List<StewardDto> getStewards() {
        return stewardFacade.findAllStewards();
    }

    /**
     * Get single steward by id
     * curl -i -X GET http://localhost:9090/pa165/rest/stewards/{id}
     *
     * @param id identifier of steward
     * @return StewardDto
     */
    @GetMapping("/{id}")
    public StewardDto getSteward(@PathVariable("id") long id) throws ResourceNotFoundException {
        StewardDto stewardDto = stewardFacade.findStewardById(id);
        if (stewardDto != null) {
            return stewardDto;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Delete steward by id
     * curl -i -X DELETE http://localhost:9090/pa165/rest/stewards/{id}
     *
     * @param id identifier of steward
     * @throws ResourceNotFoundException when the steward with the id does not exist
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public final void deleteSteward(@PathVariable("id") long id) throws ResourceNotFoundException {

        try {
            stewardFacade.removeSteward(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Create a new steward
     * curl -X POST -i -H "Content-Type: application/json" --data
     * '{"firstName":"test","lastName":"test","citizenId":"test"}'
     * http://localhost:9090/pa165/rest/stewards/create?{userEmail=}
     *
     * @param steward with required fields for creation
     *
     * @throws ResourceAlreadyExistingException when the steward with the id already exist
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createSteward(@Valid @RequestBody StewardDto steward, @RequestParam String userEmail) throws ResourceAlreadyExistingException {
        System.out.println(userEmail);

        try {
            stewardFacade.createSteward(steward, userEmail);
        } catch (AirportManagerServiceException ex) {
            throw new ResourceAlreadyExistingException();
        } catch (IllegalArgumentException exx) {
            throw new IllegalEmailException();
        }
    }

    /**
     * Update steward by PUT method
     * curl -X PUT -i -H "Content-Type: application/json" --data
     * '{"firstName":"test","lastName":"test","citizenId":"test"}'
     * http://localhost:9090/pa165/rest/stewards/{id}
     *
     * @param id identifier of steward
     * @param steward required fields for update
     * @throws ResourceNotFoundException when steward with id doesn't exist
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateSteward(@PathVariable("id") long id, @Valid @RequestBody StewardDto steward) throws ResourceNotFoundException {

        try {
            steward.setId(id);
            stewardFacade.updateSteward(steward);
        } catch (Exception esse) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Find all available stewards for time interval
     * curl -i -X GET "http://localhost:9090/pa165/rest/stewards/available?from=2022-07-14 15:00&to=2022-07-16 15:00"
     *
     * @param from beginning of interval
     * @param to end of interval
     * @return List<StewardDto>
     */
    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public final List<StewardDto> getStewardsForPeriod(@RequestParam String from, @RequestParam String to){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fromDateTime = LocalDateTime.parse(from, formatter);
        LocalDateTime toDateTime = LocalDateTime.parse(to, formatter);
        return stewardFacade.findAllAvailableStewardsForPeriod(fromDateTime, toDateTime);
    }

}
