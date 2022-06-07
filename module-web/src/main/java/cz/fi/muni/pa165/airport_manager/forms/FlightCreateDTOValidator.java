package cz.fi.muni.pa165.airport_manager.forms;

import cz.fi.muni.pa165.airport_manager.dto.FlightCreateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Pavel Sklenar
 * Class used for additional validation in case of creating/updating a flight.
 */
public class FlightCreateDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return FlightCreateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var flightCreateDto = (FlightCreateDto) target;

        var departureTime = flightCreateDto.getDepartureTime();
        var arrivalTime = flightCreateDto.getArrivalTime();
        if (arrivalTime != null && departureTime != null && !arrivalTime.isAfter(departureTime)) {
            errors.rejectValue("arrivalTime", "arrivalBeforeDeparture", "Arrival time must be after departure time");
        }

        var origin = flightCreateDto.getOriginId();
        var destination = flightCreateDto.getDestinationId();
        if (origin != null && origin > 0L && destination != null && destination > 0L && origin.equals(destination)) {
            errors.rejectValue("destinationId", "destinationSameAsOrigin", "Destination must be different from origin");
        }
    }
}
